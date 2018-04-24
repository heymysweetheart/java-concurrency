import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JacobiRelaxation {
    public static final int n = 10000;
    public static final float tolerance = 0.1f;
    public static final int threadNums = Runtime.getRuntime().availableProcessors();
    public static long sequentialTime, parallelTime;

    static float[][] a = new float[n][n], b = new float[n][n];
    static float[][] a1 = new float[n][n], b1 = new float[n][n];
    static float[][] a2 = new float[n][n], b2 = new float[n][n];
    static float[][] a3 = new float[n][n], b3 = new float[n][n];

    static AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) {
        initializeData();
        printLine();
        sequential(a, b);
        printLine();
        parallel(a1, b1);
        printLine();
        parallelByStream(a2,b2);
        printLine();
        parallelByStreamV1(a3,b3);
        check(a, a1);
        check(a, a2);
        check(a, a3);
    }

    private static void printLine() {
        System.out.println("==========================");
    }

    private static void check(float[][] a, float[][] a1) {
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(a[i][j] != a1[i][j]) {
                    System.out.println("The result is not equal." + "i: " +i + " j:" + j);
                    System.out.println("a[i][j]=" + a[i][j]);
                    System.out.println("a1[i][j]=" + a1[i][j]);
                }
            }
        }
    }

    private static void parallelByStream(float[][] a1, float[][] b1) {
        Date start = new Date();
        do {
            IntStream.range(1, a1.length -1)
                    .parallel()
                    .forEach(i -> process(i, a1, b1));
        } while(!isAccurateEnough(a1, b1));
        Date end = new Date();
        System.out.println("parallel by stream iterated times: " + counter.get());
        counter.set(0);//Shared counter, so reinitialize it to be 0
        System.out.println("Parallel by stream used time: " + (end.getTime() - start.getTime()));
    }

    //parallel for phase 1, sequential for phase 2 and tolerance check, this version should be slower than the previous one
    private static void parallelByStreamV1(float[][] a1, float[][] b1) {
        Date start = new Date();
        do {
            IntStream.range(1, a1.length -1)
                    .parallel()
                    .forEach(i -> process(i, a1, b1));
        } while(!isAccurateEnoughV1(a1, b1));
        Date end = new Date();
        System.out.println("parallel by stream V1 iterated times: " + counter.get());
        System.out.println("Parallel by stream V1 used time: " + (end.getTime() - start.getTime()));
    }

    private static boolean isAccurateEnough(float[][] a1, float[][] b1) {
        counter.incrementAndGet();
        Stream<Boolean> booleanStream = IntStream.range(1, a1.length - 1)
                .parallel()
                .mapToObj(i -> computeTolerance(i, a1, b1));
        return booleanStream.reduce(true, (v1, v2) -> v1 && v2);
    }

    private static boolean isAccurateEnoughV1(float[][] a1, float[][] b1) {
        counter.incrementAndGet();
        boolean isAccurate = true;
        for(int i=1; i<n -1; i++) {
            for(int j=1; j<n-1;j++) {
                if(Math.abs(a1[i][j] - b1[i][j]) > tolerance) {isAccurate = false;}
                a1[i][j] = b1[i][j];
            }
        }
        return isAccurate;
    }

    private static boolean computeTolerance(int i, float[][] a, float[][] b) {
        boolean isAccurate = true;
        for(int j=1;j<a[0].length -1;j++) {
            if(Math.abs(a[i][j] - b[i][j]) > tolerance) {isAccurate = false;}
            a[i][j] = b[i][j];
        }
        return isAccurate;
    }

    private static void process(int i, float[][] a1, float[][] b1) {
        for(int j=1; j<a1[0].length -1; j++) {
            b1[i][j] = (a1[i-1][j] + a1[i+1][j] + a1[i][j-1] + a1[i][j+1]) / 4;
        }
    }

    private static void parallel(float[][] a1, float[][] b1) {
        Date start = new Date();
        SCAN_ARRAY[] tasks = new SCAN_ARRAY[threadNums];
        Thread[] threads = new Thread[threadNums];
        Aggregate aggregate = new Aggregate(a1, b1, tolerance, tasks);
        CyclicBarrier barrier = new CyclicBarrier(threadNums);
        CyclicBarrier checkBarrier = new CyclicBarrier(threadNums, aggregate);

        for(int i=0;i<threadNums;i++) {
            int step = (n-2)/threadNums;
            if(i != threadNums -1) {
                tasks[i] = new SCAN_ARRAY(i * step + 1, (i + 1) * step, a1, b1, barrier, checkBarrier, tolerance);

            } else {
                tasks[i] = new SCAN_ARRAY(i * step + 1, n - 2, a1, b1, barrier, checkBarrier, tolerance);
            }
        }

        for(int i=0;i<threadNums;i++) {
            threads[i] = new Thread(tasks[i]);
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

        Date end = new Date();
        System.out.println("Parallel solution used time: " + (end.getTime() - start.getTime()));
        parallelTime = end.getTime() - start.getTime();
    }

    private static void sequential(float[][] a, float[][] b) {
        Date start = new Date();
        boolean done;
        int count = 0;
        do {
            for(int i=1;i<n-1;i++) {
                for(int j=1;j<n-1;j++) {
                    b[i][j] = (a[i-1][j] + a[i+1][j] + a[i][j-1] + a[i][j+1]) / 4.0f;
                }
            }

            done = true;

            for(int i=1;i<n-1;i++) {
                for(int j=1;j<n-1;j++) {
                    if(Math.abs(b[i][j] - a[i][j]) > tolerance) {
                        done = false;
                    }
                    a[i][j] = b[i][j];
                }
            }
            count++;
        } while (!done);
        Date end = new Date();
        System.out.println("For sequential solution the loop logic runs times: " + count);
        System.out.println("Sequential solution used time: " + (end.getTime() - start.getTime()));
        sequentialTime = end.getTime() - start.getTime();
    }

    private static void initializeData() {
        for(int i=1;i<n - 1 ;i++) {
            for(int j=1;j<n -1;j++) {
                a[i][j] = 0.0f;
                a1[i][j] = 0.0f;
                a2[i][j] = 0.0f;
                a3[i][j] = 0.0f;
            }
        }

        for(int i=0; i<n; i++) {
            a[0][i] = 10.0f;
            a[n-1][i] = 10.0f;
            a1[0][i] = 10.0f;
            a1[n-1][i] = 10.0f;
            a2[0][i] = 10.0f;
            a2[n-1][i] = 10.0f;
            a3[0][i] = 10.0f;
            a3[n-1][i] = 10.0f;
        }

        for(int i=0; i<n; i++) {
            a[i][0] = 10.0f;
            a[i][n-1] = 10.0f;
            a1[i][0] = 10.0f;
            a1[i][n-1] = 10.0f;
            a2[i][0] = 10.0f;
            a2[i][n-1] = 10.0f;
            a3[i][0] = 10.0f;
            a3[i][n-1] = 10.0f;
        }


        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                b[i][j] = 0.0f;
                b1[i][j] = 0.0f;
                b2[i][j] = 0.0f;
                b3[i][j] = 0.0f;
            }
        }

