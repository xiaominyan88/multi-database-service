package data.tree.service.tree;


import data.tree.bean.ParameterBean;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LdLineDataService extends AbstractTreeService {

    @Override
    public List<String> treeService(){
        ParameterBean parameterBean = ParameterThreadLocal.get();
        String devId = parameterBean.getDevId();
        String className = parameterBean.getClassName();
        return findMeasureIdFromDyLine(devId,className.toUpperCase());
    }

}
