package com.javferna.packtpub.mastering.numericalSummarization.serial.main;

import com.javferna.packtpub.mastering.numericalSummarization.common.Record;
import com.javferna.packtpub.mastering.numericalSummarization.serial.data.SerialDataLoaderNew;
import com.javferna.packtpub.mastering.numericalSummarization.util.DataUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<Record> records = Collections.emptyList();
        try {
            records = SerialDataLoaderNew.load(DataUtil.PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Date start = new Date();
        System.out.println(records.stream()
            .mapToInt(record -> record.getQuantity())
            .average());
        Date end = new Date();
        System.out.println("Time used: " + (end.getTime() -start.getTime()));

        records.stream()
                .mapToDouble(record -> record.getQuantity())
                .collect(Collectors.averagingDouble())
    }
}
