package com.yuliang.data.StudentGrade;

import java.util.List;

public class StudentGPA {

    private String studentId;

    private List<StudentGrade> grades;

    public StudentGPA(String studentId, List<StudentGrade> grades) {
        this.studentId = studentId;
        this.grades = grades;
    }

    public double getGpa() {
        double scoreSum = grades.stream()
                .mapToDouble(grade -> grade.getCredits() * grade.getScore())
                .sum();

        int credits = grades.stream()
                .mapToInt(grade -> grade.getCredits())
                .sum();

        return scoreSum / credits;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<StudentGrade> getGrades() {
        return grades;
    }

    public void setGrades(List<StudentGrade> grades) {
        this.grades = grades;
    }
}
