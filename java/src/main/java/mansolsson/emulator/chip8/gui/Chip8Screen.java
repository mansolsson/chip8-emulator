package mansolsson.emulator.chip8.gui;

import javax.swing.*;
import java.awt.*;

public class Chip8Screen extends JPanel {
    public static final int SCALE = 10;

    private EmulatorScreen emulatorScreen;

    public Chip8Screen(EmulatorScreen emulatorScreen) {
        super();
        this.emulatorScreen = emulatorScreen;
        setPreferredSize(new Dimension(640, 320));
        addKeyListener(new Chip8KeyboardListener(emulatorScreen.getChip8Controller().getChip8Service()));
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        byte[] graphics = emulatorScreen.getChip8Graphics();
        for(int i = 0; i < graphics.length; i++) {
            g.setColor(graphics[i] == 0 ? Color.WHITE : Color.BLACK);
            g.fillRect((i % 64) * SCALE, (i / 64) * SCALE, SCALE, SCALE);
        }
    }
}
