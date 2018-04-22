package com.yuliang.parallel;

import com.yuliang.data.Data;
import com.yuliang.data.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelHistogram3 {
    public static Result getHitogram(int[][] histogram, int max) {

        Date startTime = new Date(), endTime;
        int n = histogram.length;
        int[] hist = new int[max];

        List<TaskV3> tasks = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        int length = n / Data.threadsNum;
        int start, end;

        for(int i=0;i<Data.threadsNum;i++) {
            start = i * length;
            if(i<n-1) {
                end = (i + 1) * length;
            } else {
                end = histogram.length;
            }

            tasks.add(new TaskV3(histogram, start, end, hist));
        }

        for (TaskV3 task : tasks) {
            threads.add(new Thread(task));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int value : hist) {
            System.out.println(value);
        }
        endTime = new Date();
        return new Result(startTime.getTime(), endTime.getTime(), hist);
    }

}

class TaskV3 implements Runnable {
    private int[][] histogram;

    private int start, end;

    private int[] hist;

    public TaskV3(int[][] histogram, int start, int end, int[] hist) {
        this.histogram = histogram;
        this.start = start;
        this.end = end;
        this.hist = hist;
    }

    @Override
    public void run() {
        int value;
        for(int i=start;i<end;i++) {
            for(int j=0;j<histogram[0].length;j++) {
                value = histogram[i][j];
                hist[value]++;
            }
        }

    }
}

