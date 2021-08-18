package data.tree.service.plat;


import data.tree.bean.DevAndMeasurePoint;
import data.tree.dao.mysql.eidp.MysqlEidpMapper;
import data.tree.dao.mysql.zhny.MysqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component
public abstract class AbstractPlatService implements PlatServiceInterface {

    @Autowired
    MysqlEidpMapper mysqlEidpMapper;

    @Autowired
    MysqlMapper mysqlMapper;

    public Map<String,String> findCodeMap(String codeType){
        return mysqlEidpMapper.findCodeMap(codeType);
    }

    @Override
    public Map<String,String> getSiteInformation(String id){
        Map<String,String> map = mysqlEidpMapper.findSiteInfo(id);
        return mysqlEidpMapper.findSiteAllInfomation(map);
    }

    @Override
    public Map<String,String> findMeasureIdAndNameMap(String pointIds){
        String[] array = pointIds.split(",");
        return mysqlEidpMapper.findMeasureIdAndNameMap(Arrays.asList(array));
    }

    @Override
    public List<DevAndMeasurePoint> getMeterAndPoint(String stationId){
        return mysqlEidpMapper.getMeterAndPoint(stationId);
    }

    @Override
    public Map<String,List<String>> getInverterPoints(String stationId){
        Map<String,List<String>> map = new HashMap<>();
        List<String> pointIds = mysqlEidpMapper.getInverterPoints(stationId);
        map.put(stationId,pointIds);
        return map;
    }

    @Override
    public String findEnergyType(String stationId,String type){
        return mysqlEidpMapper.findEnergyType(stationId, type);
    }

    public abstract <T> T platService();

}
