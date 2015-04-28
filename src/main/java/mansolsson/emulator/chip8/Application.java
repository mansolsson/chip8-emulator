package mansolsson.emulator.chip8;

import mansolsson.emulator.chip8.controller.Chip8Controller;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        Chip8Controller chip8Controller = new Chip8Controller(args[1]);
        chip8Controller.runProgram();
    }
}
