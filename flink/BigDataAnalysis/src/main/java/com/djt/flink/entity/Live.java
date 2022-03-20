package com.djt.flink.entity;

/**
 * @author bigdata
 * @create 2021-08-25-21:32
 */
public class Live {
    private String cityCode;
    private String provinceCode;
    private double v_score;
    private String v_level;
    private String time;
    private String v_type;

    @Override
    public String toString() {
        return "Live{" +
                "cityCode='" + cityCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", v_score=" + v_score +
                ", v_level='" + v_level + '\'' +
                ", time='" + time + '\'' +
                ", v_type='" + v_type + '\'' +
                '}';
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public double getV_score() {
        return v_score;
    }

    public void setV_score(double v_score) {
        this.v_score = v_score;
    }

    public String getV_level() {
        return v_level;
    }

    public void setV_level(String v_level) {
        this.v_level = v_level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }
}
