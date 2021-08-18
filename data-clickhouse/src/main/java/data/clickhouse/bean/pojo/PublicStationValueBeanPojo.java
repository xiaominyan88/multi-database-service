package data.clickhouse.bean.pojo;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;


public class PublicStationValueBeanPojo {
    private String stationId;
    private Map<String, Double> measurePointIdValue;
    private Double maxValue;
    private Double minValue;
    private Double avgValue;
    private Double sumValue;
    private String time;
    private String measureTag;

    public Double getMaxValue() {
        return maxValue;
    }
    public Double getMinValue() {
        return minValue;
    }
    public Double getAvgValue() {
        return avgValue;
    }
    public Double getSumValue() {
        return sumValue;
    }
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Map<String, Double> getMeasurePointId() {
        return measurePointIdValue;
    }

    public void setMeasurePointId(Map<String, Double> measurePointIdValue) {
        if(measurePointIdValue!=null) {
            Double i=0.00;
            Collection<Double> values = measurePointIdValue.values();
            Double max = Collections.max(values);
            this.maxValue = max;
            Double min = Collections.min(values);
            this.minValue = min;
            Iterator<Double> iterator = values.iterator();
            while(iterator.hasNext()){
                i+=iterator.next();
            }
            this.sumValue=i;
            this.avgValue=this.sumValue/measurePointIdValue.size();
        }
        this.measurePointIdValue = measurePointIdValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMeasureTag() {
        return measureTag;
    }

    public void setMeasureTag(String measureTag) {
        this.measureTag = measureTag;
    }

}
