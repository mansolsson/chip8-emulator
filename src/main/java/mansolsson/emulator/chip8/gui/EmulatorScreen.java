package mansolsson.emulator.chip8.gui;

import mansolsson.emulator.chip8.controller.Chip8Controller;

import javax.swing.*;

public class EmulatorScreen extends JFrame {
    private Chip8Screen chip8Screen;
    private MenuBar menuBar;
    private Chip8Controller chip8Controller;
    private Thread emulatorThread;

    public EmulatorScreen() {
        super();
        chip8Controller = new Chip8Controller(this);
        chip8Screen = new Chip8Screen(this);
        menuBar = new MenuBar(this);
        emulatorThread = null;
    }

    public void init() {
        setTitle("Chip-8 Emulator");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setJMenuBar(menuBar);
        add(chip8Screen);
        pack();
        setVisible(true);
    }

    protected void loadProgram(String path) {
        chip8Controller.setProgramPath(path);
        emulatorThread = new Thread(chip8Controller);
        emulatorThread.start();
    }

    protected byte[] getChip8Graphics() {
        return chip8Controller.getChip8Service().getGraphics() != null ? chip8Controller.getChip8Service().getGraphics() : new byte[0];
    }

    protected Chip8Controller getChip8Controller() {
        return chip8Controller;
    }
}
