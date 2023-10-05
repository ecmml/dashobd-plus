package com.ecm.dashobd_plus;


/**
 * {@link ObdData} is a class used for transferring obd data between services
 */
public final class ObdData {

    private String name;

    private String value;

    private String resultUnit;

    private  boolean isString = false;

    private float minimum = 0f;
    private float maximum = 100f;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }


    public boolean isString() {
        return isString;
    }

    public void setString(boolean string) {
        isString = string;
    }

    public float getMaximum() {
        return maximum;
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }

    public void setMinimum(float minimum) {
        this.minimum = minimum;
    }


}
