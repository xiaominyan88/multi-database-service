package data.tree.service.tree;

import com.google.common.collect.Lists;

import data.tree.bean.DeviceData;
import data.tree.bean.ParameterBean;
import data.tree.bean.TreeNode;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TreeGroupService extends AbstractTreeService {

    private static final String PWSTATIONPSR = "PWStationPSR";

    private static final String PHOTOVOLTAICSTATIONPSR = "PhotovoltaicStationPSR";

    private static final String CONSTRUCTIONSITEPSR = "ConstructionSitePSR";

    private static final String LAMPSTATION = "LampStation";

    private static String classNames = "PWBusbarAsset,PWCapacitorPSR,PWGroundDisconnectorPSR,PWArresterPSR,PWDisconnectorPSR,ControlTerminalPSR,ControlTerminalAsset," +
            "MeterPSR,MeterAsset,PWBreakerAsset,PWMeterAsset,PWTransformerAsset,CommunicationPSR,CommunicationAsset,PWPotentialTransformerPSR," +
            "subadministrationregion,dylinepsr,lightpolepsr,roadlamppsr";


    @Override
    public List<TreeNode> treeService(){

        List<TreeNode> listNodes = new CopyOnWriteArrayList<>();
        ParameterBean parameterBean = ParameterThreadLocal.get();
        List<String> nodeIds = findNodeId(parameterBean);
        if(nodeIds.size() > 0){
            List<List<String>> groupLists = Lists.partition(nodeIds,nodeIds.size());

            CompletableFuture[] completeFutures = groupLists.stream().map( m -> CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    TreeNode treeNode = new TreeNode();
                    treeNode.setNodeId(nodeIds.get(0));
                    treeNode.setTableName(parameterBean.getTable());
                    selectDeviceTree(treeNode, Collections.singletonList(parameterBean.getId()));
                    List<DeviceData> deviceDatas = treeNode.getDeviceData();
                    deviceDatas.stream().sorted((a,b)->{
                        Comparator<Object> com = Collator.getInstance(Locale.CHINA);
                        return com.compare(b.getName(), a.getName());
                    });
                    for(int i = 0; i < deviceDatas.size(); i++){
                        removeDevice(deviceDatas.get(i),0);
                    }
                    listNodes.add(treeNode);
                }
            }, executor)).toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(completeFutures).join();
        }
        return listNodes;
    }

    /**
     * 删除不符合业务要求的设备类型以及没有挂接测点的设备
     * @param dd
     * @param level
     */
    private void removeDevice(DeviceData dd, int level){
        List<DeviceData> devices = dd.getList();
        if(devices.size() > 0){
            for(int j = 0; j < devices.size(); j++){
                List<String> points = null;
                DeviceData deviceData = devices.get(j);
                String className = deviceData.getClassName();
                if(!"PWStationPSR".equals(className)
                        || !"PhotovoltaicStationPSR".equals(className)
                        || !"ConstructionSitePSR".equals(className)
                        || !"LampStation".equals(className)
                        || !"SubGeographicalRegion".equals(className)
                        || !"GeographicalRegion".equals(className)
                        || !"Company".equals(className)){
                    points = getPoint(deviceData.getClassName(), deviceData.getId());
                }
                if(classNames.contains(deviceData.getClassName()) ||
                        (points.size() == 0 && points != null)){
                    devices.remove(j);
                    j--;
                    devices.addAll(deviceData.getList());
                }
            }

        }else{
            for (int j = 0; j < devices.size(); j++) {

                removeDevice(devices.get(j), level + 1);

            }
        }
    }


}
