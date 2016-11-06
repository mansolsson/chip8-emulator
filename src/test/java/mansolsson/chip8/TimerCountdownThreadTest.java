package mansolsson.chip8;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimerCountdownThreadTest {
	@Test
	public void threadShouldTerminateWhenshutdownThreadIsCalled() throws InterruptedException {
		final TimerCountdownThread thread = new TimerCountdownThread(new Chip8());
		thread.start();

		thread.shutdown();

		await().atMost(1, TimeUnit.SECONDS).until(() -> assertEquals(Thread.State.TERMINATED, thread.getState()));
	}
}
