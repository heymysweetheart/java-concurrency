package com.yuliang;

import com.yuliang.data.Data;
import com.yuliang.data.Result;
import com.yuliang.parallel.ParallelHistogram1;
import com.yuliang.parallel.ParallelHistogram3;
import com.yuliang.parallel.ParallelHistogram4;
import com.yuliang.serial.SerialHistogram;

public class CompleteMain {

    public static final int N = 20000;
    public static final int FIRST_MAX = 10;
    public static final int SECOND_MAX= 100;

    public static void main(String[] args) {

        int[][] basicHistogram = Data.init(N, FIRST_MAX);
        int[][] normalHistogram = Data.init(N, SECOND_MAX);

        Result serialHistogram = SerialHistogram.getHistogram(basicHistogram, SECOND_MAX);
        printResult(serialHistogram, "Serial version");

        Result hitogramV1 = ParallelHistogram1.getHitogram(basicHistogram, FIRST_MAX);
        printResult(hitogramV1, "Parallel version 1");

        Result hitogramV2 = ParallelHistogram1.getHitogram(normalHistogram, SECOND_MAX);
        printResult(hitogramV2, "Parallel version 2");

        Result hitogramV3 = ParallelHistogram3.getHitogram(normalHistogram, SECOND_MAX);
        printResult(hitogramV3, "Parallel version 3");

        Result hitogramV4 = ParallelHistogram4.getHitogram(normalHistogram, SECOND_MAX);
        printResult(hitogramV4, "Parallel version 4");
    }

    private static void printResult(Result result, String taskName) {
        System.out.printf("%s, Time used: %d\n", taskName, (result.getEndTime() - result.getStartTime()));
        System.out.println("=======================");
    }
}
