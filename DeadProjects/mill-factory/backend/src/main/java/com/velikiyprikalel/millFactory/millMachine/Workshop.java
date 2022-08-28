package com.velikiyprikalel.millFactory.millMachine;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.velikiyprikalel.millFactory.bean.Flour;
import com.velikiyprikalel.millFactory.bean.Millet;
import com.velikiyprikalel.millFactory.millEngine.Engine;

public class Workshop extends Machine {
	private final Logger logger = Logger.getLogger(Machine.class.toString());
	
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private final Engine engine;
	private final Queue<Millet> mallets;
	private final Queue<Flour> floursQueue;

	public Workshop(Engine engine, Queue<Millet> mallets, Queue<Flour> floursQueue) {
		this.engine = engine;
		this.mallets = mallets;
		this.floursQueue = floursQueue;
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			if (engine.getPower() > 0) {
				engine.decPower(1);

				this.executor.submit(() -> {
					Millet millet = mallets.poll();
					if (millet != null) {
						floursQueue.offer(new Flour());
						logger.info("Flour: " + floursQueue.size());
					}
					logger.info("Power left: " + engine.getPower());
				});
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.throwing(Machine.class.toString(), "run()", e);
				}
			}
		}
	}
}
