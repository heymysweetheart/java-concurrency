package com.javferna.packtpub.mastering.bestMatching.concurrent;

import com.javferna.packtpub.mastering.bestMatching.serial.BestMatchingSerialMain;

import java.util.Scanner;

public class CompleteMain {
    public static void main(String[] args) {
        while(true) {
            System.out.println();
            print();
            System.out.println("Please input a word to calculate (Input Q to exit):");
            String next = new Scanner(System.in).next();
            if(next.equals("Q")) return;
            args = new String[1];
            args[0] = next;
            print();
            BestMatchingSerialMain.main(args);
            print();
            BestMatchingConcurrentMain.main(args);
            print();
            BestMatchingConcurrentAdvancedMain.main(args);
        }

    }

    public static void print() {
        System.out.println("========================================================================");
    }
}
