package data.clickhouse.controller;


import data.clickhouse.annotation.FilterAbnormalParameters;
import data.clickhouse.bean.ParameterBeanVo;
import data.clickhouse.service.clickhouse.ClickHouseService;
import data.clickhouse.service.hive.HiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/hData")
@Api(value = "数据获取接口",description = "ClickHouse和Hive数据源获取接口")
public class HiveClickHouseDataController {

    @Autowired
    ClickHouseService clickHouseService;
    @Autowired
    HiveService hiveService;

    @RequestMapping(value = "/collectionSumValueFromTimeZoneGroupByMinuteTime", method = RequestMethod.POST)
    @ResponseBody
    @FilterAbnormalParameters
    @ApiOperation(value = "获得所选时间范围内各个测点按分钟时间的加和",httpMethod = "POST")
    public Map<String, Object> findCollectionSumValueFromTimeZoneGroupByMinuteTime(@RequestBody ParameterBeanVo parameterBeanVo) {
        List<ParameterBeanVo> collect = Stream.of(parameterBeanVo).filter(ParameterBeanVo.timeMore3Month).collect(Collectors.toList());
        Map<String, Object> stringObjectMap = collect.size() == 0 ? clickHouseService.selectCollectionValueFromTimeZoneGroupByMinuteTime(parameterBeanVo) : hiveService.selectCollectionValueFromTimeZoneGroupByMinuteTime(parameterBeanVo);
        return stringObjectMap;
    }


    @RequestMapping(value = "/collectionSumValueForMinAvgMaxFromTimeZoneAndTJLTableGroupByTimeTag", method = RequestMethod.POST)
    @ResponseBody
    @FilterAbnormalParameters
    public Map<String, Object> findCollectionSumValueForMinAvgMaxFromTimeZoneAndTJLTableGroupByTag(@RequestBody ParameterBeanVo parameterBeanVo) {
        Map<String, Object> resultMap = new HashMap<>(171);
        try {
            List<ParameterBeanVo> collect = Stream.of(parameterBeanVo).filter(ParameterBeanVo.timeMore3Month).collect(Collectors.toList());
            resultMap = collect.size() == 0 ? clickHouseService.selectCollectionSumValueForMinAvgMaxFromTimeZoneAndTJLTableGroupByTimeTag(parameterBeanVo) : hiveService.selectCollectionSumValueForMinAvgMaxFromTimeZoneAndTJLTableGroupByTimeTag(parameterBeanVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }


}
