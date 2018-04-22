package com.yuliang.util;

import com.yuliang.data.StudentGrade.CourseDate;
import com.yuliang.data.StudentGrade.StudentGrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataUtil {

    public static Course[] courses = {
            new Course("CS", "C001", new CourseDate(4, 2018), 4),
            new Course("CS", "C002", new CourseDate(3, 2018), 2),
            new Course("CS", "C003", new CourseDate(3, 2018), 2),
            new Course("CS", "C004", new CourseDate(3, 2017), 4),
            new Course("Art", "C005", new CourseDate(3, 2017), 4),
            new Course("Art", "C006", new CourseDate(3, 2018), 4),
            new Course("Art", "C007", new CourseDate(3, 2017), 4),
            new Course("Math", "C008", new CourseDate(3, 2016), 4),
            new Course("Math", "C009", new CourseDate(3, 2017), 4),
            new Course("Math", "C010", new CourseDate(3, 2016), 4)
    };

    public static List<StudentGrade> init() {

        ArrayList<StudentGrade> studentGrades = new ArrayList<>();

        Random random = new Random();
        int studentId = 0;
        Course course;

        //Create 2500 students
        for(int i=0;i<550000;i++) {
            studentId++;
            //Each student takes four courses
            for(int j=0; j<4;j++) {
                course = getCourse(new Random().nextInt(10));
                String id = String.valueOf("Student" + studentId);
                float score = random.nextFloat() * 100;
                StudentGrade studentGrade = new StudentGrade(id, course.getDepartmentId(), course.getCourseNumber(), course.getCourseDate(), course.getCredits(), score);
                studentGrades.add(studentGrade);
            }
        }

        return studentGrades;
    }

    private static Course getCourse(int i) {
        return courses[i];
    }

    private static class Course {

        public CourseDate getCourseDate() {
            return courseDate;
        }

        public void setCourseDate(CourseDate courseDate) {
            this.courseDate = courseDate;
        }

        private String departmentId;

        private String courseNumber;

        private CourseDate courseDate;

        private int credits;

        public Course(String departmentId, String courseNumber, CourseDate courseDate, int credits) {
            this.departmentId = departmentId;
            this.courseNumber = courseNumber;
            this.courseDate = courseDate;
            this.credits = credits;
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

        public int getCredits() {
            return credits;
        }

        public void setCredits(int credits) {
            this.credits = credits;
        }

        public String getDepartmentId() {
            return departmentId;
        }
    }
}
