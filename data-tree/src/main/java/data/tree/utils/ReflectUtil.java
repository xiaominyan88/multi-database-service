package data.tree.utils;

import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

public class ReflectUtil {

    public static MetaObject getRealTarget(Invocation invocation) {
        MetaObject metaStatementHandler = SystemMetaObject.forObject(invocation.getTarget());


        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }


        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }


        return metaStatementHandler;

    }

}
