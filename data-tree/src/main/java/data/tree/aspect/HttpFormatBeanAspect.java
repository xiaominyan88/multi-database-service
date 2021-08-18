package data.tree.aspect;


import data.tree.bean.ParameterBean;
import data.tree.threadlocal.ParameterThreadLocal;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Aspect
@Component
public class HttpFormatBeanAspect {

    @Pointcut("@annotation(data.tree.annotation.HttpFormatBean)")
    public void httpPoint(){}

    @Before(value = "httpPoint()")
    public void doBefore(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        ParameterBean parameterBean=new ParameterBean();
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            buildParameterBean(name,parameterBean,request);
        }
        ParameterThreadLocal.set(parameterBean);
    }

    @AfterReturning(returning = "response" , pointcut = "httpPoint()")
    public void doAfterReturning(Object response){
        ParameterThreadLocal.set(null);
    }

    private void buildParameterBean(String name,ParameterBean parameterBean,HttpServletRequest request){
        switch (name){
            case "id":
                parameterBean.setId(request.getParameter(name));
                break;
            case "type":
                parameterBean.setType(request.getParameter(name));
                break;
            case "stationId":
                parameterBean.setStationId(request.getParameter(name));
                break;
            case "tagType":
                parameterBean.setTagType(request.getParameter(name));
                break;
            case "deviceType":
                parameterBean.setDeviceType(request.getParameter(name));
                break;
            case "stationIdTypes":
                parameterBean.setStationIdTypes(request.getParameter(name));
                break;
            case "devId":
                parameterBean.setDevId(request.getParameter(name));
            case "devClassName":
                parameterBean.setDevClassName(request.getParameter(name));
            case "className":
                parameterBean.setClassName(request.getParameter(name));
            case "points":
                parameterBean.setPointIds(request.getParameter(name));
            default:
                break;
        }
    }

}
