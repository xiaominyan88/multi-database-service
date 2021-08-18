package data.tree.service.tree;


import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;

import data.tree.bean.DeviceData;
import data.tree.bean.ParameterBean;
import data.tree.bean.TreeNode;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TreeDevicesService extends AbstractTreeService {

    private static List<DeviceData> dataList = new CopyOnWriteArrayList<>();

    private volatile String tags;

    @Override
    public List<DeviceData> treeService(){
        ParameterBean parameterBean = ParameterThreadLocal.get();
        String stationId = parameterBean.getStationId();
        String table = parameterBean.getTable();
        String deviceType = parameterBean.getDeviceType();
        String tagType = parameterBean.getTagType();
        tags = findTags(tagType);
        List<String> nodeIds = findNodeId(parameterBean);
        if(nodeIds.size() > 0){
            List<List<String>> groupLists = Lists.partition(nodeIds,nodeIds.size());

            CompletableFuture[] completeFutures = groupLists.stream().map(m -> CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run(){
                    List<DeviceData> list = new CopyOnWriteArrayList<>();
                    TreeNode myNode = new TreeNode();
                    myNode.setNodeId(nodeIds.get(0));
                    myNode.setTableName(table);
                    selectDeviceTree(myNode, Collections.singletonList(stationId));
                    List<DeviceData> dds = myNode.getDeviceData();
                    if(dds.size() > 0){
                        if(dds.get(0).getParentId().equals(stationId)){
                            if(!"PWTransformerPSR".equals(deviceType)){
                                for(DeviceData dd : dds){
                                    setAndRemoveDevice(dd);
                                }
                            }

                            for(DeviceData dd : dds){
                                recurveAndFillCollection(dd,list,tags);
                            }

                        }
                    }
                    if(StringUtils.isEmpty(deviceType) || "-1".equals(deviceType)){
                        dataList.addAll(list);
                    }else{
                        String[] deviceTypeArray = deviceType.split(",");
                        List<DeviceData> tempList = new CopyOnWriteArrayList<>();
                        for(DeviceData dd : list){
                            for(int i = 0; i < deviceTypeArray.length; i++){
                                String temp = deviceTypeArray[i];
                                if(temp.startsWith("PWBayPSR")){
                                    String[] tt = temp.split("-");
                                    if(tt[1].equals(dd.getType())){
                                        tempList.add(dd);
                                    }
                                }else{
                                    if(Collections.singletonList(deviceType).contains(dd.getClassName())){
                                        tempList.add(dd);
                                    }
                                }
                            }
                        }
                        dataList.addAll(tempList);
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
                return "'ANotBalanceRate','BNotBalanceRate','CNotBalanceRate','NotBalanceRate'";
            case "2":
                return "'SwitchOpenCount','SwitchCloseCount'";
            case "3":
                return "'GEN.F','GEN.Fa','GEN.Fb','GEN.Fc'";
            case "4":
                return "'GEN.PFt','GEN.Pfa','GEN.PFb','GEN.PFc'";
            case "5":
                return "'UaOffset','UbOffset','UcOffset'";
            case "6":
                return "'GEN.UaTHD','GEN.UbTHD','GEN.UcTHD','GEN.UTHD'";
            case "7":
                return "'GEN.HIa2','GEN.HIb2','GEN.HIc2'";
            case "8":
                return "'GEN.kWh','GEN.CurkWhRec','GEN.CurkWhRecFee1','GEN.CurkWhRecFee2','GEN.CurkWhRecFee3','GEN.CurkWhRecFee4',"
                        + "'GEN.FroDkWhRec','GEN.FroDkWhRecFee1','GEN.FroDkWhRecFee2','GEN.FroDkWhRecFee3','GEN.FroDkWhRecFee4'";
            case "9":
                return "'LoadRate','ALoadRate','BLoadRate','CLoadRate'";
            default:
                return null;
        }
    }


}