//        for (float[] doubles : a) {
//            for (double aDouble : doubles) {
//                System.out.print(aDouble + " ");
//            }
//            System.out.println();
//        }
    }
}

class SCAN_ARRAY implements Runnable {

    int start, end;
    Boolean done;
    CyclicBarrier barrier;
    CyclicBarrier checkBarrier;
    float [][] a, b;
    float tolerance;

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public SCAN_ARRAY(int start, int end, float[][] a, float[][] b, CyclicBarrier barrier, CyclicBarrier checkBarrier, float tolerance) {
        this.start = start;
        this.end = end;
        this.a = a;
        this.b = b;
        this.barrier = barrier;
        this.checkBarrier = checkBarrier;
        this.tolerance = tolerance;
    }

    @Override
    public void run() {

        try {
            do {
                for (int i = start; i <= end; i++) {
                    for (int j = 1; j < a.length - 1; j++) {
                        b[i][j] = (a[i - 1][j] + a[i + 1][j] + a[i][j - 1] + a[i][j + 1]) / 4;
                    }
                }
                barrier.await();

                done = true;
                for (int i = start; i <= end; i++) {
                    for (int j = 1; j < a.length - 1; j++) {
                        if(Math.abs(a[i][j] - b[i][j]) > tolerance) {
                            done = false;
                        }
                        a[i][j] = b[i][j];
                    }
                }
                checkBarrier.await();//&& , set to each thread.
            } while (!done);
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//Compute global done for different thread
class Aggregate implements Runnable {

    volatile int count = 0;

    float tolerance;

    float[][] a, b;

    SCAN_ARRAY[] tasks;

    public Aggregate(float[][] a, float[][] b, float tolerance, SCAN_ARRAY[] tasks) {
        this.a = a;
        this.b = b;
        this.tolerance = tolerance;
        this.tasks = tasks;
    }

    @Override
    public void run() {
        boolean done = true;
        count++;

        for (SCAN_ARRAY task : tasks) {
            done = done && task.getDone();
        }


        for (SCAN_ARRAY task : tasks) {
            task.setDone(done);
        }

        if(done) {
            System.out.println("The aggregator thread runs times: " + count);
        }
    }
}
