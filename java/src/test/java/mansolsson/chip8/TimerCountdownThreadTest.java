package mansolsson.chip8;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class TimerCountdownThreadTest {
	@Rule
	public Timeout testTimeout = Timeout.seconds(2);

	@Test
	public void threadShouldTerminateWhenshutdownThreadIsCalled() throws InterruptedException {
		TimerCountdownThread thread = new TimerCountdownThread(new Chip8());
		thread.start();
		thread.shutdownThread();
		thread.join();
		assertEquals(Thread.State.TERMINATED, thread.getState());
	}
}
