package mansolsson.chip8;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.media.AudioClip;

public class TimerCountdownThread extends Thread {
	private static final long MILLIS_PER_CYCLE = 1000 / 60;

	private final Chip8 chip8;
	private final AtomicBoolean running = new AtomicBoolean();
	private final AudioClip sound = new AudioClip(getClass().getClassLoader().getResource("sound/blip.wav").toString());

	public TimerCountdownThread(final Chip8 chip8) {
		this.chip8 = chip8;
		running.set(true);
	}

	@Override
	public void run() {
		while (running.get()) {
			final long before = System.currentTimeMillis();
			chip8.getDelayTimer().reduce();
			chip8.getSoundTimer().reduce();
			playSound();
			final long after = System.currentTimeMillis();
			sleepIfNeeded(after - before);
		}
	}

	private void playSound() {
		if (chip8.getSoundTimer().getTime() > 0 && !sound.isPlaying()) {
			sound.play();
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

	public void shutdown() {
		running.set(false);
	}
}
