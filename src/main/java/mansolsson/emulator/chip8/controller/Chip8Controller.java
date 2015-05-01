package mansolsson.emulator.chip8.controller;

import mansolsson.emulator.chip8.service.Chip8Service;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Chip8Controller {
    private String programPath;
    Chip8Service chip8Service;
    private EmulatorScreen screen;

    public Chip8Controller(String programPath) {
        this.programPath = programPath;
        chip8Service = new Chip8Service();
    }

    public void runProgram() throws IOException, InvocationTargetException, InterruptedException {
        byte[] program = readProgramFromFile();
        chip8Service.loadProgram(program);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                screen = new EmulatorScreen(chip8Service);
            }
        });

        boolean programRunning = true;
        while(programRunning) {
            chip8Service.executeProgramInstruction();
            if(chip8Service.shouldScreenBeUpdated()) {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        screen.repaint();
                    }
                });
            }
            if(chip8Service.shouldSoundBePlayed()) {
                // Play sound
            }
            chip8Service.setDelayTimer((byte)(chip8Service.getDelayTimer() - 1));
        }
    }

    private byte[] readProgramFromFile() throws IOException {
        return Files.readAllBytes(Paths.get(programPath));
    }
}
