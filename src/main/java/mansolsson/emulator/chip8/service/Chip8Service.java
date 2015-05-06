package mansolsson.emulator.chip8.service;

import mansolsson.emulator.chip8.model.Chip8;
import mansolsson.emulator.chip8.model.Chip8Constants;

public class Chip8Service {
    private Chip8 chip8;
    private OpcodeHandler opcodeHandler;

    public Chip8Service() {
        chip8 = new Chip8();
        opcodeHandler = new OpcodeHandler(this);
    }

    public void loadProgram(byte[] program) {
        initializeChip8();
        int memoryLocation = Chip8Constants.START_OF_PROGRAM_IN_MEMORY;
        byte[] memory = chip8.getMemory();
        for (int i = 0; i < program.length; i++) {
            memory[memoryLocation + i] = program[i];
        }
    }

    private void initializeChip8() {
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        for (int i = 0; i < registers.length; i++) {
            registers[i] = 0;
        }
        chip8.setRegisters(registers);

        byte[] memory = new byte[Chip8Constants.MEMORY_SIZE];
        for (int i = 0; i < memory.length; i++) {
            memory[i] = 0;
        }
        chip8.setMemory(memory);

        chip8.setAddressRegister(0);
        chip8.setPc(Chip8Constants.START_OF_PROGRAM_IN_MEMORY);

        byte[] graphics = new byte[Chip8Constants.GRAPHICS_SIZE];
        for (int i = 0; i < graphics.length; i++) {
            graphics[i] = 0;
        }
        chip8.setGraphics(graphics);

        chip8.setDelayTimer((byte) 0);
        chip8.setSoundTimer((byte) 0);

        int[] stack = new int[Chip8Constants.STACK_SIZE];
        for (int i = 0; i < stack.length; i++) {
            stack[i] = 0;
        }
        chip8.setStack(stack);

        chip8.setStackPointer(-1);

        int[] keys = new int[Chip8Constants.NR_OF_KEYS];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = 0;
        }
        chip8.setKeys(keys);

        chip8.setRedrawScreen(true);
        chip8.setPlaySound(false);
    }

    public void executeProgramInstruction() {
        int opcode = getCurrentOpcode();
        opcodeHandler.executeOpcode(opcode);
    }

    private int getCurrentOpcode() {
        return ((chip8.getMemory()[chip8.getPc()] << 8) & 0xFFFF) | (chip8.getMemory()[chip8.getPc() + 1] & 0xFF);
    }

    public boolean shouldScreenBeUpdated() {
        return chip8.isRedrawScreen();
    }

    public boolean shouldSoundBePlayed() {
        return chip8.isPlaySound();
    }

    public void movePcToNextInstruction() {
        chip8.setPc(chip8.getPc() + Chip8Constants.NR_OF_BYTES_PER_INSTRUCTION);
    }

    public synchronized void clearScreen() {
        byte[] graphics = chip8.getGraphics();
        for (int i = 0; i < graphics.length; i++) {
            graphics[i] = 0;
        }
    }

    public void setScreenToBeUpdated() {
        chip8.setRedrawScreen(true);
    }

    public void setScreenUpdated() {
        chip8.setRedrawScreen(false);
    }

    public void popStackIntoPc() {
        int[] stack = chip8.getStack();
        chip8.setPc(stack[chip8.getStackPointer()]);
        chip8.setStackPointer(chip8.getStackPointer() - 1);
    }

    public void storePcInStack() {
        int newStackPointer = chip8.getStackPointer() + 1;
        chip8.getStack()[newStackPointer] = chip8.getPc();
        chip8.setStackPointer(newStackPointer);
    }

    public void setPc(int pc) {
        chip8.setPc(pc);
    }

    public boolean registryAtEqualsValue(int registryIndex, byte value) {
        return chip8.getRegisters()[registryIndex] == value;
    }

    public boolean registryAtEqualsRegistryAt(int registryIndex1, int registryIndex2) {
        return chip8.getRegisters()[registryIndex1] == chip8.getRegisters()[registryIndex2];
    }

    public void setRegistryAt(int registryIndex, byte value) {
        chip8.getRegisters()[registryIndex] = value;
    }

    public boolean addToRegistryAt(int registryIndex, byte value) {
        boolean carry = false;
        int sum = (chip8.getRegisters()[registryIndex] & 0xFF) + (value & 0xFF);
        if (sum > 0xFF) {
            carry = true;
        }
        chip8.getRegisters()[registryIndex] = (byte) sum;
        return carry;
    }

    public byte getRegistryAt(int registryIndex) {
        return chip8.getRegisters()[registryIndex];
    }

    public void orRegistryAt(int registryIndex, byte value) {
        chip8.getRegisters()[registryIndex] |= value;
    }

    public void andRegistryAt(int registryIndex, byte value) {
        chip8.getRegisters()[registryIndex] &= value;
    }

    public void xorRegistryAt(int registryIndex, byte value) {
        chip8.getRegisters()[registryIndex] ^= value;
    }

    public boolean subtractFromRegistry(int registryIndex, byte value) {
        boolean borrow = false;
        int result = (chip8.getRegisters()[registryIndex] & 0xFF) - (value & 0xFF);
        if (result < 0) {
            borrow = true;
            result = 0xFF - (result * -1) - 1;
        }
        chip8.getRegisters()[registryIndex] = (byte) result;
        return borrow;
    }

    public void setAddressRegister(int value) {
        chip8.setAddressRegister(value);
    }

    public int getAddressRegister() {
        return chip8.getAddressRegister();
    }

    public byte[] getMemory() {
        return chip8.getMemory();
    }

    public synchronized byte[] getGraphics() {
        return chip8.getGraphics() != null ? chip8.getGraphics() : new byte[0];
    }

    public byte getDelayTimer() {
        return chip8.getDelayTimer();
    }

    public void setDelayTimer(byte value) {
        chip8.setDelayTimer(value);
    }

    public void setSoundTimer(byte value) {
        chip8.setSoundTimer(value);
    }

    public synchronized int getKey(int index) {
        if(chip8.getKeys() != null) {
            return chip8.getKeys()[index];
        }
        return 0;
    }

    public synchronized void setKey(int index, int value) {
        if(chip8.getKeys() != null) {
            chip8.getKeys()[index] = value;
        }
    }

    public boolean subtractRegistryFromValue(int registryIndex, byte value) {
        int registerValue = chip8.getRegisters()[registryIndex] & 0xFF;
        boolean borrow = registerValue > (value & 0xFF);
        int result = (value | 0x100) - registerValue;
        chip8.getRegisters()[registryIndex] = (byte) (result & 0xFF);
        return borrow;
    }

    public synchronized void updateGraphics(int x, int y, int height) {
        int pixel;
        setRegistryAt(0xF, (byte) 0);
        for (int yline = 0; yline < height; yline++) {
            pixel = getMemory()[getAddressRegister() + yline] & 0xFF;
            for (int xline = 0; xline < 8; xline++) {
                if ((pixel & (0x80 >> xline)) != 0) {
                    int index = x + xline + ((y + yline) * 64);
                    if (index > 2047) {
                        index = 2047;
                    }
                    if (getGraphics()[index] == 1) {
                        setRegistryAt(0xF, (byte) 1);
                    }
                    index = x + xline + ((y + yline) * 64);
                    if (index > 2047) {
                        index = 2047;
                    }
                    getGraphics()[index] ^= 1;
                }
            }
        }
    }
}
