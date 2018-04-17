package com.javferna.packtpub.mastering.knn.main;


public class CompleteMain {

    public static void main(String[] args) {
        print();
        SerialMain.main(args);
        print();
        ParallelIndividualMainSort.main(args);
        print();
        ParallelGrouplMain.main(args);
        print();
        ParallelGroupMainSort.main(args);
        print();
        ParallelGroupMainSortNew.main(args);
    }

    public static void print() {
        System.out.println("========================================================================");
    }

}
