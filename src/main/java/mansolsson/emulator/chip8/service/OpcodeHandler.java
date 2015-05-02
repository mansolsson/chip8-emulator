package mansolsson.emulator.chip8.service;

import java.util.Random;

public class OpcodeHandler {
    private static final Random RANDOM = new Random();
    private Chip8Service chip8Service;

    public OpcodeHandler(Chip8Service chip8Service) {
        this.chip8Service = chip8Service;
    }

    public void executeOpcode(int opcode) {
        switch (opcode & 0xF000) {
            case 0x0000:
                switch (opcode) {
                    case 0x00E0:
                        chip8Service.clearScreen();
                        chip8Service.setScreenToBeUpdated();
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x00EE:
                        chip8Service.popStackIntoPc();
                        chip8Service.movePcToNextInstruction();
                        break;
                }
                break;
            case 0x1000:
                chip8Service.setPc(opcode & 0x0FFF);
                break;
            case 0x2000:
                chip8Service.storePcInStack();
                chip8Service.setPc(opcode & 0x0FFF);
                break;
            case 0x3000:
                if (chip8Service.registryAtEqualsValue((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF))) {
                    chip8Service.movePcToNextInstruction();
                }
                chip8Service.movePcToNextInstruction();
                break;
            case 0x4000:
                if (!chip8Service.registryAtEqualsValue((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF))) {
                    chip8Service.movePcToNextInstruction();
                }
                chip8Service.movePcToNextInstruction();
                break;
            case 0x5000:
                if (chip8Service.registryAtEqualsRegistryAt((opcode & 0x0F00) >> 8, (opcode & 0x00F0) >> 4)) {
                    chip8Service.movePcToNextInstruction();
                }
                chip8Service.movePcToNextInstruction();
                break;
            case 0x6000:
                chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF));
                chip8Service.movePcToNextInstruction();
                break;
            case 0x7000:
                chip8Service.addToRegistryAt((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF));
                chip8Service.movePcToNextInstruction();
                break;
            case 0x8000:
                switch (opcode & 0x000F) {
                    case 0x0000:
                        chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4));
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0001:
                        chip8Service.orRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4));
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0002:
                        chip8Service.andRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4));
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0003:
                        chip8Service.xorRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4));
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0004:
                        if (chip8Service.addToRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4))) {
                            chip8Service.setRegistryAt(0xF, (byte) 1);
                        } else {
                            chip8Service.setRegistryAt(0xF, (byte) 0);
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0005:
                        if (chip8Service.subtractFromRegistry((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4))) {
                            chip8Service.setRegistryAt(0xF, (byte) 0);
                        } else {
                            chip8Service.setRegistryAt(0xF, (byte) 1);
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0006:
                        chip8Service.setRegistryAt(0xF, (byte) (chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0x1));
                        chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) >> 1) & 0b01111111));
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0007:
                        if (chip8Service.subtractRegistryFromValue((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4))) {
                            chip8Service.setRegistryAt(0xF, (byte) 0);
                        } else {
                            chip8Service.setRegistryAt(0xF, (byte) 1);
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x000E:
                        chip8Service.setRegistryAt(0xF, (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) >> 7) & 0b00000001));
                        chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) << 1));
                        chip8Service.movePcToNextInstruction();
                        break;
                }
                break;
            case 0x9000:
                if (!chip8Service.registryAtEqualsRegistryAt((opcode & 0x0F00) >> 8, (opcode & 0x00F0) >> 4)) {
                    chip8Service.movePcToNextInstruction();
                }
                chip8Service.movePcToNextInstruction();
                break;
            case 0xA000:
                chip8Service.setAddressRegister((opcode & 0x0FFF));
                chip8Service.movePcToNextInstruction();
                break;
            case 0xB000:
                chip8Service.setPc((opcode & 0x0FFF) + (chip8Service.getRegistryAt(0) & 0xFF));
                break;
            case 0xC000:
                chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) (RANDOM.nextInt(256) & (opcode & 0x00FF)));
                chip8Service.movePcToNextInstruction();
                break;
            case 0xD000:
                chip8Service.updateGraphics(chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4) & 0xFF, opcode & 0x000F);
                chip8Service.setScreenToBeUpdated();
                chip8Service.movePcToNextInstruction();
                break;
            case 0xE000:
                switch (opcode & 0x00FF) {
                    case 0x009E:
                        if (chip8Service.getKeys()[chip8Service.getRegistryAt((opcode & 0x0F00) >> 8)] == 1) {
                            chip8Service.movePcToNextInstruction();
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x00A1:
                        if (chip8Service.getKeys()[chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xF] == 0) {
                            chip8Service.movePcToNextInstruction();
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                }
                break;
            case 0xF000:
                switch (opcode & 0x00FF) {
                    case 0x0007:
                        chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getDelayTimer());
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x000A:
                        int length = chip8Service.getKeys().length;
                        for (int i = 0; i < length; i++) {
                            if (chip8Service.getKeys()[i] == 1) {
                                chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) i);
                                chip8Service.movePcToNextInstruction();
                                break;
                            }
                        }
                        break;
                    case 0x0015:
                        chip8Service.setDelayTimer(chip8Service.getRegistryAt((opcode & 0x0F00) >> 8));
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0018:
                        chip8Service.setSoundTimer(chip8Service.getRegistryAt((opcode & 0x0F00) >> 8));
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x001E:
                        chip8Service.setAddressRegister(chip8Service.getAddressRegister() + (chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF));
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0029:
                        chip8Service.setAddressRegister((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) * 5);
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0033:
                        chip8Service.getMemory()[chip8Service.getAddressRegister()] = (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) / 100);
                        chip8Service.getMemory()[chip8Service.getAddressRegister() + 1] = (byte) (((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) / 10) % 10);
                        chip8Service.getMemory()[chip8Service.getAddressRegister() + 2] = (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) % 10);
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0055:
                        byte[] memory1 = chip8Service.getMemory();
                        int addressRegister1 = chip8Service.getAddressRegister();
                        for (int i = 0; i < 0xF; i++) {
                            memory1[addressRegister1] = chip8Service.getRegistryAt(i);
                            addressRegister1++;
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    case 0x0065:
                        byte[] memory = chip8Service.getMemory();
                        int addressRegister = chip8Service.getAddressRegister();
                        for (int i = 0; i < 0xF; i++) {
                            chip8Service.setRegistryAt(i, memory[addressRegister]);
                            addressRegister++;
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                }
                break;
        }
    }
}
