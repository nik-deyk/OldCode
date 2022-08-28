package com.velikiyprikalel.millFactory.millEngine;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.velikiyprikalel.millFactory.bean.Water;

public class WaterWheel extends Engine {
	private final Logger logger = Logger.getLogger(WaterWheel.class.toString());

	private final Queue<Water> waterFlow;
	private static int MAX_POWER = 10;

	private final ExecutorService executor = Executors.newFixedThreadPool(2);

	public WaterWheel(Queue<Water> waterFlow) {
		setName("Thread.WaterWheel");
		this.waterFlow = waterFlow;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			Water water = waterFlow.poll();
			this.executor.execute(() -> {
				if (water != null && this.getPower() < MAX_POWER) {
					this.incPower(water.getPower());
				}
			});
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				logger.throwing(WaterWheel.class.toString(), "run()", e);
			}
		}
	}
}
