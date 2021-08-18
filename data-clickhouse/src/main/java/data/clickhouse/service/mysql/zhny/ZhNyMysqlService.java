package data.clickhouse.service.mysql.zhny;


import data.clickhouse.bean.ParameterBeanVo;
import data.clickhouse.dao.mysql.zhny.MysqlMapper;
import data.clickhouse.method.PublicMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component
@Transactional(rollbackFor = Exception.class)
public class ZhNyMysqlService {

    @Autowired
    MysqlMapper mysqlMapper;
    @Autowired
    PublicMethods publicMethods;

    /**
     * 电量指数的信息
     * @param parameterBean
     * @return
     */
    public Map<String, String> selectDwOffLine(ParameterBeanVo parameterBean){
        Map<String,String> resultMap=new HashMap<>();
        List<String> stationIds = new ArrayList<>(parameterBean.getStationMap().keySet());
        List<String> filterStationIds = parameterBean.isFilterStation()?
                mysqlMapper.filterAbnormalStation(stationIds):stationIds;
        if(filterStationIds.size()>0) {
            Map<String, BigDecimal> stringDoubleMap = mysqlMapper.selectDwOffLine(filterStationIds);
            stringDoubleMap.forEach((key, value) -> resultMap.put(key, publicMethods.doubleFormat(value)));
        }
        return resultMap;
    }
}
