package mansolsson.emulator.chip8.controller;

import mansolsson.emulator.chip8.service.Chip8Service;

import javax.swing.*;

public class EmulatorScreen extends JFrame {
    public static final int SCALE = 10;

    public EmulatorScreen(Chip8Service chip8Service) {
        super();
        this.setTitle("Chip-8 Emulator");
        this.add(new Chip8Screen(chip8Service));
        this.setSize(64 * SCALE, 32 * SCALE);
        this.addKeyListener(new Chip8KeyboardListener(chip8Service));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
