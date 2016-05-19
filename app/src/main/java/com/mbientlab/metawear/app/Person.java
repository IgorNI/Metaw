package com.mbientlab.metawear.app;

/**
 * Created by nilif on 2016/5/20.
 */
public class Person {
    private  float data1;
    private  float data2;
    private  float data3;
    private  float data4;
    private  int stepNum;
    private  int gesture;
    private  String time;
    public Person(float data1,float data2,float data3,float data4,int stepNum,int gesture,String time){
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.stepNum = stepNum;
        this.gesture = gesture;
        this.time = time;
    }

    public float getData1() {
        return data1;
    }

    public float getData2() {
        return data2;
    }

    public float getData3() {
        return data3;
    }

    public float getData4() {
        return data4;
    }

    public int getGesture() {
        return gesture;
    }

    public int getStepNum() {
        return stepNum;
    }

    public String getTime() {
        return time;
    }
}
