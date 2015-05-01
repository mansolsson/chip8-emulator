package mansolsson.emulator.chip8;

import mansolsson.emulator.chip8.controller.Chip8Controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Application {
    public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {
        Chip8Controller chip8Controller = new Chip8Controller("/home/mans/Downloads/pong1.ch8");
        chip8Controller.runProgram();
    }
}
