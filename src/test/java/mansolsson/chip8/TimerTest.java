package mansolsson.chip8;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimerTest {
	@Test
	public void countdownReducesTimerByOne() {
	    final Timer timer = new Timer();
		timer.setTime(10);

		timer.reduce();

		assertEquals(9, timer.getTime());
	}

	@Test
	public void countdownDoesNotReduceBelowZero() {
	    final Timer timer = new Timer();
		timer.setTime(0);

		timer.reduce();

		assertEquals(0, timer.getTime());
	}
}
