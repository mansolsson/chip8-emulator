package mansolsson.chip8;

import java.util.Random;
import java.util.Stack;

public class Chip8 {
	private static final int BYTES_PER_INSTRUCTION = 2;
	private static final int START_OF_PROGRAM = 512;

	private Screen screen = new Screen();
	private Stack<Integer> stack = new Stack<Integer>();
	private int pc;
	private int[] v = new int[16];
	private int I;
	private int[] memory = new int[4096];
	private Keyboard keyboard = new Keyboard();
	private Timer delayTimer = new Timer();
	private Timer soundTimer = new Timer();

	public Chip8() {
		init();
	}

	public void init() {
		screen.clear();
		stack.clear();
		pc = START_OF_PROGRAM;
		for (int i = 0; i < v.length; i++) {
			v[i] = 0;
		}
		I = 0;
		for (int i = 0; i < memory.length; i++) {
			memory[i] = 0;
		}
		putCharactersInMemory();
		delayTimer.setTime(0);
		soundTimer.setTime(0);
	}

	public void loadProgram(byte[] program) {
		for (int i = 0; i < program.length; i++) {
			memory[START_OF_PROGRAM + i] = program[i] & 0xFF;
		}
	}

	public void runNextInstruction() {
		int opcode = (memory[pc] << 8) | memory[pc + 1];
		runOpcode(new Opcode(opcode));
	}

	private void runOpcode(Opcode opcode) {
		if (opcode.getBit12To16() == 0) {
			runCommandGroup0(opcode);
		} else if (opcode.getBit12To16() == 1) {
			runCommandGroup1(opcode);
		} else if (opcode.getBit12To16() == 2) {
			runCommandGroup2(opcode);
		} else if (opcode.getBit12To16() == 3) {
			runCommandGroup3(opcode);
		} else if (opcode.getBit12To16() == 4) {
			runCommandGroup4(opcode);
		} else if (opcode.getBit12To16() == 5) {
			runCommandGroup5(opcode);
		} else if (opcode.getBit12To16() == 6) {
			runCommandGroup6(opcode);
		} else if (opcode.getBit12To16() == 7) {
			runCommandGroup7(opcode);
		} else if (opcode.getBit12To16() == 8) {
			runCommandGroup8(opcode);
		} else if (opcode.getBit12To16() == 9) {
			runCommandGroup9(opcode);
		} else if (opcode.getBit12To16() == 0xA) {
			runCommandGroupA(opcode);
		} else if (opcode.getBit12To16() == 0xB) {
			runCommandGroupB(opcode);
		} else if (opcode.getBit12To16() == 0xC) {
			runCommandGroupC(opcode);
		} else if (opcode.getBit12To16() == 0xD) {
			runCommandGroupD(opcode);
		} else if (opcode.getBit12To16() == 0xE) {
			runCommandGroupE(opcode);
		} else {
			runCommandGroupF(opcode);
		}
	}

	private void runCommandGroup0(Opcode opcode) {
		if (opcode.getFirst8Bits() == 0xE0) {
			screen.clear();
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0xEE) {
			pc = stack.pop();
		} else {
			throw new UnknownOpcodeException(opcode);
		}
	}

	private void runCommandGroup1(Opcode opcode) {
		pc = opcode.getFirst12Bits();
	}

	private void runCommandGroup2(Opcode opcode) {
		moveToNextInstruction();
		stack.push(pc);
		pc = opcode.getFirst12Bits();
	}

	private void runCommandGroup3(Opcode opcode) {
		if (v[opcode.getBit8To12()] == opcode.getFirst8Bits()) {
			moveToNextInstruction();
		}
		moveToNextInstruction();
	}

	private void runCommandGroup4(Opcode opcode) {
		if (v[opcode.getBit8To12()] != opcode.getFirst8Bits()) {
			moveToNextInstruction();
		}
		moveToNextInstruction();
	}

	private void runCommandGroup5(Opcode opcode) {
		if (opcode.getFirst4Bits() == 0) {
			if (v[opcode.getBit8To12()] == v[opcode.getBit4To8()]) {
				moveToNextInstruction();
			}
			moveToNextInstruction();
		} else {
			throw new UnknownOpcodeException(opcode);
		}
	}

	private void runCommandGroup6(Opcode opcode) {
		v[opcode.getBit8To12()] = opcode.getFirst8Bits();
		moveToNextInstruction();
	}

	private void runCommandGroup7(Opcode opcode) {
		v[opcode.getBit8To12()] += opcode.getFirst8Bits();
		v[opcode.getBit8To12()] &= 0xFF;
		moveToNextInstruction();
	}

