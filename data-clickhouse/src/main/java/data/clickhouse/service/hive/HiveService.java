package data.clickhouse.service.hive;


import data.clickhouse.bean.ParameterBeanVo;
import data.clickhouse.bean.RequestParamVo;
import data.clickhouse.bean.ResponseDataVo;
import data.clickhouse.bean.ResponseResultVo;
import data.clickhouse.dao.hive.HiveMapper;
import data.clickhouse.method.PublicMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Component
@Transactional
public class HiveService {

    @Autowired
    private HiveMapper hiveMapper;
    @Autowired
    private PublicMethods publicMethods;


    /**
     * 按照输入的Tag进行分组，并获得各个Tag对应的测点在所选时间段内各个时间点的加和值(时间类型是分钟级)
     * @param parameterBeanVo
     * @return
     */
    public Map<String, Object> selectCollectionValueFromTimeZoneGroupByMinuteTime(ParameterBeanVo parameterBeanVo) {
        String dateType = "0,16";
        Map<String, Object> resultMap = new HashMap<>();
        String tableFlag = parameterBeanVo.getTableFlag();
        Map<String, Object> stationMap = parameterBeanVo.getStationMap();
        try {
            for (String key : stationMap.keySet()) {
                RequestParamVo requestParamVo = publicMethods.packageRequestParamVo(parameterBeanVo);
                List<String> measurePointIds = (List<String>) stationMap.get(key);
                requestParamVo.setMeasurePointIds(measurePointIds);
                requestParamVo.setDateType(dateType);
                requestParamVo.setTableName(publicMethods.getDataTableFromMeasureId(tableFlag,measurePointIds));
                List<ResponseResultVo> responseResultVos = hiveMapper.selectCollectionValueFromTimeZoneGroupByTime(requestParamVo);
                Map<String, List<ResponseDataVo>> collect = responseResultVos.stream().collect(Collectors.toMap(ResponseResultVo::getMeasureTag, ResponseResultVo::getResponseDataVo));
                resultMap.put(key,collect);
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
        Map<String, Object> resultMap = new HashMap<>();
        String tableFlag = parameterBeanVo.getTableFlag();
        Map<String, Object> stationMap = parameterBeanVo.getStationMap();
        try {
            for (String key : stationMap.keySet()) {
                RequestParamVo requestParamVo = publicMethods.packageRequestParamVo(parameterBeanVo);
                List<String> measurePointIds = (List<String>) stationMap.get(key);
                requestParamVo.setMeasurePointIds(measurePointIds);
                requestParamVo.setTableName(publicMethods.getDataTableFromMeasureId(tableFlag,measurePointIds));
                List<ResponseResultVo> responseResultVos = hiveMapper.selectSumValueForMinAvgMaxFromTimeZoneAndTJLTableGroupByTimeTag(requestParamVo);
                Map<String, List<ResponseDataVo>> collect = responseResultVos.stream().collect(Collectors.toMap(ResponseResultVo::getMeasureTag, ResponseResultVo::getResponseDataVo));
                resultMap.put(key,collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

}
