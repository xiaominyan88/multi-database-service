package data.clickhouse.bean;


import data.clickhouse.method.PublicMethods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
@Data
@ApiModel(value = "入参对象")
public class ParameterBeanVo {
    @ApiModelProperty(value = "站点测点集合",dataType = "Map<String, List<String>")
    private Map<String, Object> stationMap;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "时间点")
    private List<String> time;
    @ApiModelProperty(value = "测量相集合")
    private List<String> tagList;
    /**
     * 15min_tjl表示NEW_DW_15MIN_TJL
     * 15min_lj表示NEW_DW_15MIN_TJL_LJ
     * day_tjl表示NEW_DW_DAY_TJL
     * day_lj表示NEW_DW_DAY_TJL_LJ
     * month_tjl表示NEW_DW_MONTH_TJL
     * month_lj表示NEW_DW_MONTH_TJL_LJ
     * year_tjl表示NEW_DW_YEAR_TJL
     * year_lj表示
     */
    @ApiModelProperty(value = "表名")
    private String tableFlag;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    //用于判断入参stationMap的value是否是站点Id集合
    @ApiModelProperty(value = "用于判断入参stationMap的value是否是站点Id集合")
    private boolean measureType = false;
    /**
     * 是否过滤站点
     */
    @ApiModelProperty(value="是否过滤站点")
    private boolean filterStation = false;
    /**
     *通用判断
     */
    @ApiModelProperty(value = "完整率标签")
    private boolean judgeParameter= false;
    /**
     * 筛选数据
     */
    public static Predicate<ParameterBeanVo> timeMore3Month = x -> (PublicMethods.compareTime(x.getStartTime()) ||
            PublicMethods.compareTime(x.getTime())) && PublicMethods.getPhoenixOrHiveTableFlag(x.getTableFlag());
    /**
     * 筛选带有projectName
     */
    public static Predicate<ParameterBeanVo> existProjectName = x -> StringUtils.isNotEmpty(x.getProjectName());
    /**
     * 筛选光伏实时数据计算电量
     */
    public static Predicate<ParameterBeanVo> tjlCalculateLj = x -> x.tableFlag.toUpperCase().contains("15min_lj".toUpperCase())
            && x.tagList.get(0).equalsIgnoreCase("GEN.CurkWhRec")&& x.getProjectName().equalsIgnoreCase("guangfu");
    /**
     * 筛选是走15min实时表还是天月年表或其他（15min为分表）
     */
    public static Predicate<ParameterBeanVo> is15MinTjl= x->x.tableFlag.toUpperCase().contains("15min_tjl".toUpperCase());
    /**
     * 筛选stationMap为空的，或者站点id有值，value 为空
     */
    public static Predicate<ParameterBeanVo> stationMapIsNull= x->x.getStationMap().size()==0;
    /**
     * 筛选出stationMap的数据量
     */
    public static Predicate<ParameterBeanVo> isStationMapIsBig= x->x.getStationMap().keySet().size()>1100;
}
