package data.tree.service.tree;


import data.tree.bean.*;
import data.tree.dao.mysql.eidp.MysqlEidpMapper;
import data.tree.dao.mysql.zhny.MysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Component
public abstract class AbstractTreeService implements TreeServiceInterface {

    private static int coreNum = Runtime.getRuntime().availableProcessors();

    public static final ExecutorService executor = new ThreadPoolExecutor(coreNum,
            coreNum+10, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());

    private static String filterClassName = "MeterPSR,ControlTerminalPSR,PWTransformerAsset,PWMeterAsset,CommunicationAsset";

    @Autowired
    MysqlEidpMapper mysqlEidpMapper;

    @Autowired
    MysqlMapper mysqlMapper;


    public Map<String,String> findCodeMap(String codeType){
        return mysqlEidpMapper.findCodeMap(codeType);
    }

    public DeviceData findDeviceType(String devId, String className){
        return mysqlEidpMapper.findDeviceTypeAndClassName(devId, className);
    }

    public List<String> findMeasureIdFromDyLine(String devId,String className){
        return mysqlEidpMapper.findMeasureIdFromDyLine(devId, className);
    }

    @Override
    public List<String> findNodeId(ParameterBean parameter){
        TreeParameterBean treeParameter = new TreeParameterBean();
        if(parameter.getId()!=null){
            treeParameter.setId(parameter.getId());
        }else{
            treeParameter.setId(parameter.getStationId());
        }
        treeParameter.setTableName(parameter.getTable());
        treeParameter.setType(parameter.getType());
        List<TreeResultBean> resultBeans = mysqlEidpMapper.findNodeId(treeParameter);
        List<String> list = new ArrayList<>();
        resultBeans.forEach(x->list.add(x.getNodeId()));
        return list;
    }

    @Override
    public void selectDeviceTree(TreeNode node, List<String> ids){
        List<TreeResultBean> findBelongNode =
                mysqlEidpMapper.findBelongNode(new WeakReference<>(new TreeParameterBean(node.getNodeId(), "")).get());
        for(TreeResultBean belongNode : findBelongNode){
            TreeNode childNode = new TreeNode();
            childNode.setParentNodeId(belongNode.getParentNodeId());
            childNode.setNodeId(belongNode.getNodeId());
            List<TreeResultBean> childNodeInformation = mysqlEidpMapper.findChildNodeInformation
                    (new WeakReference<>(new TreeParameterBean(belongNode.getNodeId())).get());
            //子节点表一一对应
            if (childNodeInformation.size() > 0) {
                childNode.setClassId(childNodeInformation.get(0).getClassId());
                childNode.setNodeName(childNodeInformation.get(0).getNodeName());
                childNode.setNodeType(childNodeInformation.get(0).getNodeType());
                List<TreeResultBean> childNodeTable = mysqlEidpMapper.findChildNodeTable
                        (new WeakReference<>(
                                new TreeParameterBean("", "", childNode.getClassId())).get());
                if (childNodeTable.size() > 0) {
                    childNode.setTableName(childNodeTable.get(0).getTableName());
                }
                childNode.setParentNodeType(node.getNodeType());
                //ids为空则无子类
                if (ids.size()>0) {
                    if (!childNode.getNodeType().equals("0")) {
                        // 子类型nodeType 不等于0 只能是传ids 不能做虚拟节点的查询
                        childNode.setIds(ids);
                        // 获取虚拟节点下个节点
                        List<DeviceData> oldDevices = node.getDeviceData();
                        // 子类引用给父类
                        node.setDeviceData(childNode.getDeviceData());
                        node.getDeviceData().addAll(oldDevices);
                        selectDeviceTree(childNode, childNode.getIds());
                    } else {
                        if ("ME_CONF_MEASUREPOINT".equals(childNode.getTableName())) {

                        } else {
                            List<TreeResultBean> flag = mysqlEidpMapper.findFlagStatus(
                                    new WeakReference<>(
                                            new TreeParameterBean("", "", "", childNode.getTableName())).get()
                            );
                            List<TreeResultBean> deviceData = childNode.getTableName()!=null?mysqlEidpMapper.findDeviceData(
                                    new WeakReference<>(
                                            new TreeParameterBean("","", "", "", childNode.getTableName(),ids, flag.size()!= 0 ? "1" : null)).get()
                            ):new ArrayList<>();
                            List<String> tempIds=new ArrayList<>();
                            String names ="";
                            for (TreeResultBean x : deviceData) {
                                DeviceData deviceDataWeakReference = new DeviceData();
                                deviceDataWeakReference.setId(x.getId());
                                deviceDataWeakReference.setName(x.getName());
                                deviceDataWeakReference.setParentId(x.getParentId());
                                deviceDataWeakReference.setClassName(x.getClassName());
                                if ( flag.size()!= 0) {
                                    deviceDataWeakReference.setRunStatus(x.getRunStatus());
                                }
                                childNode.addDevice(deviceDataWeakReference);
                                tempIds.add(x.getId());
                                names = names + x.getName() + ",";
                            }
                            if (deviceData.size() > 0) {
                                node.addChildDeviceDataList(childNode.getDeviceData());
                            }
                            childNode.setIds(tempIds);
                        }
                        node.getChildNodes().add(childNode);
                        selectDeviceTree( childNode,childNode.getIds());
                    }
                }
            }
        }

    }

