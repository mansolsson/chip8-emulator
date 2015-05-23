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
        System.arraycopy(Chip8Constants.CHARACTERS, 0, chip8.getMemory(), 0, Chip8Constants.CHARACTERS.length);
        System.arraycopy(program, 0, chip8.getMemory(), Chip8Constants.START_OF_PROGRAM_IN_MEMORY, program.length);
    }

    private void initializeChip8() {
        chip8.setRegisters(new byte[Chip8Constants.NR_OF_REGISTERS]);
        chip8.setMemory(new byte[Chip8Constants.MEMORY_SIZE]);
        chip8.setAddressRegister(0);
        chip8.setPc(Chip8Constants.START_OF_PROGRAM_IN_MEMORY);
        chip8.setGraphics(new byte[Chip8Constants.GRAPHICS_SIZE]);
        chip8.setDelayTimer((byte) 0);
        chip8.setSoundTimer((byte) 0);
        chip8.setStack(new int[Chip8Constants.STACK_SIZE]);
        chip8.setStackPointer(-1);
        chip8.setKeys(new boolean[Chip8Constants.NR_OF_KEYS]);
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
        int currentStackPointer = chip8.getStackPointer();
        chip8.setPc(stack[currentStackPointer]);
        chip8.setStackPointer(currentStackPointer - 1);
    }

    public void storePcInStack() {
        int newStackPointer = chip8.getStackPointer() + 1;
        chip8.getStack()[newStackPointer] = chip8.getPc() + Chip8Constants.NR_OF_BYTES_PER_INSTRUCTION;
        chip8.setStackPointer(newStackPointer);
    }

    public void setPc(int pc) {
        chip8.setPc(pc);
    }

    public boolean registryAtEqualsValue(int registryIndex, byte value) {
        return chip8.getRegisters()[registryIndex] == value;
    }

    public boolean registryAtEqualsRegistryAt(int registryIndex1, int registryIndex2) {
        byte[] registers = chip8.getRegisters();
        return registers[registryIndex1] == registers[registryIndex2];
    }

    public void setRegistryAt(int registryIndex, byte value) {
        chip8.getRegisters()[registryIndex] = value;
    }

    public boolean addToRegistryAt(int registryIndex, byte value) {
        byte[] registers = chip8.getRegisters();
        boolean carry = false;
        int sum = (registers[registryIndex] & 0xFF) + (value & 0xFF);
        if (sum > 0xFF) {
            carry = true;
        }
        registers[registryIndex] = (byte) sum;
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
        byte[] registers = chip8.getRegisters();
        boolean borrow = false;
        int result = (registers[registryIndex] & 0xFF) - (value & 0xFF);
        if (result < 0) {
            borrow = true;
            result = 0xFF - (result * -1) + 1;
        }
        registers[registryIndex] = (byte) result;
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
        byte[] graphics = chip8.getGraphics();
        return graphics != null ? graphics : new byte[0];
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

    public synchronized boolean getKey(int index) {
        boolean[] keys = chip8.getKeys();
        return keys != null && keys[index];
    }

    public synchronized void setKey(int index, boolean value) {
        boolean[] keys = chip8.getKeys();
        if(keys != null) {
            keys[index] = value;
        }
    }

    public boolean subtractRegistryFromValue(int registryIndex, byte value) {
        byte[] registers = chip8.getRegisters();
        int registerValue = registers[registryIndex] & 0xFF;
        boolean borrow = registerValue > (value & 0xFF);
        int result = (value | 0x100) - registerValue;
        registers[registryIndex] = (byte) (result & 0xFF);
        return borrow;
    }

    public synchronized void updateGraphics(int x, int y, int height) {
        boolean carryFlag = false;
        byte[] graphics = getGraphics();
        for(int rowIndex = 0; rowIndex < height; rowIndex++) {
            int memoryRow = getMemory()[getAddressRegister() + rowIndex] & 0xFF;
            for(int columnIndex = 0; columnIndex < 8; columnIndex++) {
                int index = (x + columnIndex) % 64 + (rowIndex + y) * 64;
                // TODO: Probably a bug making it possible for the index to be out of range
                if(index > 2047) {
                    continue;
                }
                if((memoryRow & (0b10000000 >> columnIndex)) != 0) {
                    if(graphics[index] == 1) {
                        carryFlag = true;
                    }
                    graphics[index] ^= 1;
                }
            }
        }
        setRegistryAt(0xF, carryFlag ? (byte) 1 : (byte) 0);
    }
}
