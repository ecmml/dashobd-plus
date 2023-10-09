package com.ecm.dashobd_plus.models;

import java.io.Serializable;

public class Zone implements Serializable {

    private String name;

    private String pid;

    private String value;

    private String resultUnit;

    private  boolean isString = false;

    private float minimum = 0f;
    private float maximum = 100f;

    public String getPid() {
        return pid;
    }

    public boolean isString() {
        return isString;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public float getMaximum() {
        return maximum;
    }

    public float getMinimum() {
        return minimum;
    }

    public String getName() {
        return name;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public String getValue() {
        return value;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }

    public void setMinimum(float minimum) {
        this.minimum = minimum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }

    public void setString(boolean string) {
        isString = string;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
