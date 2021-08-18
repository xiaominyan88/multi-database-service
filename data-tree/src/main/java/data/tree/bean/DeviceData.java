package data.tree.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeviceData implements Serializable {

    private static final long serialVersionUID = 3198722343798925092L;
    private String id;
    private String key;
    private String name;
    private String parentId;
    private String className;
    private String runStatus;
    private String isCircuit;
    private List<DeviceData> list = new ArrayList<DeviceData>();
    private String noData;
    private String type;

    public void setListData(DeviceData deviceData){
        this.list.add(deviceData);
    }
}
