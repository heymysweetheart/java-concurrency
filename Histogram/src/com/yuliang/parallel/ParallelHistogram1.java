package com.yuliang.parallel;

import com.yuliang.data.Data;
import com.yuliang.data.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelHistogram1 {
    public static Result getHitogram(int[][] histogram, int max) {

        Date startTime = new Date(), endTime;
        int n = histogram.length;
        AtomicInteger[] hist = new AtomicInteger[max];

        for(int i=0;i<max;i++) {
            hist[i] = new AtomicInteger(0);
        }


        List<Task> tasks = new ArrayList<>();
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

            tasks.add(new Task(histogram, start, end, hist));
        }

        for (Task task : tasks) {
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

        for (AtomicInteger atomicInteger : hist) {
            System.out.println(atomicInteger.get());
        }
        endTime = new Date();
        return new Result(startTime.getTime(), endTime.getTime(), hist);
    }

}

class Task implements Runnable {
    private int[][] histogram;

    private int start, end;

    private AtomicInteger[] hist;

    public Task(int[][] histogram, int start, int end, AtomicInteger[] hist) {
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
                hist[value].incrementAndGet();
            }
        }

    }
}
