package mansolsson.chip8;

public class Opcode {
	private int opcode;

	public Opcode(int opcode) {
		this.opcode = opcode;
	}

	public int getFirst4Bits() {
		return opcode & 0xF;
	}

	public int getFirst8Bits() {
		return opcode & 0xFF;
	}

	public int getFirst12Bits() {
		return opcode & 0xFFF;
	}

	public int getBit4To8() {
		return (opcode >> 4) & 0xF;
	}

	public int getBit8To12() {
		return (opcode >> 8) & 0xF;
	}

	public int getBit12To16() {
		return (opcode >> 12) & 0xF;
	}

	public int getOpcode() {
		return opcode;
	}
}
