package com.yuliang.data.StudentGrade;

import java.util.Date;

public class StudentGrade {

    private String studentId;

    private String departmentId;

    private String courseNumber;

    private CourseDate courseDate;

    private int credits;

    private float score;

    public StudentGrade(String studentId, String departmentId, String courseNumber, CourseDate courseDate, int credits, float score) {
        this.studentId = studentId;
        this.departmentId = departmentId;
        this.courseNumber = courseNumber;
        this.courseDate = courseDate;
        this.credits = credits;
        this.score = score;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public CourseDate getCourseDate() {
        return courseDate;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getTotalScore() {
        return this.score * this.getCredits();
    }

    @Override
    public String toString() {
        return "StudentGrade{" +
                "studentId='" + studentId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                ", courseDate=" + courseDate +
                ", credits=" + credits +
                ", score=" + score +
                '}';
    }
}
