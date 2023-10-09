package com.ecm.dashobd;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * {@link ObdData} is a class used for transferring obd data between services
 */
public class ObdData implements Parcelable {

    private String name;

    private String pid;

    private String value;

    private String resultUnit;

    private  boolean isString = false;

    private float minimum = 0f;
    private float maximum = 100f;

    protected ObdData(Parcel in) {
        name = in.readString();
        value = in.readString();
        pid = in.readString();
        resultUnit = in.readString();
        isString = in.readByte() != 0;
        minimum = in.readFloat();
        maximum = in.readFloat();
    }

    public static final Creator<ObdData> CREATOR = new Creator<ObdData>() {
        @Override
        public ObdData createFromParcel(Parcel in) {
            return new ObdData(in);
        }

        @Override
        public ObdData[] newArray(int size) {
            return new ObdData[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
        dest.writeString(pid);
        dest.writeString(resultUnit);
        dest.writeByte((byte) (isString ? 1 : 0 ));
        dest.writeFloat(maximum);
        dest.writeFloat(minimum);


    }
}
