package com.velikiyprikalel.millFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.velikiyprikalel.millFactory.bean.Flour;
import com.velikiyprikalel.millFactory.bean.MillState;
import com.velikiyprikalel.millFactory.bean.Millet;
import com.velikiyprikalel.millFactory.bean.Water;

@Service
public class MillService {
	private final Logger logger = Logger.getLogger(MillService.class.toString());

	private static final BlockingQueue<Water> waterFlow = new ArrayBlockingQueue<Water>(10);
	private static final BlockingQueue<Millet> milletFlow = new ArrayBlockingQueue<Millet>(10);
	private static final BlockingQueue<Flour> flourFlow = new ArrayBlockingQueue<Flour>(10);

	private Mill mill;

	private final ExecutorService waterPipeExecutor = Executors.newSingleThreadExecutor();
	private final ExecutorService milletPipeExecutor = Executors.newSingleThreadExecutor();

	public MillService() {
		mill = new Mill(waterFlow, milletFlow, flourFlow);
		mill.start();
	}

	public MillState getState() {
		return new MillState(mill, waterFlow, milletFlow, flourFlow);
	}

	public void addWater(int capacity) {
		waterPipeExecutor.execute(() -> {
			int counter = capacity;
			try {

				while (counter > 0) {
					waterFlow.add(new Water());
					
					counter--;
				}
			} catch (IllegalStateException e) {
				logger.info("Water tank is full: " + e.getMessage());
			}
		});
	}

	public void addMillet(int capacity) {
		milletPipeExecutor.execute(() -> {
			int counter = capacity;
			try {

				while (counter > 0) {
					milletFlow.add(new Millet());
					
					counter--;
				}
			} catch (IllegalStateException e) {
				logger.info("Millet bank is full: " + e.getMessage());
			}
		});
	}

	public void dropFlour() {
		flourFlow.clear();
	}
}
