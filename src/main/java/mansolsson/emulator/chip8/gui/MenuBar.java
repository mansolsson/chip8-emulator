package mansolsson.emulator.chip8.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {
    public MenuBar(EmulatorScreen emulatorScreen) {
        super();
        JMenu file = new JMenu("File");
        JMenuItem item = new JMenuItem("Open");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int returnValue = chooser.showOpenDialog(emulatorScreen);
                if(returnValue == JFileChooser.APPROVE_OPTION) {
                    emulatorScreen.loadProgram(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        file.add(item);
        this.add(file);
    }
}
