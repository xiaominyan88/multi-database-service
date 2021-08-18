package data.clickhouse.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.text.DecimalFormat;


public class PublicMethodVo {
    /**
     * 站点id
     */
    private String stationId;
    /**
     *采集频率
     */
    private int acquiring;
    /**
     * 完整率数据
     */
    private Double rate;
    /**
     * 时间
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sampleTime;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public int getAcquiring() {
        return acquiring;
    }

    public void setAcquiring(int acquiring) {
        this.acquiring = acquiring;
    }

    public String getRate() {
        return doubleFormat(rate);
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }
    public  static String doubleFormat(Double value){
        if (value==null){
            return  null;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String  number=df.format(value);
        return number;
    }
}
