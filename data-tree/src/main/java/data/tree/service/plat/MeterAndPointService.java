package data.tree.service.plat;


import data.tree.bean.DevAndMeasurePoint;
import data.tree.bean.ParameterBean;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeterAndPointService extends AbstractPlatService {

    @Override
    public List<DevAndMeasurePoint> platService(){
        ParameterBean parameterBean = ParameterThreadLocal.get();
        String stationId = parameterBean.getStationId();
        return getMeterAndPoint(stationId);
    }

}
