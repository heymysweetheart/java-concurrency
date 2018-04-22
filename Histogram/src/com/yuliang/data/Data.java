package com.yuliang.data;


public class Data {

    public static final int threadsNum = Runtime.getRuntime().availableProcessors();


    public static int[][] init(int n, int max) {
        int[][] ints = new int[n][n];
        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                ints[i][j] = (i + j) % max;
            }
        }
        return ints;
    }
}
