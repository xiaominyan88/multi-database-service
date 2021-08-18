package data.tree.service.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;

import data.tree.bean.DeviceData;
import data.tree.bean.ParameterBean;
import data.tree.bean.TreeNode;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TreePointService extends AbstractTreeService {

    private static ConcurrentMap<String,List<String>> map = new MapMaker().concurrencyLevel(15).makeMap();
    private static List<String> list = new CopyOnWriteArrayList<>();


    @Override
    public Map<String,List<String>> treeService(){

        ParameterBean parameterBean = ParameterThreadLocal.get();
        List<String> nodeIds = findNodeId(parameterBean);
        if(nodeIds.size() > 0){
            List<List<String>> groupLists = Lists.partition(nodeIds,nodeIds.size());
            CompletableFuture[] completeFutures = groupLists.stream().map(m -> CompletableFuture.runAsync(new Runnable(){
                @Override
                public void run(){
                    List<String> dataList = new ArrayList<>();
                    TreeNode myNode = new TreeNode();
                    myNode.setNodeId(nodeIds.get(0));
                    myNode.setTableName(parameterBean.getTable());
                    myNode.setNodeType("0");
                    selectDeviceTree(myNode, Collections.singletonList(parameterBean.getStationId()));
                    List<DeviceData> deviceDatas = myNode.getDeviceData();
                    for(int i = 0; i < deviceDatas.size(); i++){
                        DeviceData dd = deviceDatas.get(i);
                        if(dd.getParentId().equals(parameterBean.getStationId())){
                            checkData(dd,dataList);
                            list.addAll(dataList);
                        }
                    }
                }
            }, executor)).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(completeFutures).join();
            map.putIfAbsent(parameterBean.getStationId(),list);
        }
        return map;
    }

    private void checkData(DeviceData dd,List<String> dataList){
        String devId = dd.getId();
        List<String> pointIds = getPoint("",devId);
        if(pointIds.size() > 0){
            dataList.addAll(pointIds);
        }
        if(dd.getList().size() > 0){
            for(int p = 0; p < dd.getList().size(); p++){
                DeviceData dds = dd.getList().get(p);
                checkData(dds,dataList);
            }
        }
    }
}
