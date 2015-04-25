package mansolsson.emulator.chip8;

public class Chip8 {
    private static final int NR_OF_REGISTERS = 16;
    private static final int MEMORY_SIZE = 4096;
    private static final int GRAPHICS_SIZE = 2048;
    private static final int STACK_SIZE = 16;
    private static final int NR_OF_KEYS = 16;

    private byte[] registers;
    private byte[] memory;

    private int addressRegister;
    private int pc;

    private byte[] graphics;

    private byte delayTimer;
    private byte soundTimer;

    private int[] stack;
    private int stackPointer;

    private int[] keys;

    private boolean redrawScreen;

    public void init() {
        initVariables();
        loadGame();
    }

    private void initVariables() {
        registers = new byte[NR_OF_REGISTERS];
        for(int i = 0; i < NR_OF_REGISTERS; i++) {
            registers[i] = 0;
        }
        memory = new byte[MEMORY_SIZE];
        for(int i = 0; i < MEMORY_SIZE; i++) {
            memory[i] = 0;
        }
        addressRegister = 0;
        pc = 0x200;
        graphics = new byte[GRAPHICS_SIZE];
        for(int i = 0; i < GRAPHICS_SIZE; i++) {
            graphics[i] = 0;
        }
        delayTimer = 0;
        soundTimer = 0;
        stack = new int[STACK_SIZE];
        for(int i = 0; i < STACK_SIZE; i++) {
            stack[i] = 0;
        }
        stackPointer = -1;
        keys = new int[NR_OF_KEYS];
        for(int i = 0; i < NR_OF_KEYS; i++) {
            keys[i] = 0;
        }
        redrawScreen = true;
    }

    private void loadGame() {
        // TODO: Load game into appropriate section of the memory
    }

    public void clearGraphics() {
        for(int i = 0; i < GRAPHICS_SIZE; i++) {
            graphics[i] = 0;
        }
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public void movePcToNextInstruction() {
        pc += 2;
    }

    public int popStack() {
        return stack[stackPointer--];
    }

    public void addToStack(int value) {
        stack[++stackPointer] = value;
    }

    public byte getRegisterAt(int index) {
        return registers[index];
    }

    public void setRegistersAt(int index, byte value) {
        registers[index] = value;
    }

    public boolean addToRegister(int registerIndex, byte value) {
        int newValue = (registers[registerIndex] & 0xFF) + (value & 0xFF);
        registers[registerIndex] = (byte)(newValue % 0x100);
        return newValue > 0xFF;
    }

    public boolean subFromRegistry(int registerIndex, byte value) {
        int newValue = (registers[registerIndex] & 0xFF) - (value & 0xFF);
        if(newValue < 0) {
            registers[registerIndex] = (byte)newValue;
        } else {
            registers[registerIndex] = (byte)newValue;
        }
        return newValue < 0;
    }

    public byte[] getGraphics() {
        return graphics;
    }

    public void setAddressRegister(int addressRegister) {
        this.addressRegister = addressRegister;
    }

    public int getAddressRegister() {
        return addressRegister;
    }

    public byte getDelayTimer() {
        return delayTimer;
    }

    public void setDelayTimer(byte delayTimer) {
        this.delayTimer = delayTimer;
    }

    public byte[] getMemory() {
        return memory;
    }

    public byte getSoundTimer() {
        return soundTimer;
    }

    public void setSoundTimer(byte soundTimer) {
        this.soundTimer = soundTimer;
    }

    public int[] getKeys() {
        return keys;
    }

    public boolean isRedrawScreen() {
        return redrawScreen;
    }

    public void setRedrawScreen(boolean redrawScreen) {
        this.redrawScreen = redrawScreen;
    }
}
