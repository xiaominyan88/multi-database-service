package data.tree.intercepts;


import data.tree.annotation.MapF2F;
import data.tree.utils.ReflectUtil;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Configuration
@Component
@Intercepts(@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {Statement.class}))
public class MapF2FInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MetaObject metaStatementHandler = ReflectUtil.getRealTarget(invocation);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("mappedStatement");
        // 当前类
        String className = StringUtils.substringBeforeLast(mappedStatement.getId(), ".");
        // 当前方法
        String currentMethodName = StringUtils.substringAfterLast(mappedStatement.getId(), ".");
        // 获取当前Method
        Method currentMethod = findMethod(className, currentMethodName);


        if (currentMethod == null || currentMethod.getAnnotation(MapF2F.class) == null) {
            // 如果当前Method没有注解MapF2F
            return invocation.proceed();
        }


        // 如果有MapF2F注解，则这里对结果进行拦截并转换
        MapF2F mapF2FAnnotation = currentMethod.getAnnotation(MapF2F.class);
        Statement statement = (Statement) invocation.getArgs()[0];
        // 获取返回Map里key-value的类型
        Pair<Class<?>, Class<?>> kvTypePair = getKVTypeOfReturnMap(currentMethod);
        // 获取各种TypeHander的注册器
        TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        return result2Map(statement, typeHandlerRegistry, kvTypePair, mapF2FAnnotation);


    }


    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }


    @Override
    public void setProperties(Properties properties) {


    }


    /**
     * 找到与指定函数名匹配的Method。
     *
     * @param className
     * @param targetMethodName
     * @return
     * @throws Throwable
     */
    private Method findMethod(String className, String targetMethodName) throws Throwable {
        // 该类所有声明的方法
        Method[] methods = Class.forName(className).getDeclaredMethods();
        if (methods == null) {
            return null;
        }


        for (Method method : methods) {
            if (StringUtils.equals(method.getName(), targetMethodName)) {
                return method;
            }
        }


        return null;
    }


    /**
     * 获取函数返回Map中key-value的类型
     *
     * @param mapF2FMethod
     * @return left为key的类型，right为value的类型
     */
    private Pair<Class<?>, Class<?>> getKVTypeOfReturnMap(Method mapF2FMethod) {
        Type returnType = mapF2FMethod.getGenericReturnType();


        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            if (!Map.class.equals(parameterizedType.getRawType())) {
                throw new RuntimeException(
                        "[ERROR-MapF2F-return-map-type]使用MapF2F,返回类型必须是java.util.Map类型！！！method=" + mapF2FMethod);
            }


            return new Pair<>((Class<?>) parameterizedType.getActualTypeArguments()[0],
                    (Class<?>) parameterizedType.getActualTypeArguments()[1]);
        }


        return new Pair<>(null, null);
    }


    /**
     * 将查询结果映射成Map，其中第一个字段作为key，第二个字段作为value.
     *
     * @param statement
     * @param typeHandlerRegistry MyBatis里typeHandler的注册器，方便转换成用户指定的结果类型
     * @param kvTypePair          函数指定返回Map key-value的类型
     * @param mapF2FAnnotation
     * @return
     * @throws Throwable
     */
    private Object result2Map(Statement statement, TypeHandlerRegistry typeHandlerRegistry,
                              Pair<Class<?>, Class<?>> kvTypePair, MapF2F mapF2FAnnotation) throws Throwable {
        ResultSet resultSet = statement.getResultSet();
        List<Object> res = new ArrayList();
        Map<Object, Object> map = new HashMap();


        while (resultSet.next()) {
            Object key = this.getObject(resultSet, 1, typeHandlerRegistry, kvTypePair.getKey());
            Object value = this.getObject(resultSet, 2, typeHandlerRegistry, kvTypePair.getValue());


            if (map.containsKey(key)) {// 该key已存在
                if (!mapF2FAnnotation.isAllowKeyRepeat()) {
                    // 判断是否允许key重复
                    throw new DuplicateKeyException("MapF2F duplicated key!key=" + key);
                }


                Object preValue = map.get(key);
                if (!mapF2FAnnotation.isAllowValueDifferentWithSameKey() && !Objects.equals(value, preValue)) {
                    // 判断是否允许value不同
                    throw new DuplicateKeyException("MapF2F different value with same key!key=" + key + ",value1="
                            + preValue + ",value2=" + value);
                }
            }
            // 第一列作为key,第二列作为value。
            map.put(key, value);
        }


        res.add(map);
        return res;
    }


    /**
     * 结果类型转换。
     * <p>
     * 这里借用注册在MyBatis的typeHander（包括自定义的），方便进行类型转换。
     *
     * @param resultSet
     * @param columnIndex         字段下标，从1开始
     * @param typeHandlerRegistry MyBatis里typeHandler的注册器，方便转换成用户指定的结果类型
     * @param javaType            要转换的Java类型
     * @return
     * @throws SQLException
     */
    private Object getObject(ResultSet resultSet, int columnIndex, TypeHandlerRegistry typeHandlerRegistry,
                             Class<?> javaType) throws SQLException {
        final TypeHandler<?> typeHandler = typeHandlerRegistry.hasTypeHandler(javaType)
                ? typeHandlerRegistry.getTypeHandler(javaType) : typeHandlerRegistry.getUnknownTypeHandler();


        return typeHandler.getResult(resultSet, columnIndex);


    }


}
