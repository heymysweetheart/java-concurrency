package com.javferna.packtpub.mastering.bestMatching.concurrent;

import com.javferna.packtpub.mastering.bestMatching.distance.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

public class ExistBasicConcurrentCalculation {

	public static boolean existWord(String word, List<String> dictionary) throws InterruptedException, ExecutionException {
		int numCores = Runtime.getRuntime().availableProcessors();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numCores);

		int size = dictionary.size();
		int step = size / numCores;
		int startIndex, endIndex;
//		List<ExistBasicTask> tasks = new ArrayList<>();
		List<Callable<Boolean>> tasks = new ArrayList<>();

		for (int i = 0; i < numCores; i++) {
			startIndex = i * step;
			int start = startIndex;
			if (i == numCores - 1) {
				endIndex = dictionary.size();
			} else {
				endIndex = (i + 1) * step;
			}
			int end = endIndex;
			//ExistBasicTask task = new ExistBasicTask(startIndex, endIndex, dictionary, word);
			//tasks.add(task);
			tasks.add(() -> {
				for (int j=start; j<end; j++) {
					if (LevenshteinDistance.calculate(word, dictionary.get(j))==0) {
						return true;
					}

					if (Thread.interrupted()) {
						return false;
					}
				}
				return false;
			});
		}
		try {
//			Boolean result = executor.invokeAny(tasks);
			List<Future<Boolean>> futures = executor.invokeAll(tasks);
			for (Future<Boolean> future : futures) {
				if(future.get()==true) {
					return true;
				}
			}
			return false;
		} catch (ExecutionException e) {
			if (e.getCause() instanceof NoSuchElementException)
				return false;
			throw e;
		} finally {
			executor.shutdown();
		}
	}
}
