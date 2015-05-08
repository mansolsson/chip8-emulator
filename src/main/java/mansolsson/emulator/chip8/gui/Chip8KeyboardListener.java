package mansolsson.emulator.chip8.gui;

import mansolsson.emulator.chip8.service.Chip8Service;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Chip8KeyboardListener implements KeyListener {
    private Chip8Service chip8Service;

    public Chip8KeyboardListener(Chip8Service chip8Service) {
        this.chip8Service = chip8Service;
    }

    private static Map<Integer, Integer> KEY_MAP = new HashMap<>();
    static {
        KEY_MAP.put(KeyEvent.VK_1, 0);
        KEY_MAP.put(KeyEvent.VK_2, 1);
        KEY_MAP.put(KeyEvent.VK_3, 2);
        KEY_MAP.put(KeyEvent.VK_4, 3);
        KEY_MAP.put(KeyEvent.VK_Q, 4);
        KEY_MAP.put(KeyEvent.VK_W, 5);
        KEY_MAP.put(KeyEvent.VK_E, 6);
        KEY_MAP.put(KeyEvent.VK_R, 7);
        KEY_MAP.put(KeyEvent.VK_A, 8);
        KEY_MAP.put(KeyEvent.VK_S, 9);
        KEY_MAP.put(KeyEvent.VK_D, 10);
        KEY_MAP.put(KeyEvent.VK_F, 11);
        KEY_MAP.put(KeyEvent.VK_Z, 12);
        KEY_MAP.put(KeyEvent.VK_X, 13);
        KEY_MAP.put(KeyEvent.VK_C, 14);
        KEY_MAP.put(KeyEvent.VK_V, 15);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(KEY_MAP.containsKey(e.getKeyCode())) {
            chip8Service.setKey(KEY_MAP.get(e.getKeyCode()), 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(KEY_MAP.containsKey(e.getKeyCode())) {
            chip8Service.setKey(KEY_MAP.get(e.getKeyCode()), 0);
        }
    }
}
