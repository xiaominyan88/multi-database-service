package data.clickhouse.bean;

import lombok.Data;

import java.util.List;


@Data
public class RequestParamVo {
    private List<String> measurePointIds;
    private String measurePointId;
    private String startTime;
    private String endTime;
    private List<String> measureTags;
    private String measureTag;
    private String tableName;
    private String tableIndex;
    private List<String> times;
    private String row;
    private String dateType;
    private String isStationId = "measurePointId";
    private Boolean filterStation = false;
}
