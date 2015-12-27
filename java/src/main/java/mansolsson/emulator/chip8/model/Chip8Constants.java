package mansolsson.emulator.chip8.model;

public final class Chip8Constants {
    public static final int START_OF_PROGRAM_IN_MEMORY  = 0x200;
    public static final int NR_OF_REGISTERS = 16;
    public static final int MEMORY_SIZE = 4096;
    public static final int GRAPHICS_SIZE = 2048;
    public static final int STACK_SIZE = 16;
    public static final int NR_OF_KEYS = 16;
    public static final int NR_OF_BYTES_PER_INSTRUCTION = 2;

    public static final byte[] CHARACTERS = new byte[] {
            // 0
            (byte) 0b01100000,
            (byte) 0b10010000,
            (byte) 0b10010000,
            (byte) 0b10010000,
            (byte) 0b01100000,

            // 1
            (byte) 0b01100000,
            (byte) 0b01100000,
            (byte) 0b01100000,
            (byte) 0b01100000,
            (byte) 0b01100000,

            // 2
            (byte) 0b11110000,
            (byte) 0b00010000,
            (byte) 0b11110000,
            (byte) 0b10000000,
            (byte) 0b11110000,

            // 3
            (byte) 0b11110000,
            (byte) 0b00010000,
            (byte) 0b01110000,
            (byte) 0b00010000,
            (byte) 0b11110000,

            // 4
            (byte) 0b10010000,
            (byte) 0b10010000,
            (byte) 0b11110000,
            (byte) 0b00010000,
            (byte) 0b00010000,

            // 5
            (byte) 0b11110000,
            (byte) 0b10000000,
            (byte) 0b11110000,
            (byte) 0b00010000,
            (byte) 0b11110000,

            // 6
            (byte) 0b11110000,
            (byte) 0b10000000,
            (byte) 0b11110000,
            (byte) 0b10010000,
            (byte) 0b11110000,

            // 7
            (byte) 0b11110000,
            (byte) 0b00010000,
            (byte) 0b00010000,
            (byte) 0b00010000,
            (byte) 0b00010000,

            // 8
            (byte) 0b11110000,
            (byte) 0b10010000,
            (byte) 0b11110000,
            (byte) 0b10010000,
            (byte) 0b11110000,

            // 9
            (byte) 0b11110000,
            (byte) 0b10010000,
            (byte) 0b11110000,
            (byte) 0b00010000,
            (byte) 0b00010000,

            // A
            (byte) 0b01100000,
            (byte) 0b10010000,
            (byte) 0b11110000,
            (byte) 0b10010000,
            (byte) 0b10010000,

            // B
            (byte) 0b11100000,
            (byte) 0b10010000,
            (byte) 0b11100000,
            (byte) 0b10010000,
            (byte) 0b11100000,

            // C
            (byte) 0b01110000,
            (byte) 0b10000000,
            (byte) 0b10000000,
            (byte) 0b10000000,
            (byte) 0b01110000,

            // D
            (byte) 0b11100000,
            (byte) 0b10010000,
            (byte) 0b10010000,
            (byte) 0b10010000,
            (byte) 0b11100000,

            // E
            (byte) 0b11110000,
            (byte) 0b10000000,
            (byte) 0b11100000,
            (byte) 0b10000000,
            (byte) 0b11110000,

            // F
            (byte) 0b11110000,
            (byte) 0b10000000,
            (byte) 0b11100000,
            (byte) 0b10000000,
            (byte) 0b10000000
        };
}
