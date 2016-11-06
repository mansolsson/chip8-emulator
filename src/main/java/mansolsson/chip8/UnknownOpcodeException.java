package mansolsson.chip8;

public class UnknownOpcodeException extends RuntimeException {
	private static final long serialVersionUID = 233306479089593181L;

	public UnknownOpcodeException(final Opcode opcode) {
		super("Unknown opcode 0x" + Integer.toHexString(opcode.getOpcode()));
	}
}
