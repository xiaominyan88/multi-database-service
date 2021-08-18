package data.clickhouse.dao.hive;


import data.clickhouse.bean.RequestParamVo;
import data.clickhouse.bean.ResponseResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HiveMapper {

    List<ResponseResultVo> selectCollectionValueFromTimeZoneGroupByTime(RequestParamVo requestParamVo);

    List<ResponseResultVo> selectSumValueForMinAvgMaxFromTimeZoneAndTJLTableGroupByTimeTag(RequestParamVo requestParamVo);

}
