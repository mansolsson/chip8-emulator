package mansolsson.emulator.chip8.controller;

import mansolsson.emulator.chip8.service.Chip8Service;

import javax.swing.*;
import java.awt.*;

public class Chip8Screen extends JPanel {
    private Chip8Service chip8Service;

    public Chip8Screen(Chip8Service chip8Service) {
        super();
        this.chip8Service = chip8Service;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        byte[] graphics = chip8Service.getGraphics();
        for(int i = 0; i < graphics.length; i++) {
            g.setColor(graphics[i] == 0 ? Color.WHITE : Color.BLACK);
            g.fillRect((i % 64) * EmulatorScreen.SCALE, (i / 64) * EmulatorScreen.SCALE, EmulatorScreen.SCALE, EmulatorScreen.SCALE);
        }
    }
}
