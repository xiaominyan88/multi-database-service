package data.tree.service.plat;


import data.tree.bean.ParameterBean;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MeasureIdAndNameService extends AbstractPlatService {

    @Override
    public Map<String,String> platService(){
        ParameterBean parameterBean = ParameterThreadLocal.get();
        String pointIds = parameterBean.getPointIds();
        return findMeasureIdAndNameMap(pointIds);
    }

}
