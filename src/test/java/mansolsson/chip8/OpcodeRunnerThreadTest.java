package mansolsson.chip8;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OpcodeRunnerThreadTest {
	@Test
	public void threadShouldTerminateWhenshutdownThreadIsCalled() throws InterruptedException {
		final Chip8 chip8 = new Chip8();
		// Opcode doing nothing if no key is pressed.
		chip8.getMemory()[512] = 0xF1;
		chip8.getMemory()[512 + 1] = 0x0A;
		final OpcodeRunnerThread thread = new OpcodeRunnerThread(chip8);
		thread.start();
		thread.shutdownThread();
		thread.join(2_000L);
		assertEquals(Thread.State.TERMINATED, thread.getState());
	}
}
