package data.tree.bean;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class ParameterBean {

    @NonNull
    @Setter
    @Getter
    private String id;

    @NonNull
    @Setter
    @Getter
    private String stationId;

    @NonNull
    @Setter
    @Getter
    private String table;

    @NonNull
    @Setter
    @Getter
    private String tagType;

    @NonNull
    @Setter
    @Getter
    private String deviceType;

    @NonNull
    @Getter
    private String type;

    @NonNull
    @Setter
    @Getter
    private String stationIdTypes;

    @NonNull
    @Setter
    @Getter
    private String devId;

    @NonNull
    @Setter
    @Getter
    private String devClassName;

    @NonNull
    @Getter
    @Setter
    private String className;

    @NonNull
    @Getter
    @Setter
    private String pointIds;

    public void setType(String type){
        this.type = type;
        this.table = "DEV_" + type.toUpperCase();
    }

}
