package mansolsson.chip8;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class OpcodeRunnerThreadTest {
	@Rule
	public Timeout testTimeout = Timeout.seconds(2);
	
	@Test
	public void threadShouldTerminateWhenshutdownThreadIsCalled() throws InterruptedException {
		Chip8 chip8 = new Chip8();
		// Opcode doing nothing if no key is pressed.
		chip8.getMemory()[512] = 0xF1;
		chip8.getMemory()[512 + 1] = 0x0A;
		OpcodeRunnerThread thread = new OpcodeRunnerThread(chip8);
		thread.start();
		thread.shutdownThread();
		thread.join();
		assertEquals(Thread.State.TERMINATED, thread.getState());
	}
}
