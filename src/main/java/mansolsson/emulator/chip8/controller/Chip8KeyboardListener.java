package mansolsson.emulator.chip8.controller;

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
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_1), Integer.valueOf(0));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_2), Integer.valueOf(1));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_3), Integer.valueOf(2));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_4), Integer.valueOf(3));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_Q), Integer.valueOf(4));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_W), Integer.valueOf(5));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_E), Integer.valueOf(6));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_R), Integer.valueOf(7));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_A), Integer.valueOf(8));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_S), Integer.valueOf(9));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_D), Integer.valueOf(10));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_F), Integer.valueOf(11));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_Z), Integer.valueOf(12));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_X), Integer.valueOf(13));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_C), Integer.valueOf(14));
        KEY_MAP.put(Integer.valueOf(KeyEvent.VK_V), Integer.valueOf(15));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(KEY_MAP.containsKey(e.getKeyCode())) {
            chip8Service.getKeys()[KEY_MAP.get(e.getKeyCode())] = 1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(KEY_MAP.containsKey(e.getKeyCode())) {
            chip8Service.getKeys()[KEY_MAP.get(e.getKeyCode())] = 0;
        }
    }
}
