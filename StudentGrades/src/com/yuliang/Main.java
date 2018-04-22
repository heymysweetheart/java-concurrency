package com.yuliang;

import com.yuliang.data.StudentGrade.StudentGPA;
import com.yuliang.data.StudentGrade.StudentGrade;
import com.yuliang.util.DataUtil;

import java.util.*;
import java.util.function.DoubleConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<StudentGrade> studentGrades = DataUtil.init();
        System.out.println("Student grade records size is: " + studentGrades.stream().count());
//        parallelGPAOfStudent(studentGrades);


        serialGPAOfCourse(studentGrades);
        parallelGPAOfCourse(studentGrades);
        advancedParallelGPAOfCourse(studentGrades);

        serialGPAOfDept(studentGrades);
        parallelGPAOfDept(studentGrades);

        serialHighestAndLowestGpa(studentGrades);
        parallelHighestAndLowestGpa(studentGrades);


    }

    private static void serialGPAOfCourse(List<StudentGrade> studentGrades) {
        Date start = new Date();
        Map<String, List<StudentGrade>> scoresByCourse = studentGrades.stream()
                .collect(Collectors.groupingBy(StudentGrade::getCourseNumber));

        scoresByCourse.forEach((s, courseGrades) -> printGPA(s, courseGrades));
        Date end = new Date();
        System.out.println("Serial Course GPA computation used time: " + (end.getTime() - start.getTime()));
    }

    private static void parallelGPAOfCourse(List<StudentGrade> studentGrades) {
        Date start = new Date();
        Map<String, List<StudentGrade>> scoresByCourse = studentGrades.parallelStream()
                .collect(Collectors.groupingByConcurrent(StudentGrade::getCourseNumber));

        scoresByCourse.forEach((s, courseGrades) -> printGPA(s, courseGrades));
        Date end = new Date();
        System.out.println("Parallel Course GPA computation used time: " + (end.getTime() - start.getTime()));
    }

    /** Create a direct map with self-defined collector */
    private static void advancedParallelGPAOfCourse(List<StudentGrade> studentGrades) {
        Date start = new Date();
        Map<String, Double> doubleMap = studentGrades.parallelStream()
                .collect(Collectors.groupingBy(StudentGrade::getCourseNumber,
                        Collector.of(
                                () -> new double[]{0, 0},//Supplier provides the initial value, this is temp data type
                                (tempDoubleArr, studentGrade) -> {
                                    tempDoubleArr[0] += studentGrade.getTotalScore();
                                    tempDoubleArr[1] += studentGrade.getCredits();
                                },//Accumulator of BiConsumer type
                                (tempDoubleArr1, tempDoubleArr2) -> {
                                    tempDoubleArr1[0] += tempDoubleArr2[0];
                                    tempDoubleArr1[1] += tempDoubleArr2[1];
                                    return tempDoubleArr1;
                                },//Combiner
                                temp -> temp[0] / temp[1] //Finisher
                        )));
        for (Map.Entry<String, Double> doubleEntry : doubleMap.entrySet()) {
            System.out.printf("Course id: %s, average gpa is: %.2f\n", doubleEntry.getKey(), doubleEntry.getValue());
        }
        Date end = new Date();
        System.out.println("Advanced Parallel Course GPA computation used time: " + (end.getTime() - start.getTime()));
    }



    private static void serialGPAOfDept(List<StudentGrade> studentGrades) {
        Date start = new Date();
        Map<String, List<StudentGrade>> scoresByDprt= studentGrades.stream()
                .collect(Collectors.groupingBy(StudentGrade::getDepartmentId));

        scoresByDprt.forEach((s, courseGrades) -> printGPA(s, courseGrades));
        Date end = new Date();
        System.out.println("Serial Department GPA computation used time: " + (end.getTime() - start.getTime()));
    }

    private static void parallelGPAOfDept(List<StudentGrade> studentGrades) {
        Date start = new Date();
        Map<String, List<StudentGrade>> scoresByDprt= studentGrades.parallelStream()
                .collect(Collectors.groupingByConcurrent(StudentGrade::getDepartmentId));

        scoresByDprt.forEach((s, courseGrades) -> printGPA(s, courseGrades));
        Date end = new Date();
        System.out.println("Parallel Department GPA computation used time: " + (end.getTime() - start.getTime()));
    }

    private static void serialGpaOfStudent(List<StudentGrade> studentGrades) {
        Map<String, Double> collect = studentGrades.stream()
                .collect(Collectors.groupingBy(StudentGrade::getStudentId, Collectors.averagingDouble(StudentGrade::getTotalScore)));

        for (String s : collect.keySet()) {
            System.out.printf("Student id: %s, and total score is: %.2f\n", s, collect.get(s));
        }
    }

    private static void parallelGPAOfStudent(List<StudentGrade> studentGrades) {
        Map<String, List<StudentGrade>> map = studentGrades.parallelStream()
                .collect(Collectors.groupingBy(StudentGrade::getStudentId));

        map.forEach((s, grades) -> printGPA(s, grades));
    }

    private static void parallelGpaStatistics(List<StudentGrade> studentGrades) {
        Map<String, List<StudentGrade>> map = studentGrades.parallelStream()
                .collect(Collectors.groupingBy(StudentGrade::getStudentId));

        map.forEach((s, grades) -> printGPA(s, grades));
    }

    private static void printGPA(String s, List<StudentGrade> grades) {
        int credits = 0;
        double scores = 0.0;
        for (StudentGrade grade : grades) {
            credits += grade.getCredits();
            scores += grade.getScore() * grade.getCredits();
        }

        System.out.printf("Id is: %s, the GPA is: %.2f\n", s, scores/credits);
    }

    //gpa summarizing of student
    private static void serialGpaSumarizing(List<StudentGrade> studentGrades) {
        Date start = new Date();
        Map<String, List<StudentGrade>> scoresBySid = studentGrades.stream()
                .collect(Collectors.groupingByConcurrent(StudentGrade::getStudentId));

//        DoubleSummaryStatistics summaryStatistics = scoresBySid.entrySet().stream()
//                .map(entry -> new StudentGPA(entry.getKey(), entry.getValue()))
//                .collect(Collectors.summarizingDouble(studentGpa -> studentGpa.getGpa()));

//        System.out.println("Min Gpa: " + summaryStatistics.getMin());
//        System.out.println("Max Gpa: " + summaryStatistics.getMax());

        scoresBySid.entrySet().stream()
                .map(entry -> new StudentGPA(entry.getKey(), entry.getValue()))
                .map(studentGPA -> new GPA(studentGPA.getStudentId(), studentGPA.getGpa()))
                .sorted();
        Date end = new Date();
        System.out.println("Parallel Course GPA computation used time: " + (end.getTime() - start.getTime()));
    }

    //gpa summarizing of student
    private static void parallelHighestAndLowestGpa(List<StudentGrade> studentGrades) {
        printline();
        System.out.println("parallel computation of highest and lowest gpa: ");
        Date start = new Date();
        Map<String, Double> gpaMap = studentGrades.parallelStream()
                .collect(Collectors.groupingBy(StudentGrade::getStudentId,
                        Collector.of(
                                () -> new double[]{0, 0},
                                (r, v) -> {
                                    r[0] += v.getTotalScore();
                                    r[1] += v.getCredits();
                                },
                                (r1, r2) -> {
                                    r1[0] += r2[0];
                                    r1[1] += r2[1];
                                    return r1;
                                },
                                (r) -> r[0] / r[1]
                        )));

        Optional<Map.Entry<String, Double>> maxGpaOptional = gpaMap.entrySet().stream()
//                .sorted((e1, e2)-> ((e1.getValue() - e2.getValue()) > 0 ? 1 : -1))
                .max((e1, e2) -> ((e1.getValue() - e2.getValue()) > 0 ? 1 : -1));

        Map.Entry<String, Double> minGpa = gpaMap.entrySet().stream()
                .min((e1, e2) -> ((e1.getValue() - e2.getValue()) > 0 ? 1 : -1)).get();

        System.out.printf("Student with id: %s got the highest Gpa: %.2f\n", maxGpaOptional.get().getKey(), maxGpaOptional.get().getValue());
        System.out.printf("Student with id: %s got the highest Gpa: %.2f\n", minGpa.getKey(), minGpa.getValue());
        Date end = new Date();
        System.out.println("Parallel highest/lowest GPA computation used time: " + (end.getTime() - start.getTime()));
    }

    private static void serialHighestAndLowestGpa(List<StudentGrade> studentGrades) {
        printline();
        System.out.println("Serial computation of highest and lowest gpa: ");
        Date start = new Date();
        Map<String, Double> gpaMap = studentGrades.parallelStream()
                .collect(Collectors.groupingBy(StudentGrade::getStudentId,
                        Collector.of(
                                () -> new double[]{0, 0},
                                (r, v) -> {
                                    r[0] += v.getTotalScore();
                                    r[1] += v.getCredits();
                                },
                                (r1, r2) -> {
                                    r1[0] += r2[0];
                                    r1[1] += r2[1];
                                    return r1;
                                },
                                (r) -> r[0] / r[1]
                        )));

        Optional<Map.Entry<String, Double>> maxGpaOptional = gpaMap.entrySet().stream()
//                .sorted((e1, e2)-> ((e1.getValue() - e2.getValue()) > 0 ? 1 : -1))
                .max(gpaComparator);

        Map.Entry<String, Double> minGpa = gpaMap.entrySet().stream()
                .min((e1, e2) -> ((e1.getValue() - e2.getValue()) > 0 ? 1 : -1)).get();

        System.out.printf("Student with id: %s got the highest Gpa: %.2f\n", maxGpaOptional.get().getKey(), maxGpaOptional.get().getValue());
        System.out.printf("Student with id: %s got the highest Gpa: %.2f\n", minGpa.getKey(), minGpa.getValue());
        Date end = new Date();
        System.out.println("Serial highest/lowest GPA computation used time: " + (end.getTime() - start.getTime()));
    }

    private static void printline() {
        System.out.println("===============================================================");
    }

    static Comparator<Map.Entry<String, Double>> gpaComparator = (entry1, entry2) -> (entry1.getValue() - entry2.getValue() > 0 ? 1 : -1);
}



class GPAAverager implements DoubleConsumer {

    private double total = 0.0;
    private int count = 0;

    @Override
    public void accept(double value) {
        total += value;
        count++;
    }

    public double average() {
        return count > 0 ? (total)/count : 0;
    }

    public void combine(GPAAverager gpaAverager) {
        this.count += gpaAverager.count;
        this.total += gpaAverager.total;
    }
}

