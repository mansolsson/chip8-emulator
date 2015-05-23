package mansolsson.emulator.chip8.service;

import mansolsson.emulator.chip8.model.Chip8Constants;

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
                    // Clears the screen.
                    case 0x00E0:
                        chip8Service.clearScreen();
                        chip8Service.setScreenToBeUpdated();
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Returns from a subroutine.
                    case 0x00EE:
                        chip8Service.popStackIntoPc();
                        break;
                }
                break;
            // 	Jumps to address NNN.
            case 0x1000:
                chip8Service.setPc(opcode & 0x0FFF);
                break;
            // Calls subroutine at NNN.
            case 0x2000:
                chip8Service.storePcInStack();
                chip8Service.setPc(opcode & 0x0FFF);
                break;
            // Skips the next instruction if VX equals NN.
            case 0x3000:
                if (chip8Service.registryAtEqualsValue((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF))) {
                    chip8Service.movePcToNextInstruction();
                }
                chip8Service.movePcToNextInstruction();
                break;
            //	Skips the next instruction if VX doesn't equal NN.
            case 0x4000:
                if (!chip8Service.registryAtEqualsValue((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF))) {
                    chip8Service.movePcToNextInstruction();
                }
                chip8Service.movePcToNextInstruction();
                break;
            // Skips the next instruction if VX equals VY.
            case 0x5000:
                if (chip8Service.registryAtEqualsRegistryAt((opcode & 0x0F00) >> 8, (opcode & 0x00F0) >> 4)) {
                    chip8Service.movePcToNextInstruction();
                }
                chip8Service.movePcToNextInstruction();
                break;
            // Sets VX to NN.
            case 0x6000:
                chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF));
                chip8Service.movePcToNextInstruction();
                break;
            // Adds NN to VX.
            case 0x7000:
                chip8Service.addToRegistryAt((opcode & 0x0F00) >> 8, (byte) (opcode & 0x00FF));
                chip8Service.movePcToNextInstruction();
                break;
            case 0x8000:
                switch (opcode & 0x000F) {
                    // 	Sets VX to the value of VY.
                    case 0x0000:
                        chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4));
                        chip8Service.movePcToNextInstruction();
                        break;
                    // 	Sets VX to VX or VY.
                    case 0x0001:
                        chip8Service.orRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4));
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Sets VX to VX and VY.
                    case 0x0002:
                        chip8Service.andRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4));
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Sets VX to VX xor VY.
                    case 0x0003:
                        chip8Service.xorRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4));
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Adds VY to VX. VF is set to 1 when there's a carry, and to 0 when there isn't.
                    case 0x0004:
                        if (chip8Service.addToRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4))) {
                            chip8Service.setRegistryAt(0xF, (byte) 1);
                        } else {
                            chip8Service.setRegistryAt(0xF, (byte) 0);
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    // VY is subtracted from VX. VF is set to 0 when there's a borrow, and 1 when there isn't.
                    case 0x0005:
                        if (chip8Service.subtractFromRegistry((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4))) {
                            chip8Service.setRegistryAt(0xF, (byte) 0);
                        } else {
                            chip8Service.setRegistryAt(0xF, (byte) 1);
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Shifts VX right by one. VF is set to the value of the least significant bit of VX before the shift.
                    case 0x0006:
                        chip8Service.setRegistryAt(0xF, (byte) (chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0x1));
                        chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) >> 1) & 0b01111111));
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Sets VX to VY minus VX. VF is set to 0 when there's a borrow, and 1 when there isn't.
                    case 0x0007:
                        if (chip8Service.subtractRegistryFromValue((opcode & 0x0F00) >> 8, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4))) {
                            chip8Service.setRegistryAt(0xF, (byte) 0);
                        } else {
                            chip8Service.setRegistryAt(0xF, (byte) 1);
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Shifts VX left by one. VF is set to the value of the most significant bit of VX before the shift.
                    case 0x000E:
                        chip8Service.setRegistryAt(0xF, (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) >> 7) & 0b00000001));
                        chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) << 1));
                        chip8Service.movePcToNextInstruction();
                        break;
                }
                break;
            // Skips the next instruction if VX doesn't equal VY.
            case 0x9000:
                if (!chip8Service.registryAtEqualsRegistryAt((opcode & 0x0F00) >> 8, (opcode & 0x00F0) >> 4)) {
                    chip8Service.movePcToNextInstruction();
                }
                chip8Service.movePcToNextInstruction();
                break;
            // Sets I to the address NNN.
            case 0xA000:
                chip8Service.setAddressRegister((opcode & 0x0FFF));
                chip8Service.movePcToNextInstruction();
                break;
            // Jumps to the address NNN plus V0.
            case 0xB000:
                chip8Service.setPc((opcode & 0x0FFF) + (chip8Service.getRegistryAt(0) & 0xFF));
                break;
            // Sets VX to a random number, masked by NN.
            case 0xC000:
                chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) (RANDOM.nextInt(256) & (opcode & 0x00FF)));
                chip8Service.movePcToNextInstruction();
                break;
            // Sprites stored in memory at location in index register (I), maximum 8bits wide. Wraps around the screen.
            // If when drawn, clears a pixel, register VF is set to 1 otherwise it is zero.
            // All drawing is XOR drawing (i.e. it toggles the screen pixels)
            case 0xD000:
                chip8Service.updateGraphics(chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF, chip8Service.getRegistryAt((opcode & 0x00F0) >> 4) & 0xFF, opcode & 0x000F);
                chip8Service.setScreenToBeUpdated();
                chip8Service.movePcToNextInstruction();
                break;
            case 0xE000:
                switch (opcode & 0x00FF) {
                    // Skips the next instruction if the key stored in VX is pressed.
                    case 0x009E:
                        if (chip8Service.getKey(chip8Service.getRegistryAt((opcode & 0x0F00) >> 8))) {
                            chip8Service.movePcToNextInstruction();
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Skips the next instruction if the key stored in VX isn't pressed.
                    case 0x00A1:
                        if (!chip8Service.getKey(chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xF)) {
                            chip8Service.movePcToNextInstruction();
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                }
                break;
            case 0xF000:
                switch (opcode & 0x00FF) {
                    // Sets VX to the value of the delay timer.
                    case 0x0007:
                        chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, chip8Service.getDelayTimer());
                        chip8Service.movePcToNextInstruction();
                        break;
                    // A key press is awaited, and then stored in VX.
                    case 0x000A:
                        for (int i = 0; i < Chip8Constants.NR_OF_KEYS; i++) {
                            if (chip8Service.getKey(i)) {
                                chip8Service.setRegistryAt((opcode & 0x0F00) >> 8, (byte) i);
                                chip8Service.movePcToNextInstruction();
                                break;
                            }
                        }
                        break;
                    // Sets the delay timer to VX.
                    case 0x0015:
                        chip8Service.setDelayTimer(chip8Service.getRegistryAt((opcode & 0x0F00) >> 8));
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Sets the sound timer to VX.
                    case 0x0018:
                        chip8Service.setSoundTimer(chip8Service.getRegistryAt((opcode & 0x0F00) >> 8));
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Adds VX to I.
                    case 0x001E:
                        int r = chip8Service.getAddressRegister() + (chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF);
                        if(r > 0xFFF) {
                            chip8Service.setRegistryAt(0xF, (byte) 1);
                        } else {
                            chip8Service.setRegistryAt(0xF, (byte) 0);
                        }
                        chip8Service.setAddressRegister((r & 0xFFF));
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Sets I to the location of the sprite for the character in VX.
                    // Characters 0-F (in hexadecimal) are represented by a 4x5 font.
                    case 0x0029:
                        chip8Service.setAddressRegister((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) * 5);
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Stores the Binary-coded decimal representation of VX,
                    // with the most significant of three digits at the address in I,
                    // the middle digit at I plus 1, and the least significant digit at I plus 2.
                    // (In other words, take the decimal representation of VX,
                    // place the hundreds digit in memory at location in I, the tens digit at location I+1,
                    // and the ones digit at location I+2.)
                    case 0x0033:
                        chip8Service.getMemory()[chip8Service.getAddressRegister()] = (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) / 100);
                        chip8Service.getMemory()[chip8Service.getAddressRegister() + 1] = (byte) (((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) / 10) % 10);
                        chip8Service.getMemory()[chip8Service.getAddressRegister() + 2] = (byte) ((chip8Service.getRegistryAt((opcode & 0x0F00) >> 8) & 0xFF) % 10);
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Stores V0 to VX in memory starting at address I.
                    case 0x0055:
                        byte[] memory1 = chip8Service.getMemory();
                        int addressRegister1 = chip8Service.getAddressRegister();
                        int limit1 = ((opcode & 0x0F00) >> 8);
                        for (int i = 0; i < limit1; i++) {
                            memory1[addressRegister1] = chip8Service.getRegistryAt(i);
                            addressRegister1++;
                        }
                        chip8Service.movePcToNextInstruction();
                        break;
                    // Fills V0 to VX with values from memory starting at address I.
                    case 0x0065:
                        byte[] memory = chip8Service.getMemory();
                        int addressRegister = chip8Service.getAddressRegister();
                        int limit2 = ((opcode & 0x0F00) >> 8);
                        for (int i = 0; i < limit2; i++) {
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
