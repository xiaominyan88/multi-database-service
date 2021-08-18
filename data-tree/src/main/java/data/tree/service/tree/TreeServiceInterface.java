package data.tree.service.tree;


import data.tree.bean.DeviceData;
import data.tree.bean.ParameterBean;
import data.tree.bean.TreeNode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TreeServiceInterface {

    public List<String> findNodeId(ParameterBean parameter);

    public void selectDeviceTree(TreeNode node, List<String> ids);

    public List<String> getTransformerPoint(String devId);

    public List<String> getPoint(String className, String devId);

    public boolean checkPoint(List<String> pointIds);

    public boolean checkPointTags(List<String> pointIds, String tags);

    public void recurveAndFillCollection(DeviceData top, List<DeviceData> dataList, String tags);

    public void setAndRemoveDevice(DeviceData dd);

    public void setDeviceCircuitAndType(DeviceData dd);

    public void removeDeviceByClassName(DeviceData dd, String className);

}
