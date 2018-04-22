package com.yuliang.serial;

import com.yuliang.data.Result;

import java.util.Date;

public class SerialHistogram {
    public static Result getHistogram(int[][] histogram, int max) {
        Date start = new Date(), end;
        int n = histogram.length;
        int[] hist = new int[max];
        int histValue;

        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                histValue = histogram[i][j];
                hist[histValue]++;
            }
        }
        end = new Date();

        return new Result(start.getTime(), end.getTime(), hist);
    }
}
