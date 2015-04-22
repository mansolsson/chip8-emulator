package mansolsson.emulator.chip8;

import java.util.Random;

public final class OpCodeRunner {
    private OpCodeRunner() {
    }

    private static final Random RANDOM = new Random();

    public static void executeOpcode(int opcode, Chip8 chip8) {
        switch (opcode & 0xF000) {
            case 0x0000:
                switch (opcode) {
                    case 0x00E0:
                        chip8.clearGraphics();
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x00EE:
                        chip8.setPc(chip8.popStack());
                        break;
                }
                break;
            case 0x1000:
                chip8.setPc(opcode & 0x0FFF);
                break;
            case 0x2000:
                chip8.addToStack(chip8.getPc());
                chip8.setPc(opcode & 0x0FFF);
                break;
            case 0x3000:
                if (chip8.getRegisterAt((opcode & 0x0F00) >> 8) == (opcode & 0x00FF)) {
                    chip8.movePcToNextInstruction();
                }
                chip8.movePcToNextInstruction();
                break;
            case 0x4000:
                if (chip8.getRegisterAt((opcode & 0x0F00) >> 8) != (opcode & 0x00FF)) {
                    chip8.movePcToNextInstruction();
                }
                chip8.movePcToNextInstruction();
                break;
            case 0x5000:
                if (chip8.getRegisterAt((opcode & 0x0F00) >> 8) == chip8.getRegisterAt((opcode & 0x00F0) >> 4)) {
                    chip8.movePcToNextInstruction();
                }
                chip8.movePcToNextInstruction();
                break;
            case 0x6000:
                chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF));
                chip8.movePcToNextInstruction();
                break;
            case 0x7000:
                chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) + (byte) (opcode & 0x00FF)));
                chip8.movePcToNextInstruction();
                break;
            case 0x8000:
                switch (opcode & 0x000F) {
                    case 0x0000:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, chip8.getRegisterAt((opcode & 0x00F0) >> 4));
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0001:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) | chip8.getRegisterAt((opcode & 0x00F0) >> 4)));
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0002:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) & chip8.getRegisterAt((opcode & 0x00F0) >> 4)));
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0003:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) ((chip8.getRegisterAt((opcode & 0x0F00) >> 8)) ^ ((chip8.getRegisterAt((opcode & 0x00F0) >> 4)))));
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0004:
                        if (chip8.addToRegister((opcode & 0x0F00) >> 8, chip8.getRegisterAt((opcode & 0x00F0) >> 4))) {
                            chip8.setRegistersAt(0xF, (byte) 1);
                        } else {
                            chip8.setRegistersAt(0xF, (byte) 0);
                        }
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0005:
                        if (chip8.subFromRegistry((opcode & 0x0F00) >> 8, chip8.getRegisterAt((opcode & 0x00F0) >> 4))) {
                            chip8.setRegistersAt(0xF, (byte) 0);
                        } else {
                            chip8.setRegistersAt(0xF, (byte) 1);
                        }
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0006:
                        chip8.setRegistersAt(0xF, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) & 0x1));
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) ((chip8.getRegisterAt((opcode & 0x0F00) >> 8) >> 1) & 0b01111111));
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0007:
                        byte temp = chip8.getRegisterAt((opcode & 0x00F0) >> 4);
                        if (chip8.subFromRegistry((opcode & 0x00F0) >> 4, chip8.getRegisterAt((opcode & 0x0F00) >> 8))) {
                            chip8.setRegistersAt(0xF, (byte) 0);
                        } else {
                            chip8.setRegistersAt(0xF, (byte) 1);
                        }
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, chip8.getRegisterAt((opcode & 0x00F0) >> 4));
                        chip8.setRegistersAt((opcode & 0x00F0) >> 4, temp);
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x000E:
                        chip8.setRegistersAt(0xF, (byte) ((chip8.getRegisterAt((opcode & 0x0F00) >> 8) >> 7) & 0b00000001));
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) << 1));
                        chip8.movePcToNextInstruction();
                        break;
                }
                break;
            case 0x9000:
                if (chip8.getRegisterAt((opcode & 0x0F00) >> 8) != chip8.getRegisterAt((opcode & 0x00F0) >> 4)) {
                    chip8.movePcToNextInstruction();
                }
                chip8.movePcToNextInstruction();
                break;
            case 0xA000:
                chip8.setAddressRegister((opcode & 0x0FFF));
                chip8.movePcToNextInstruction();
                break;
            case 0xB000:
                chip8.setPc((opcode & 0x0FFF) + chip8.getRegisterAt(0));
                break;
            case 0xC000:
                chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte)(RANDOM.nextInt(256) & (opcode & 0x00FF)));
                chip8.movePcToNextInstruction();
                break;
            case 0xD000:
                byte x = chip8.getRegisterAt((opcode & 0x0F00) >> 8);
                byte y = chip8.getRegisterAt((opcode & 0x00F0) >> 4);
                int height = opcode & 0x000F;
                int pixel;

                chip8.setRegistersAt(0xF, (byte)0);
                for (int yline = 0; yline < height; yline++)
                {
                    pixel = chip8.getMemory()[chip8.getAddressRegister() + yline];
                    for(int xline = 0; xline < 8; xline++)
                    {
                        if((pixel & (0x80 >> xline)) != 0)
                        {
                            if(chip8.getGraphics()[(x + xline + ((y + yline) * 64))] == 1)
                            {
                                chip8.setRegistersAt(0xF, (byte)1);
                            }
                            chip8.getGraphics()[x + xline + ((y + yline) * 64)] ^= 1;
                        }
                    }
                }
                chip8.movePcToNextInstruction();
                break;
            case 0xE000:
                switch (opcode & 0x00FF) {
                    case 0x009E:
                        if(chip8.getKeys()[chip8.getRegisterAt((opcode & 0x0F00) >> 8)] == 1) {
                            chip8.movePcToNextInstruction();
                        }
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x00A1:
                        if(chip8.getKeys()[chip8.getRegisterAt((opcode & 0x0F00) >> 8)] == 0) {
                            chip8.movePcToNextInstruction();
                        }
                        chip8.movePcToNextInstruction();
                        break;
                }
                break;
            case 0xF000:
                switch (opcode & 0x00FF) {
                    case 0x0007:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, chip8.getDelayTimer());
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x000A:
                        for(int i = 0; i < chip8.getKeys().length; i++) {
                            if(chip8.getKeys()[i] == 1) {
                                chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) i);
                                chip8.movePcToNextInstruction();
                                break;
                            }
                        }
                        break;
                    case 0x0015:
                        chip8.setDelayTimer(chip8.getRegisterAt((opcode & 0x0F00) >> 8));
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0018:
                        chip8.setSoundTimer(chip8.getRegisterAt((opcode & 0x0F00) >> 8));
                        break;
                    case 0x001E:
                        chip8.setAddressRegister(chip8.getAddressRegister() + chip8.getRegisterAt((opcode & 0x0F00) >> 8));
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0029:
                        chip8.setAddressRegister(chip8.getRegisterAt((opcode & 0x0F00) >> 8) * 5);
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0033:
                        chip8.getMemory()[chip8.getAddressRegister()] = (byte)(chip8.getRegisterAt((opcode & 0x0F00) >> 8) / 100);
                        chip8.getMemory()[chip8.getAddressRegister() + 1] = (byte)((chip8.getRegisterAt((opcode & 0x0F00) >> 8) / 10) % 10);
                        chip8.getMemory()[chip8.getAddressRegister() + 2] = (byte)(chip8.getRegisterAt((opcode & 0x0F00) >> 8) % 10);
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0055:
                        byte[] memory1 = chip8.getMemory();
                        int addressRegister1 = chip8.getAddressRegister();
                        for(int i = 0; i < 0xF; i++) {
                            memory1[addressRegister1] = chip8.getRegisterAt(i);
                            addressRegister1++;
                        }
                        chip8.movePcToNextInstruction();
                        break;
                    case 0x0065:
                        byte[] memory = chip8.getMemory();
                        int addressRegister = chip8.getAddressRegister();
                        for(int i = 0; i < 0xF; i++) {
                            chip8.setRegistersAt(i, memory[addressRegister]);
                            addressRegister++;
                        }
                        chip8.movePcToNextInstruction();
                        break;
                }
                break;
        }
    }
}
