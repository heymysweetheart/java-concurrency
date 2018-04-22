package com.yuliang.data;

public class Result {

    private long startTime;
    private long endTime;
    private Object data;

    public Result(long startTime, long endTime, Object data) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.data = data;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
