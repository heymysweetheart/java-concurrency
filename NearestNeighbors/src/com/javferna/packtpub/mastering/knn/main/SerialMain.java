package com.javferna.packtpub.mastering.knn.main;

import java.util.Date;
import java.util.List;

import com.javferna.packtpub.mastering.knn.data.BankMarketing;
import com.javferna.packtpub.mastering.knn.loader.BankMarketingLoader;
import com.javferna.packtpub.mastering.knn.serial.KnnClassifier;

/**
 * Main class that launches the tests using the serial knn with serial sorting
 * @author Usuario
 *
 */
public class SerialMain {

	public static void main(String[] args) {

		BankMarketingLoader loader = new BankMarketingLoader();
		List<BankMarketing> train = loader.load(DataUtil.BankPath);
		System.out.println("Train: " + train.size());
		List<BankMarketing> test = loader.load(DataUtil.TestPath);
		System.out.println("Test: " + test.size());
		int testSize = 2059;
		double currentTime = 0d;
		int success = 0, mistakes = 0;
		
		int k = 10;
		if (args.length==1) {
			k = Integer.parseInt(args[0]);
		}

		success = 0;
		mistakes = 0;
		List<BankMarketing> marketings = test.subList(0, testSize);
		KnnClassifier classifier = new KnnClassifier(train, k);
		try {
			Date start, end;
			start = new Date();
			for (BankMarketing example : marketings) {
				String tag = classifier.classify(example);
				if (tag.equals(example.getTag())) {
					success++;
				} else {
					mistakes++;
				}
			}
			end = new Date();

			currentTime = end.getTime() - start.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Serial Classifier - K: " + k);
		System.out.println("Success: " + success);
		System.out.println("Mistakes: " + mistakes);
		System.out.println("Execution Time: " + (currentTime / (float)1000)
				+ " seconds.");

	}

}
