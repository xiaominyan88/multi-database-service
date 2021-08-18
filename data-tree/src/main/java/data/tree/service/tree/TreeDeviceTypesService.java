package data.tree.service.tree;

import com.google.common.collect.Lists;

import data.tree.bean.DeviceData;
import data.tree.bean.ParameterBean;
import data.tree.bean.TreeNode;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TreeDeviceTypesService extends AbstractTreeService {

    private static Map<String,String> map = new HashMap<>();

    private static List<DeviceData> dataList = new CopyOnWriteArrayList<>();

    private static String filterClassName = "subadministrationregion,dylinepsr,lightpolepsr,roadlamppsr";

    @Override
    public List<DeviceData> treeService(){
        ParameterBean parameterBean = ParameterThreadLocal.get();
        String stationId = parameterBean.getStationId();
        String type = parameterBean.getType();
        map = findCodeMap("DZ_JGLX");
        List<String> nodeIds = findNodeId(parameterBean);
        if(nodeIds.size()>0){
            List<List<String>> groupLists = Lists.partition(nodeIds,nodeIds.size());

            CompletableFuture[] completeFutures = groupLists.stream().map(m -> CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run(){
                    List<DeviceData> list = new CopyOnWriteArrayList<>();
                    TreeNode myNode = new TreeNode();
                    myNode.setNodeId(nodeIds.get(0));
                    myNode.setTableName("DEV_" + type);
                    selectDeviceTree(myNode, Collections.singletonList(stationId));
                    if("LampStation".equals(type)){
                        List<DeviceData> deviceDatalist = myNode.getDeviceData();
                        for(DeviceData dd : deviceDatalist){
                            removeDeviceByClassName(dd,filterClassName);
                        }

                        for(DeviceData dd : deviceDatalist){
                            if(dd.getParentId().equals(stationId)){
                                recurveAndFillCollection(dd,list);
                            }
                        }
                    }else{
                        List<DeviceData> deviceDatalist = myNode.getDeviceData();

                        for(DeviceData dd : deviceDatalist){
                            if(dd.getParentId().equals(stationId)){
                                recurveAndFillCollection(dd,list);
                            }
                        }
                    }

                    for(DeviceData dd : list){
                        if (dd.getClassName().contains("Asset")
                                || dd.getClassName().contains("MeterPSR")
                                || dd.getName().contains("ControlTerminalPSR")) {
                            continue;
                        }
                        boolean flag = false;
                        List<String> pointIds = getPoint(dd.getClassName(),dd.getId());
                        if(pointIds.size() > 0) flag = checkPoint(pointIds);
                        if(flag){

                            DeviceData deviceData = findDeviceType(dd.getId(),dd.getClassName());

                            if("PWBayPSR".equals(dd.getClassName())){
                                if(!dataList.contains(deviceData)){
                                    deviceData.setName(map.get(deviceData.getName()));
                                    dataList.add(deviceData);
                                }
                            }else{
                                if(!dataList.contains(deviceData)){
                                    dataList.add(deviceData);
                                }
                            }

                        }
                    }
                }
            }, executor)).toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(completeFutures).join();
        }
        return dataList;
    }
}
