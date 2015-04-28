package mansolsson.emulator.chip8.model;

public class Chip8 {
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
    private boolean playSound;

    public byte[] getRegisters() {
        return registers;
    }

    public void setRegisters(byte[] registers) {
        this.registers = registers;
    }

    public byte[] getMemory() {
        return memory;
    }

    public void setMemory(byte[] memory) {
        this.memory = memory;
    }

    public int getAddressRegister() {
        return addressRegister;
    }

    public void setAddressRegister(int addressRegister) {
        this.addressRegister = addressRegister;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public byte[] getGraphics() {
        return graphics;
    }

    public void setGraphics(byte[] graphics) {
        this.graphics = graphics;
    }

    public byte getDelayTimer() {
        return delayTimer;
    }

    public void setDelayTimer(byte delayTimer) {
        this.delayTimer = delayTimer;
    }

    public byte getSoundTimer() {
        return soundTimer;
    }

    public void setSoundTimer(byte soundTimer) {
        this.soundTimer = soundTimer;
    }

    public int[] getStack() {
        return stack;
    }

    public void setStack(int[] stack) {
        this.stack = stack;
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public void setStackPointer(int stackPointer) {
        this.stackPointer = stackPointer;
    }

    public int[] getKeys() {
        return keys;
    }

    public void setKeys(int[] keys) {
        this.keys = keys;
    }

    public boolean isRedrawScreen() {
        return redrawScreen;
    }

    public void setRedrawScreen(boolean redrawScreen) {
        this.redrawScreen = redrawScreen;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }
}
