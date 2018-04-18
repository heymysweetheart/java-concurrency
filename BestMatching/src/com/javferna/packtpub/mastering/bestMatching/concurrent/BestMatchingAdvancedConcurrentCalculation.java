package com.javferna.packtpub.mastering.bestMatching.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.javferna.packtpub.mastering.bestMatching.common.BestMatchingData;
import com.javferna.packtpub.mastering.bestMatching.distance.LevenshteinDistance;

public class BestMatchingAdvancedConcurrentCalculation {

	public static BestMatchingData getBestMatchingWords(String word, List<String> dictionary) throws InterruptedException, ExecutionException {

		int numCores = Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numCores);

		int size = dictionary.size();
		int step = size / numCores;
		int startIndex, endIndex;
		List<Future<BestMatchingData>> results;
//		List<BestMatchingBasicTask> tasks = new ArrayList<>();
//
//		for (int i = 0; i < numCores; i++) {
//			startIndex = i * step;
//			if (i == numCores - 1) {
//				endIndex = dictionary.size();
//			} else {
//				endIndex = (i + 1) * step;
//			}
//			BestMatchingBasicTask task = new BestMatchingBasicTask(startIndex, endIndex, dictionary, word);
//			tasks.add(task);
//		}

		List<Callable<BestMatchingData>> tasks = new ArrayList<>();
		for(int i=0; i< numCores; i++) {
			startIndex = i * step;
			if(i < numCores -1) {
				endIndex = (i + 1) * step;
			} else {
				endIndex = size;
			}

			int start = startIndex, end = endIndex;

			tasks.add(() -> {
				List<String> rets =new ArrayList<String>();
				int minDistance=Integer.MAX_VALUE;
				int distance;
				for (int j=start; j<end; j++) {
					distance= LevenshteinDistance.calculate(word,dictionary.get(j));
					if (distance<minDistance) {
						rets.clear();
						minDistance=distance;
						rets.add(dictionary.get(j));
					} else if (distance==minDistance) {
						rets.add(dictionary.get(j));
					}
				}

				BestMatchingData result=new BestMatchingData();
				result.setWords(rets);
				result.setDistance(minDistance);
				return result;
			});
		}

		results = executor.invokeAll(tasks);
		executor.shutdown();
		
		List<String> words = new ArrayList<String>();
		int minDistance = Integer.MAX_VALUE;
		for (Future<BestMatchingData> future : results) {
			BestMatchingData data = future.get();
			if (data.getDistance() < minDistance) {
				words.clear();
				minDistance = data.getDistance();
				words.addAll(data.getWords());
			} else if (data.getDistance() == minDistance) {
				words.addAll(data.getWords());
			}
		}

		BestMatchingData result = new BestMatchingData();
		result.setDistance(minDistance);
		result.setWords(words);
		return result;

	}

}