	private void runCommandGroup8(Opcode opcode) {
		if (opcode.getFirst4Bits() == 0) {
			v[opcode.getBit8To12()] = v[opcode.getBit4To8()];
			moveToNextInstruction();
		} else if (opcode.getFirst4Bits() == 1) {
			v[opcode.getBit8To12()] |= v[opcode.getBit4To8()];
			moveToNextInstruction();
		} else if (opcode.getFirst4Bits() == 2) {
			v[opcode.getBit8To12()] &= v[opcode.getBit4To8()];
			moveToNextInstruction();
		} else if (opcode.getFirst4Bits() == 3) {
			v[opcode.getBit8To12()] ^= v[opcode.getBit4To8()];
			moveToNextInstruction();
		} else if (opcode.getFirst4Bits() == 4) {
			v[opcode.getBit8To12()] += v[opcode.getBit4To8()];
			v[0xF] = v[opcode.getBit8To12()] > 0xFF ? 1 : 0;
			v[opcode.getBit8To12()] &= 0xFF;
			moveToNextInstruction();
		} else if (opcode.getFirst4Bits() == 5) {
			v[opcode.getBit8To12()] -= v[opcode.getBit4To8()];
			v[0xF] = v[opcode.getBit8To12()] < 0 ? 0 : 1;
			v[opcode.getBit8To12()] &= 0xFF;
			moveToNextInstruction();
		} else if (opcode.getFirst4Bits() == 6) {
			v[0xF] = v[opcode.getBit8To12()] & 1;
			v[opcode.getBit8To12()] >>>= 1;
			moveToNextInstruction();
		} else if (opcode.getFirst4Bits() == 7) {
			v[opcode.getBit8To12()] = v[opcode.getBit4To8()] - v[opcode.getBit8To12()];
			v[0xF] = v[opcode.getBit8To12()] < 0 ? 0 : 1;
			v[opcode.getBit8To12()] &= 0xFF;
			moveToNextInstruction();
		} else if (opcode.getFirst4Bits() == 0xE) {
			v[0xF] = (v[opcode.getBit8To12()] >>> 7) & 1;
			v[opcode.getBit8To12()] <<= 1;
			v[opcode.getBit8To12()] &= 0xFF;
			moveToNextInstruction();
		} else {
			throw new UnknownOpcodeException(opcode);
		}
	}

	private void runCommandGroup9(Opcode opcode) {
		if (opcode.getFirst4Bits() == 0) {
			if (v[opcode.getBit8To12()] != v[opcode.getBit4To8()]) {
				moveToNextInstruction();
			}
			moveToNextInstruction();
		} else {
			throw new UnknownOpcodeException(opcode);
		}
	}

	private void runCommandGroupA(Opcode opcode) {
		I = opcode.getFirst12Bits();
		moveToNextInstruction();
	}

	private void runCommandGroupB(Opcode opcode) {
		pc = opcode.getFirst12Bits() + v[0];
	}

	private void runCommandGroupC(Opcode opcode) {
		v[opcode.getBit8To12()] = opcode.getFirst8Bits() & new Random().nextInt();
		moveToNextInstruction();
	}

	private void runCommandGroupD(Opcode opcode) {
		int[] rows = new int[opcode.getFirst4Bits()];
		System.arraycopy(memory, I, rows, 0, rows.length);
		v[0xF] = screen.drawSprite(v[opcode.getBit8To12()], v[opcode.getBit4To8()], rows) ? 1 : 0;
		moveToNextInstruction();
	}

	private void runCommandGroupE(Opcode opcode) {
		if (opcode.getFirst8Bits() == 0x9E) {
			if (keyboard.isKeyPressed(v[opcode.getBit8To12()])) {
				moveToNextInstruction();
			}
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0xA1) {
			if (!keyboard.isKeyPressed(v[opcode.getBit8To12()])) {
				moveToNextInstruction();
			}
			moveToNextInstruction();
		} else {
			throw new UnknownOpcodeException(opcode);
		}
	}

	private void runCommandGroupF(Opcode opcode) {
		if (opcode.getFirst8Bits() == 7) {
			v[opcode.getBit8To12()] = delayTimer.getTime();
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0xA) {
			for (int index = 0; index < keyboard.numberOfKeys(); index++) {
				if (keyboard.isKeyPressed(index)) {
					v[opcode.getBit8To12()] = index;
					moveToNextInstruction();
					break;
				}
			}
		} else if (opcode.getFirst8Bits() == 0x15) {
			delayTimer.setTime(v[opcode.getBit8To12()]);
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0x18) {
			soundTimer.setTime(v[opcode.getBit8To12()]);
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0x1E) {
			I += v[opcode.getBit8To12()];
			v[0xF] = I > 0xFFF ? 1 : 0;
			I &= 0xFFF;
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0x29) {
			I = v[opcode.getBit8To12()] * 5;
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0x33) {
			memory[I] = v[opcode.getBit8To12()] / 100;
			memory[I + 1] = (v[opcode.getBit8To12()] % 100) / 10;
			memory[I + 2] = v[opcode.getBit8To12()] % 10;
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0x55) {
			for (int index = 0; index < opcode.getBit8To12() + 1; index++) {
				memory[I + index] = v[index];
			}
			moveToNextInstruction();
		} else if (opcode.getFirst8Bits() == 0x65) {
			for (int index = 0; index < opcode.getBit8To12() + 1; index++) {
				v[index] = memory[I + index];
			}
			moveToNextInstruction();
		} else {
			throw new UnknownOpcodeException(opcode);
		}
	}

