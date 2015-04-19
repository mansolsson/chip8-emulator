package mansolsson.emulator.chip8;

public final class OpCodeRunner {
    private OpCodeRunner() {
    }

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
                break;
            case 0x7000:
                chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) + (byte) (opcode & 0x00FF)));
                break;
            case 0x8000:
                switch (opcode & 0x000F) {
                    case 0x0000:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, chip8.getRegisterAt((opcode & 0x00F0) >> 4));
                        break;
                    case 0x0001:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) | chip8.getRegisterAt((opcode & 0x00F0) >> 4)));
                        break;
                    case 0x0002:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) & chip8.getRegisterAt((opcode & 0x00F0) >> 4)));
                        break;
                    case 0x0003:
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) ((chip8.getRegisterAt((opcode & 0x0F00) >> 8)) ^ ((chip8.getRegisterAt((opcode & 0x00F0) >> 4)))));
                        break;
                    case 0x0004:
                        if (chip8.addToRegister((opcode & 0x0F00) >> 8, chip8.getRegisterAt((opcode & 0x00F0) >> 4))) {
                            chip8.setRegistersAt(0xF, (byte) 1);
                        } else {
                            chip8.setRegistersAt(0xF, (byte) 0);
                        }
                        break;
                    case 0x0005:
                        if (chip8.subFromRegistry((opcode & 0x0F00) >> 8, chip8.getRegisterAt((opcode & 0x00F0) >> 4))) {
                            chip8.setRegistersAt(0xF, (byte) 0);
                        } else {
                            chip8.setRegistersAt(0xF, (byte) 1);
                        }
                        break;
                    case 0x0006:
                        chip8.setRegistersAt(0xF, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) & 0x1));
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) << 1));
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
                        break;
                    case 0x000E:
                        // TODO: Fix opcode
                        /*chip8.setRegistersAt(0xF, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) & 0x10000000));
                        chip8.setRegistersAt((opcode & 0x0F00) >> 8, (byte) (chip8.getRegisterAt((opcode & 0x0F00) >> 8) >>> 1));*/
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
                chip8.setI((opcode & 0x0FFF));
                break;
            case 0xB000:
                chip8.setPc((opcode & 0x0FFF) + chip8.getRegisterAt(0));
                break;
            case 0xC000:
                break;
            case 0xD000:
                break;
            case 0xE000:
                switch (opcode & 0x00FF) {
                    case 0x009E:
                        break;
                    case 0x00A1:
                        break;
                }
                break;
            case 0xF000:
                switch (opcode & 0x00FF) {
                    case 0x0007:
                        break;
                    case 0x000A:
                        break;
                    case 0x0015:
                        break;
                    case 0x0018:
                        break;
                    case 0x001E:
                        break;
                    case 0x0029:
                        break;
                    case 0x0033:
                        break;
                    case 0x0055:
                        break;
                    case 0x0065:
                        break;
                }
                break;
        }
    }
}
