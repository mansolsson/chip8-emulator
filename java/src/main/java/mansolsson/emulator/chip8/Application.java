package mansolsson.emulator.chip8;

import mansolsson.emulator.chip8.gui.EmulatorScreen;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Application {
    public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {
        SwingUtilities.invokeLater(() -> {
            EmulatorScreen emulatorScreen = new EmulatorScreen();
            emulatorScreen.init();
        });
    }
}