	private void moveToNextInstruction() {
		pc += BYTES_PER_INSTRUCTION;
	}

	private void putCharactersInMemory() {
		memory[0] = 0b1111_0000;
		memory[1] = 0b1001_0000;
		memory[2] = 0b1001_0000;
		memory[3] = 0b1001_0000;
		memory[4] = 0b1111_0000;

		memory[5] = 0b0001_0000;
		memory[6] = 0b0001_0000;
		memory[7] = 0b0001_0000;
		memory[8] = 0b0001_0000;
		memory[9] = 0b0001_0000;

		memory[10] = 0b1111_0000;
		memory[11] = 0b0001_0000;
		memory[12] = 0b1111_0000;
		memory[13] = 0b1000_0000;
		memory[14] = 0b1111_0000;

		memory[15] = 0b1111_0000;
		memory[16] = 0b0001_0000;
		memory[17] = 0b1111_0000;
		memory[18] = 0b0001_0000;
		memory[19] = 0b1111_0000;

		memory[20] = 0b1001_0000;
		memory[21] = 0b1001_0000;
		memory[22] = 0b1111_0000;
		memory[23] = 0b0001_0000;
		memory[24] = 0b0001_0000;

		memory[25] = 0b1111_0000;
		memory[26] = 0b1000_0000;
		memory[27] = 0b1111_0000;
		memory[28] = 0b0001_0000;
		memory[29] = 0b1111_0000;

		memory[30] = 0b1111_0000;
		memory[31] = 0b1000_0000;
		memory[32] = 0b1111_0000;
		memory[33] = 0b1001_0000;
		memory[34] = 0b1111_0000;

		memory[35] = 0b1111_0000;
		memory[36] = 0b0001_0000;
		memory[37] = 0b0001_0000;
		memory[38] = 0b0001_0000;
		memory[39] = 0b0001_0000;

		memory[40] = 0b1111_0000;
		memory[41] = 0b1001_0000;
		memory[42] = 0b1111_0000;
		memory[43] = 0b1001_0000;
		memory[44] = 0b1111_0000;

		memory[45] = 0b1111_0000;
		memory[46] = 0b1001_0000;
		memory[47] = 0b1111_0000;
		memory[48] = 0b0001_0000;
		memory[49] = 0b0001_0000;

		memory[50] = 0b0110_0000;
		memory[51] = 0b1001_0000;
		memory[52] = 0b1111_0000;
		memory[53] = 0b1001_0000;
		memory[54] = 0b1001_0000;

		memory[55] = 0b1110_0000;
		memory[56] = 0b1001_0000;
		memory[57] = 0b1110_0000;
		memory[58] = 0b1001_0000;
		memory[59] = 0b1110_0000;

		memory[60] = 0b0111_0000;
		memory[61] = 0b1000_0000;
		memory[62] = 0b1000_0000;
		memory[63] = 0b1000_0000;
		memory[64] = 0b0111_0000;

		memory[65] = 0b1110_0000;
		memory[66] = 0b1001_0000;
		memory[67] = 0b1001_0000;
		memory[68] = 0b1001_0000;
		memory[69] = 0b1110_0000;

		memory[70] = 0b1111_0000;
		memory[71] = 0b1000_0000;
		memory[72] = 0b1110_0000;
		memory[73] = 0b1000_0000;
		memory[74] = 0b1111_0000;

		memory[75] = 0b1111_0000;
		memory[76] = 0b1000_0000;
		memory[77] = 0b1110_0000;
		memory[78] = 0b1000_0000;
		memory[79] = 0b1000_0000;
	}

	public Timer getDelayTimer() {
		return delayTimer;
	}

	public Timer getSoundTimer() {
		return soundTimer;
	}

	public int[] getMemory() {
		return memory;
	}

	public int getPc() {
		return pc;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}

	public Screen getScreen() {
		return screen;
	}

	public Stack<Integer> getStack() {
		return stack;
	}

	public int[] getV() {
		return v;
	}

	public int getI() {
		return I;
	}

	public void setI(int I) {
		this.I = I;
	}
}
