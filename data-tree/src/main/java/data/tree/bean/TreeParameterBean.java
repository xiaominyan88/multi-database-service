package data.tree.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeParameterBean {

    private String type;
    private String id;
    private String parentNodeId;
    private String nodeId;
    private String classId;
    private String tableName;
    private List<String> ids;
    private String flag;

    public TreeParameterBean(String id, String parentNodeId, String nodeId, String classId, String tableName, List<String> ids, String flag){
        this.id = id;
        this.parentNodeId = parentNodeId;
        this.nodeId = nodeId;
        this.classId = classId;
        this.tableName = tableName;
        this.ids = ids;
        this.flag = flag;
    }

    public TreeParameterBean(String id, String parentNodeId, String nodeId, String classId, String tableName, String flag) {
        this.id = id;
        this.parentNodeId = parentNodeId;
        this.nodeId = nodeId;
        this.classId = classId;
        this.tableName = tableName;
        this.flag = flag;
    }

    public TreeParameterBean(String parentNodeId, String nodeId, String classId, String tableName, String flag) {
        this.parentNodeId = parentNodeId;
        this.nodeId = nodeId;
        this.classId = classId;
        this.tableName = tableName;
        this.flag = flag;
    }

    public TreeParameterBean(String parentNodeId, String nodeId, String classId, String tableName) {
        this.parentNodeId = parentNodeId;
        this.nodeId = nodeId;
        this.classId = classId;
        this.tableName = tableName;
    }

    public TreeParameterBean(String parentNodeId, String nodeId, String classId) {
        this.parentNodeId = parentNodeId;
        this.nodeId = nodeId;
        this.classId = classId;
    }

    public TreeParameterBean(String parentNodeId, String nodeId) {
        this.parentNodeId = parentNodeId;
        this.nodeId = nodeId;
    }

    public TreeParameterBean(String nodeId) {
        this.nodeId = nodeId;
    }

}
