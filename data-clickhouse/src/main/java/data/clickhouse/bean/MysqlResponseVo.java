package data.clickhouse.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MysqlResponseVo {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tableIndex;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String measurePointId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int count;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stationId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pathName;
    /**
     * 组件数量
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String assemblyNo;
    /**
     * 消纳率
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String acceptanceRate;
    /**
     * 电价
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String comprehensiveElectricityPrice;
    /**
     * 综合Id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String multipleId;
    /**
     * 容量
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String capacity;
    /**
     * 名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    /**
     * 测量相
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String measureTag;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String devId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String parentDevId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String devName;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer supportNumber;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer actualNumber;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String sampleTime;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private BigDecimal value;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String monthTime;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<String> measureTags;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String station_name;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private BigDecimal month_dl;
}
