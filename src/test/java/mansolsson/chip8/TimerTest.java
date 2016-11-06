package mansolsson.chip8;


import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {
	private Timer timer;
	
	@Before
	public void setUp() {
		timer = new Timer();
	}
	
	@Test
	public void countdownReducesTimerByOne() {
		timer.setTime(10);
		timer.reduce();
		assertEquals(9, timer.getTime());
	}
	
	@Test
	public void countdownDoesNotReduceBelowZero() {
		timer.setTime(0);
		timer.reduce();
		assertEquals(0, timer.getTime());
	}
}
