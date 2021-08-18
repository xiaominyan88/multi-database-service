package data.clickhouse.aspect;


import data.clickhouse.bean.ParameterBeanVo;
import data.clickhouse.exception.ParameterException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterParameterAspect {
    @Around("@annotation(com.hyit.bigdata.clickhouse.annotation.FilterAbnormalParameters)&&args(parameterBeanVo)")
    public Object filterNull(ProceedingJoinPoint joinPoint, ParameterBeanVo parameterBeanVo)  {
        List<ParameterBeanVo> decide = Stream.of(parameterBeanVo).filter(ParameterBeanVo.stationMapIsNull).collect(Collectors.toList());
        if (decide.size() > 0) {
            System.out.println("null parameters");
            return null;
        }
        //过滤入参key异常大的入参
        List<ParameterBeanVo> stationKey = Stream.of(parameterBeanVo).filter(ParameterBeanVo.isStationMapIsBig).collect(Collectors.toList());
        if (stationKey.size() > 0){
            throw new ParameterException("abnormal parameters");
        }
        try {
            Object proceed = joinPoint.proceed();
            return  proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
