package data.clickhouse.method;



import data.clickhouse.bean.ParameterBeanVo;
import data.clickhouse.bean.RequestParamVo;
import data.clickhouse.dao.mysql.zhny.MysqlMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
public class PublicMethods {
    @Autowired
    MysqlMapper mysqlMapper;

    public String getTableFromFlag(String flag) {
        switch (flag) {
            case "1min_tjl":
                return "ycl_tjl_1min_test_all";
            case "15min_tjl":
                return "ycl_tjl_15min_test_all";
            case "15min_lj":
                return "ycl_lj_15min_test_all";
            case "1hour_lj":
                return "ycl_lj_1hour_test_all";
            case "day_tjl":
                return "ycl_tjl_day_test_all";
            case "day_lj":
                return "ycl_lj_day_test_all";
            case "month_tjl":
                return "ycl_tjl_month_test_all";
            case "month_lj":
                return "ycl_lj_month_test_all";
            case "year_tjl":
                return "ycl_tjl_year_test_all";
            default:
                return "ycl_lj_year_test_all";
        }
    }

    public String getHiveTableFromFlag(String flag) {
        switch (flag) {
            case "1min_tjl":
                return "app_zhny_ycl_tjl_15min";
            case "15min_tjl":
                return "app_zhny_ycl_tjl_15min";
            case "15min_lj":
                return "app_zhny_ycl_lj_15min";
            case "1hour_tjl":
                return "app_zhny_ycl_tjl_1hour";
            case "1hour_lj":
                return "app_zhny_ycl_lj_1hour";
            case "1day_tjl":
                return "app_zhny_ycl_tjl_1day";
            case "1day_lj":
                return "app_zhny_ycl_lj_1day";
            default:
                return "app_zhny_ycl_tjl_15min";
        }
    }

    public String getTableFromFlagMeiShan(String flag) {
        switch (flag) {
            case "1min_tjl":
                return "ycl_tjl_1min_test_all";
            case "15min_tjl":
                return "ycl_tjl_15min_test_all";
            case "15min_lj":
                return "ycl_lj_15min_test_all";
            case "1hour_lj":
                return "ycl_lj_1hour_test_all";
            case "day_tjl":
                return "ycl_tjl_day_test_all";
            case "day_lj":
                return "ycl_lj_day_test_all";
            case "month_tjl":
                return "ycl_tjl_month_test_all";
            case "month_lj":
                return "ycl_lj_month_test_all";
            case "year_tjl":
                return "ycl_tjl_year_test_all";
            default:
                return "ycl_lj_year_test_all";
        }
    }


    /**
     * 将传入的List<String>转成String
     *
     * @param : List<String> list 包含站点Id的集合
     * @return : String 转化的特定格式的字符串
     */
    public static String listToString(List<String> list) {
        StringBuffer sb = new StringBuffer();
        if (list.size() > 0) {
            for (String s : list) {
                if (StringUtils.isNotEmpty(s)) {
                    sb.append("'" + s + "',");
                } else {
                    continue;
                }
            }
            return sb.toString().substring(0, sb.length() - 1);
        } else {
            System.out.println("传入的list集合为空！");
            return null;
        }
    }

    /**
     * 根据测点id找到分表规则
     *
     * @param tableFlag
     * @param measurepointIds
     * @return
     */
    public String getDataTableFromMeasureId(String tableFlag, List<String> measurepointIds) {
        //根据调用方法的不同，调用不同分表
        StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[1];
        String className = stackTraceElement.getClassName();
        String table = "";
        if (className.toUpperCase().contains("clickhouse".toUpperCase())) {
            table = getTableFromFlag(tableFlag);
        } else if (className.toUpperCase().contains("hive".toUpperCase())) {
            table = "hy_hdp_db." + getHiveTableFromFlag(tableFlag);
        } else {
            table = "";
            System.out.println("当前调用方法不在本方法列表内，请添加" + className);
        }
        return table;
    }

    public String getDataTableFromMeasureId(String tableFlag, List<String> measurepointIds, String projectName) {
        //根据调用方法的不同，调用不同分表
        StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[1];
        String className = stackTraceElement.getClassName();
        String table = "";
        if (className.toUpperCase().contains("clickhouse".toUpperCase())) {
            if (projectName.equalsIgnoreCase("meishan")) {
                table = getTableFromFlagMeiShan(tableFlag);
            } else {
                table = getTableFromFlag(tableFlag);
            }
        } else if (className.toUpperCase().contains("hive".toUpperCase())) {
            table = "hy_hdp_db." + getHiveTableFromFlag(tableFlag);
        } else {
            table = "当前调用方法不在本方法列表内，请添加";
            System.out.println("当前调用方法不在本方法列表内，请添加" + className);
        }
        return table;
    }

    public RequestParamVo packageRequestParamVo(ParameterBeanVo parameterBeanVo) {
        RequestParamVo requestParamVo = new RequestParamVo();
        requestParamVo.setStartTime(parameterBeanVo.getStartTime());
        requestParamVo.setEndTime(parameterBeanVo.getEndTime());
        requestParamVo.setTimes(parameterBeanVo.getTime());
        requestParamVo.setFilterStation(parameterBeanVo.isFilterStation());
        requestParamVo.setMeasureTags(parameterBeanVo.getTagList());
        return requestParamVo;
    }

    public static void main(String[] args) {
        PublicMethods publicMethods = new PublicMethods();
        publicMethods.getDataTableFromMeasureId("1min", Arrays.asList("1111", "1111"));

    }

    //时间比较
    public static boolean compareTime(String time) {
        Boolean flag = false;
        try {
            if (StringUtils.isEmpty(time) || "".equalsIgnoreCase(time)) {
                return flag;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowTime);
            calendar.add(Calendar.MONTH, -3);
            Date baseTime = calendar.getTime();
            Date compareTime = format.parse(time);
            //前者大于后者 返回大于0的数字反之小于0的数字，等于返回0
            switch (compareTime.compareTo(baseTime)) {
                case 1:
                    flag = false;
                    break;
                case -1:
                    flag = true;
                    break;
                case 0:
                    flag = false;
                    break;
                default:
                    flag = false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    //选择phoenix还是hive
    public static boolean getPhoenixOrHiveTableFlag(String tableFlag) {
        switch (tableFlag) {
            case "15min_tjl":
                return true;
            case "15min_lj":
                return true;
            case "day_tjl":
                return false;
            case "day_lj":
                return false;
            case "month_tjl":
                return false;
            case "year_tjl":
                return false;
            default:
                return false;
        }
    }

    //时间比较
    public static boolean compareTime(List<String> time) {
        Boolean flag = false;
        try {
            if (time == null || time.size() == 0) {
                return flag;
            }
            for (int i = 0; i < time.size(); i++) {
                flag = compareTime(time.get(i));
                if (flag) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * double转String
     *
     * @return
     */
    public static Object doubleToString(Object value) {
        if (value == null) {
            return value;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String number = df.format(value);
        return number;
    }



    /**
     * 过滤不符合要求的站点id
     *
     * @param parameterBeanVo
     * @return
     */
    public ParameterBeanVo filterAbnormalStation(ParameterBeanVo parameterBeanVo) {
        for (String s : parameterBeanVo.getStationMap().keySet()) {
            parameterBeanVo.getStationMap().put(s, mysqlMapper.filterAbnormalStation((List<String>) parameterBeanVo.getStationMap().get(s)));
        }
        return parameterBeanVo;
    }



    public String doubleFormat(Object value) {
        if (value == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String number = df.format(value);
        return number;
    }
}
