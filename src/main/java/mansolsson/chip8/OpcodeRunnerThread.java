package mansolsson.chip8;

import java.util.concurrent.atomic.AtomicBoolean;

public class OpcodeRunnerThread extends Thread {
	private static final long NR_CYCLES_PER_SECOND = 500L;
	private static final long MILLIS_PER_CYCLE = 1000L / NR_CYCLES_PER_SECOND;
	private final AtomicBoolean running = new AtomicBoolean();
	private final Chip8 chip8;

	public OpcodeRunnerThread(final Chip8 chip8) {
		this.chip8 = chip8;
		running.set(true);
	}

	@Override
	public void run() {
		while (running.get()) {
			final long before = System.currentTimeMillis();
			chip8.runNextInstruction();
			final long after = System.currentTimeMillis();
			sleepIfNeeded(after - before);
		}
	}

	private void sleepIfNeeded(final long elapsedTime) {
		final long millisToSleep = MILLIS_PER_CYCLE - elapsedTime;
		if (millisToSleep > 0) {
			try {
				Thread.sleep(millisToSleep);
			} catch (final InterruptedException e) {
			}
		}
	}

	public void shutdownThread() {
		running.set(false);
	}
}
