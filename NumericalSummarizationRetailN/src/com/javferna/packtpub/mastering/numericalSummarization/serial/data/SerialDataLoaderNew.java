package com.javferna.packtpub.mastering.numericalSummarization.serial.data;

import com.javferna.packtpub.mastering.numericalSummarization.common.Record;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class SerialDataLoaderNew {

	public static List<Record> load(String path) throws IOException {
		System.out.println("Loading data");

		List<Record> records = null;
		
//		List<String> lines = Files.readAllLines(path);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(SerialDataLoaderNew.class.getClassLoader().getResourceAsStream(path)))) {
			records = reader.lines()
					.skip(1)
					.map(l -> l.split(";"))
					.map(t -> new Record(t))
					.collect(Collectors.toList());


		}

//		List<Record> records = lines
//				.stream()
//				.skip(1)
//				.map(l -> l.split(";"))
//				.map(t -> new Record(t))
//				.collect(Collectors.toList());

		return records;
	}
}