    @Override
    public List<String> getTransformerPoint(String devId){
        return mysqlEidpMapper.getTransformerPoint(devId);
    }

    @Override
    public List<String> getPoint(String className,String devId){
        return mysqlEidpMapper.getPoint(className.toUpperCase(),devId);
    }

    @Override
    public boolean checkPoint(List<String> pointIds){
        int num = mysqlMapper.checkPoint(pointIds);
        if(num > 0) return true;
        else return false;
    }

    @Override
    public boolean checkPointTags(List<String> pointIds,String tags){
        int num = mysqlMapper.checkPointTags(pointIds,tags);
        if(num > 0) return true;
        else return false;
    }

    @Override
    public void recurveAndFillCollection(DeviceData top,List<DeviceData> dataList,String tags){
        DeviceData tempDd = new DeviceData();
        tempDd.setId(top.getId());
        tempDd.setClassName(top.getClassName());
        tempDd.setKey(top.getId());
        tempDd.setName(top.getName());
        tempDd.setRunStatus(top.getRunStatus());
        if(filterClassName.contains(tempDd.getClassName())){

        }else{
            boolean flag = false;
            if("PWTransformerPSR".equals(tempDd.getClassName())){
                flag = checkHasTransformer(tempDd.getId(),tags);
            }else{
                flag = checkHas(tempDd.getId(),tags,tempDd.getClassName());
            }
            if(flag){
                setDeviceCircuitAndType(tempDd);
                dataList.add(tempDd);
            }
        }

        if(top.getList().size() > 0){
            for(int p = 0; p < top.getList().size(); p++){
                DeviceData dd = top.getList().get(p);
                recurveAndFillCollection(dd,dataList,tags);
            }
        }
    }

    public void recurveAndFillCollection(DeviceData top,List<DeviceData> dataList){
        DeviceData tempDd = new DeviceData();
        tempDd.setId(top.getId());
        tempDd.setClassName(top.getClassName());
        tempDd.setKey(top.getId());
        tempDd.setName(top.getName());
        tempDd.setRunStatus(top.getRunStatus());
        if(filterClassName.contains(tempDd.getClassName())){

        }else{
            dataList.add(tempDd);
        }

        if(top.getList().size() > 0){
            for(int p = 0; p < top.getList().size(); p++){
                DeviceData dd = top.getList().get(p);
                recurveAndFillCollection(dd,dataList);
            }
        }
    }

    @Override
    public void setAndRemoveDevice(DeviceData dd){
        List<DeviceData> list = dd.getList();
        for(int i = 0; i < list.size(); i++){
            DeviceData deviceData = list.get(i);
            boolean flag = getData(deviceData);
            if(!flag){
                deviceData.setNoData("0");
                i--;
                list.remove(deviceData);
                list.addAll(deviceData.getList());
            }else{
                if(deviceData.getList().size() > 0){
                    for(int j = 0; j < deviceData.getList().size(); j++){
                        setAndRemoveDevice(deviceData.getList().get(j));
                    }
                }
            }
        }

    }

    @Override
    public void setDeviceCircuitAndType(DeviceData dd){
        if("DYBayPSR".equals(dd.getClassName())
                || "PWBayPSR".equals(dd.getClassName())){
            CircuitBean circuitBean = mysqlEidpMapper.isCircuit(dd);
            dd.setIsCircuit(circuitBean.getIsCircuit());
            if(circuitBean.getType() != null) dd.setType(circuitBean.getType());
        }
    }

    @Override
    public void removeDeviceByClassName(DeviceData dd, String className){
        List<DeviceData> deviceDatas = dd.getList();
        for(int i = 0; i < deviceDatas.size(); i++){
            DeviceData temp = deviceDatas.get(i);
            if (!className.contains(temp.getClassName().toLowerCase())) {
                i--;
                deviceDatas.remove(temp);
                deviceDatas.addAll(temp.getList());
            }
        }
        if (deviceDatas.size() > 0) {
            for (int j = 0; j < deviceDatas.size(); j++) {

                removeDeviceByClassName(deviceDatas.get(j), className);

            }
        }
    }


    public abstract <T> T treeService();

    private boolean getData(DeviceData dd){
        List<String> pointIds = getPoint(dd.getClassName(),dd.getId());
        if(pointIds.size() > 0){
            boolean check = checkPoint(pointIds);
            if(!check){
                if(dd.getList().size() > 0){
                    for(int i = 0; i < dd.getList().size(); i++){
                        boolean re = getData(dd.getList().get(i));
                        if(re) return true;
                    }
                }
            }else{
                return true;
            }
        }
        return false;
    }

    private boolean checkHasTransformer(String devId,String tags){
        List<String> pointIds = getTransformerPoint(devId);
        if(pointIds.size() > 0){
            return checkPointTags(pointIds,tags);
        }
        return false;
    }

    private boolean checkHas(String devId,String tags,String className){
        List<String> pointIds = getPoint(className, devId);
        if(pointIds.size() > 0){
            return checkPointTags(pointIds,tags);
        }
        return false;
    }
}
