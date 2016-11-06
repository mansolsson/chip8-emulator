package mansolsson.chip8;

import java.util.concurrent.atomic.AtomicBoolean;

public class OpcodeRunnerThread extends Thread {
	private static final long NR_CYCLES_PER_SECOND = 500L;
	private static final long MILLIS_PER_CYCLE = 1000L / NR_CYCLES_PER_SECOND;
	private AtomicBoolean running = new AtomicBoolean();
	private Chip8 chip8;

	public OpcodeRunnerThread(Chip8 chip8) {
		this.chip8 = chip8;
		running.set(true);
	}

	@Override
	public void run() {
		while (running.get()) {
			long before = System.currentTimeMillis();
			chip8.runNextInstruction();
			long after = System.currentTimeMillis();
			sleepIfNeeded(after - before);
		}
	}

	private void sleepIfNeeded(long elapsedTime) {
		long millisToSleep = MILLIS_PER_CYCLE - elapsedTime;
		if (millisToSleep > 0) {
			try {
				Thread.sleep(millisToSleep);
			} catch (InterruptedException e) {
			}
		}
	}

	public void shutdownThread() {
		running.set(false);
	}
}
