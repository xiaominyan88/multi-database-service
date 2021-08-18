package data.clickhouse.bean.pojo;



import data.clickhouse.bean.ResponseResultVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicParameterBeanPojo {
    /**
     * 是否要最大值
     */
    private Boolean isMax;
    /**
     * 是否要最小值
     */
    private Boolean isMin;
    /**
     * 是否要平均值值
     */
    private Boolean isAvg;
    /**
     * 只要当前值
     */
    private Boolean isValue;
    /**
     * 站点集合
     */
    private Map<String, Object> stationMap;
    /**
     * 返回值
     */
    private List<ResponseResultVo> responseResultVo;

}
