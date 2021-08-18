package data.clickhouse.service.clickhouse;


import data.clickhouse.bean.ParameterBeanVo;
import data.clickhouse.bean.RequestParamVo;
import data.clickhouse.bean.ResponseDataVo;
import data.clickhouse.bean.ResponseResultVo;
import data.clickhouse.dao.clickhouse.ClickHouseMapper;
import data.clickhouse.method.PublicMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Component
@Transactional
public class ClickHouseService {

    @Autowired
    private ClickHouseMapper clickHouseMapper;
    @Autowired
    private PublicMethods publicMethods;


    /**
     * 按照输入的Tag进行分组，并获得各个Tag对应的测点在所选时间段内各个时间点的加和值(时间类型是分钟级)
     *
     * @param parameterBeanVo
     * @return
     */
    public Map<String, Object> selectCollectionValueFromTimeZoneGroupByMinuteTime(ParameterBeanVo parameterBeanVo) {
        //是否过滤站点
        if (parameterBeanVo.isFilterStation()) {
            parameterBeanVo = publicMethods.filterAbnormalStation(parameterBeanVo);
        }
        String dateType = "1,16";
        Map<String, Object> resultMap = new HashMap<>();
        String tableFlag = parameterBeanVo.getTableFlag();
        Map<String, Object> stationMap = parameterBeanVo.getStationMap();
        try {
            for (String key : stationMap.keySet()) {
                RequestParamVo requestParamVo = publicMethods.packageRequestParamVo(parameterBeanVo);
                List<String> measurePointIds = (List<String>) stationMap.get(key);
                if (measurePointIds.size() == 0) {
                    continue;
                }
                requestParamVo.setMeasurePointIds(measurePointIds);
                requestParamVo.setDateType(dateType);
                requestParamVo.setTableName(Stream.of(parameterBeanVo).filter(ParameterBeanVo.existProjectName).collect(Collectors.toList()).size() == 0 ? publicMethods.getDataTableFromMeasureId(tableFlag, measurePointIds) : publicMethods.getDataTableFromMeasureId(tableFlag, measurePointIds, parameterBeanVo.getProjectName()));
                //过滤掉不符合要求的请求，例如测点id不在测点表中的错误测点
                if(requestParamVo.getTableName().equalsIgnoreCase("")||requestParamVo.getTableName()==null){
                    return resultMap;
                }
                List<ResponseResultVo> responseResultVos = clickHouseMapper.selectCollectionValueFromTimeZoneGroupByTime(requestParamVo);
                Map<String, List<ResponseDataVo>> collect = responseResultVos.stream().collect(Collectors.toMap(ResponseResultVo::getMeasureTag, ResponseResultVo::getResponseDataVo));
                if (collect.size() > 0) {
                    resultMap.put(key, collect);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }



    /**
     * 当入参中tableFlag不为15min_tjl、15min_lj、1h_lj的时候，返回一段时间中各个时间点的最大值和最小值
     *
     * @param parameterBeanVo
     * @return
     */
    public Map<String, Object> selectCollectionSumValueForMinAvgMaxFromTimeZoneAndTJLTableGroupByTimeTag(ParameterBeanVo parameterBeanVo) {
        //是否过滤站点
        if (parameterBeanVo.isFilterStation()) {
            parameterBeanVo = publicMethods.filterAbnormalStation(parameterBeanVo);
        }
        Map<String, Object> resultMap = new HashMap<>();
        String tableFlag = parameterBeanVo.getTableFlag();
        Map<String, Object> stationMap = parameterBeanVo.getStationMap();
        try {
            for (String key : stationMap.keySet()) {
                RequestParamVo requestParamVo = publicMethods.packageRequestParamVo(parameterBeanVo);
                List<String> measurePointIds = (List<String>) stationMap.get(key);
                if (measurePointIds.size() == 0) {
                    continue;
                }
                requestParamVo.setMeasurePointIds(measurePointIds);
                requestParamVo.setTableName(Stream.of(parameterBeanVo).filter(ParameterBeanVo.existProjectName).collect(Collectors.toList()).size() == 0 ? publicMethods.getDataTableFromMeasureId(tableFlag, measurePointIds) : publicMethods.getDataTableFromMeasureId(tableFlag, measurePointIds, parameterBeanVo.getProjectName()));
                //过滤掉不符合要求的请求，例如测点id不在测点表中的错误测点
                if(requestParamVo.getTableName().equalsIgnoreCase("")||requestParamVo.getTableName()==null){
                    return resultMap;
                }
                List<ResponseResultVo> responseResultVos = clickHouseMapper.selectSumValueForMinAvgMaxFromTimeZoneAndTJLTableGroupByTimeTag(requestParamVo);
                Map<String, List<ResponseDataVo>> collect = responseResultVos.stream().collect(Collectors.toMap(ResponseResultVo::getMeasureTag, ResponseResultVo::getResponseDataVo));
                if (collect.size() > 0) {
                    resultMap.put(key, collect);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

}
