package mansolsson.emulator.chip8.controller;

import mansolsson.emulator.chip8.service.Chip8Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Chip8Controller {
    private String programPath;
    Chip8Service chip8Service;

    public Chip8Controller(String programPath) {
        this.programPath = programPath;
        chip8Service = new Chip8Service();
    }

    public void runProgram() throws IOException {
        byte[] program = readProgramFromFile();
        chip8Service.loadProgram(program);

        boolean programRunning = true;
        while(programRunning) {
            chip8Service.executeProgramInstruction();
            if(chip8Service.shouldScreenBeUpdated()) {
                // Update screen
            }
            if(chip8Service.shouldSoundBePlayed()) {
                // Play sound
            }
        }
    }

    private byte[] readProgramFromFile() throws IOException {
        return Files.readAllBytes(Paths.get(programPath));
    }
}
