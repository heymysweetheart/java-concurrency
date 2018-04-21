package com.yuliang.data.StudentGrade;

public class CourseDate {

    private int month;

    private int year;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "CourseDate{" +
                "month=" + month +
                ", year=" + year +
                '}';
    }

    public CourseDate(int month, int year) {
        this.month = month;
        this.year = year;
    }
}
