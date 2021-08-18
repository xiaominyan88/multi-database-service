package data.tree.threadlocal;


import data.tree.bean.ParameterBean;

public class ParameterThreadLocal {
    private ParameterThreadLocal(){
    }
    private static final ThreadLocal<ParameterBean> LOCAL =new ThreadLocal<ParameterBean>();
    public static void set(ParameterBean parameterBean){
        LOCAL.set(parameterBean);
    }
    public static ParameterBean get(){
        return LOCAL.get();
    }
}
