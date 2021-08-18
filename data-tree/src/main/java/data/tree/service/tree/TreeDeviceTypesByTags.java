package data.tree.service.tree;

import com.google.common.collect.Lists;

import data.tree.bean.DeviceData;
import data.tree.bean.ParameterBean;
import data.tree.bean.TreeNode;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TreeDeviceTypesByTags extends AbstractTreeService {

    private static List<DeviceData> dataList = new CopyOnWriteArrayList<>();

    private static Map<String,String> map = new HashMap<>();

    @Override
    public List<DeviceData> treeService(){
        ParameterBean parameterBean = ParameterThreadLocal.get();
        String stationId = parameterBean.getStationId();
        String type = parameterBean.getType();
        String tagType = parameterBean.getTagType();
        map = findCodeMap("DZ_JGLX");
        String tags = findTags(tagType);
        List<String> nodeIds = findNodeId(parameterBean);
        if(nodeIds.size() > 0){
            List<List<String>> groupLists = Lists.partition(nodeIds,nodeIds.size());
            CompletableFuture[] completeFutures = groupLists.stream().map(m -> CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run(){
                    List<DeviceData> list = new LinkedList<>();
                    TreeNode myNode = new TreeNode();
                    myNode.setNodeId(nodeIds.get(0));
                    myNode.setTableName("DEV_" + type);
                    selectDeviceTree(myNode, Collections.singletonList(stationId));
                    List<DeviceData> deviceDatalist = myNode.getDeviceData();
                    if(deviceDatalist.size() > 0){
                        for(DeviceData dd : deviceDatalist){
                            if(dd.getParentId().equals(stationId)){
                                recurveAndFillCollection(dd,list);
                            }
                        }
                    }

                    for(DeviceData dd : list){
                        if (dd.getClassName().contains("Asset")
                                || dd.getClassName().contains("MeterPSR")
                                || dd.getClassName().contains("ControlTerminalPSR")) {
                            continue;
                        }
                        boolean flag = false;
                        List<String> pointIds = getPoint(dd.getClassName(),dd.getId());
                        if(pointIds.size() > 0) flag = checkPointTags(pointIds,tags);
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

    private String findTags(String tagType){
        switch(tagType){
            case "1":
                return "'GEN.LI','GEN.Ia','GEN.Ib','GEN.Ic',"
                        + "'GEN.SPI','GEN.Temp','GEN.tempA','GEN.tempB','GEN.tempC','GEN.ET'";
            case "2":
                return "'GEN.F','GEN.Fa','GEN.Fb','GEN.Fc'";
            case "3":
                return "'GEN.PFt','GEN.Pfa','GEN.PFb','GEN.PFc'";
            case "4":
                return "'UaOffset','UbOffset','UcOffset'";
            case "5":
                return "'GEN.UaTHD','GEN.UbTHD','GEN.UcTHD','GEN.UTHD'";
            case "6":
                return "'GEN.HIa2','GEN.HIa3','GEN.HIb2','GEN.HIb3','GEN.HIc2','GEN.HIc3'";
            case "7":
                return "'GEN.Ia','GEN.Ib','GEN.Ic','GEN.Iac','GEN.Iab','GEN.Ibc'";
            default:
                return null;
        }
    }
}
