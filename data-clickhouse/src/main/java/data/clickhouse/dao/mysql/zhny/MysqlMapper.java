package data.clickhouse.dao.mysql.zhny;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface MysqlMapper {

    /**
     * 过滤掉不符合要求的站点id
     *
     * @param stationIds
     * @return
     */
    List<String> filterAbnormalStation(@Param("stationIds") List<String> stationIds);

    /**
     * 查询电务offline的信息
     *
     * @param stationIds
     * @return
     */
    Map<String, BigDecimal> selectDwOffLine(@Param("stationIds") List<String> stationIds);


}
