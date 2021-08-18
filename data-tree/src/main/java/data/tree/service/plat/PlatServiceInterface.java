package data.tree.service.plat;


import data.tree.bean.DevAndMeasurePoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface PlatServiceInterface {

    public Map<String,String> getSiteInformation(String id);

    public Map<String,String> findMeasureIdAndNameMap(String pointIds);

    public List<DevAndMeasurePoint> getMeterAndPoint(String stationId);

    public Map<String,List<String>> getInverterPoints(String stationId);

    public String findEnergyType(String stationId, String type);

}
