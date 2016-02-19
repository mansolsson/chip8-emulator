package mansolsson.chip8;

import java.util.concurrent.atomic.AtomicBoolean;

public class TimerCountdownThread extends Thread {
	private static final long MILLIS_PER_CYCLE = 1000 / 60;

	private Chip8 chip8;
	private AtomicBoolean running = new AtomicBoolean();

	public TimerCountdownThread(Chip8 chip8) {
		this.chip8 = chip8;
		running.set(true);
	}

	@Override
	public void run() {
		while (running.get()) {
			long before = System.currentTimeMillis();
			chip8.getDelayTimer().reduce();
			chip8.getSoundTimer().reduce();
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
