package data.clickhouse.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.text.DecimalFormat;


public class ResponseDataVo {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double sumValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double avgValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double maxValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double minValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double numberValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String measurePointId;

    public String getSumValue() {
        return doubleFormat(sumValue);
    }

    public void setSumValue(Double sumValue) {
        this.sumValue = sumValue;
    }

    public String getAvgValue() {
        return doubleFormat(avgValue);
    }

    public void setAvgValue(Double avgValue) {
        this.avgValue = avgValue;
    }

    public String getMaxValue() {
        return doubleFormat(maxValue);
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return doubleFormat(minValue);
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public String getNumberValue() {
        return doubleFormat(numberValue);
    }

    public void setNumberValue(Double numberValue) {
        this.numberValue = numberValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeasurePointId() {
        return measurePointId;
    }

    public void setMeasurePointId(String measurePointId) {
        this.measurePointId = measurePointId;
    }
    @Override
    public String toString() {
        return "{" +
                "  sumValue='" + sumValue + '\'' +
                ", avgValue='" + avgValue + '\'' +
                ", maxValue='" + maxValue + '\'' +
                ", minValue='" + minValue + '\'' +
                ", numberValue='" + numberValue + '\'' +
                ", date='" + date + '\'' +
                '}';
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
