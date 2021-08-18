package data.tree.service.plat;


import data.tree.bean.ParameterBean;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class InverterPointsService extends AbstractPlatService {

    @Override
    public Map<String, List<String>> platService(){

        ParameterBean parameterBean = ParameterThreadLocal.get();
        String stationId = parameterBean.getStationId();
        return getInverterPoints(stationId);
    }

}
