package mansolsson.emulator.chip8.controller;

import mansolsson.emulator.chip8.gui.EmulatorScreen;
import mansolsson.emulator.chip8.service.Chip8Service;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class Chip8Controller implements Runnable{
    private String programPath;
    private Chip8Service chip8Service;
    private EmulatorScreen screen;

    public Chip8Controller(EmulatorScreen s) {
        this.screen = s;
        chip8Service = new Chip8Service();
    }

    public void runProgram() throws IOException, InvocationTargetException, InterruptedException {
        byte[] program = readProgramFromFile();
        chip8Service.loadProgram(program);

        long target = 1000 / 500;
        boolean programRunning = true;
        while(programRunning) {
            Date before = new Date();

            chip8Service.executeProgramInstruction();
            if(chip8Service.shouldScreenBeUpdated()) {
                SwingUtilities.invokeLater(screen::repaint);
                chip8Service.setScreenUpdated();
            }
            if(chip8Service.shouldSoundBePlayed()) {
                // Play sound
            }
            chip8Service.setDelayTimer((byte)(chip8Service.getDelayTimer() - 1));

            Date after = new Date();

            long milliseconds = after.getTime() - before.getTime();
            long timeToWait = target - milliseconds;

            if(timeToWait > 0) {
                Thread.sleep(timeToWait);
            }
        }
    }

    private byte[] readProgramFromFile() throws IOException {
        return Files.readAllBytes(Paths.get(programPath));
    }

    public Chip8Service getChip8Service() {
        return chip8Service;
    }

    @Override
    public void run() {
        try {
            runProgram();
        } catch (IOException | InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setProgramPath(String programPath) {
        this.programPath = programPath;
    }
}
