package data.tree.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNode implements Serializable {

    private static final long serialVersionUID = -382947095165281983L;
    private String parentNodeId;
    private String parentNodeType;
    private String nodeId;
    private List<TreeNode> childNodes = new ArrayList<TreeNode>();
    private String classId;
    private String nodeName;
    private String nodeType;
    private String tableName;
    private List<String> ids;
    private List<DeviceData> deviceData = new ArrayList<DeviceData>();

    public void addDevice(DeviceData dd) {
        deviceData.add(dd);
    }

    public void addChildDeviceDataList(List<DeviceData> tempList) {
        // 空的时候 直接加进去
        if (deviceData.size() == 0) {
            deviceData.addAll(tempList);

        } else {
            // 上级 和 下级 id一样，就加到上级里面去
            t:
            for (int i = 0; i < tempList.size(); i++) {
                DeviceData dd = tempList.get(i);
                boolean check = false;
                f:
                for (int j = 0; j < deviceData.size(); j++) {
                    DeviceData udd = deviceData.get(j);
                    if (dd.getParentId().equals(udd.getId())) {
                        check = true;
                        udd.setListData(dd);
                        break f;
                    }
                }
                if (!check) {
                    deviceData.add(dd);
                }
            }
        }

    }

}
