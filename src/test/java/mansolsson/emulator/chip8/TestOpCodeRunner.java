package mansolsson.emulator.chip8;

import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestOpCodeRunner {
    private Chip8 chip8;

    @Before
    public void prepare() {
        chip8 = new Chip8();
        chip8.init();
    }

    @Test
    public void testOpcode00E0() {
        byte[] graphics = chip8.getGraphics();
        for (int i = 0; i < graphics.length; i++) {
            graphics[i] = 1;
        }

        OpCodeRunner.executeOpcode(0x00E0, chip8);

        for (int i = 0; i < graphics.length; i++) {
            assertEquals(0, graphics[i]);
        }
    }

    @Test
    public void testOpcode00EE() {
        chip8.addToStack(100);

        OpCodeRunner.executeOpcode(0x00EE, chip8);

        assertEquals(100, chip8.getPc());
    }

    @Test
    public void testOpcode1NNN() {
        OpCodeRunner.executeOpcode(0x1ABC, chip8);

        assertEquals(0xABC, chip8.getPc());
    }

    @Test
    public void testOpcode2NNN() {
        chip8.setPc(100);

        OpCodeRunner.executeOpcode(0x2ABC, chip8);

        assertEquals(100, chip8.popStack());
        assertEquals(0xABC, chip8.getPc());
    }

    @Test
    public void testOpcode3XNN() {
        chip8.setPc(0);
        chip8.setRegistersAt(0xA, (byte) 0x21);
        OpCodeRunner.executeOpcode(0x3A21, chip8);
        assertEquals(4, chip8.getPc());

        chip8.setPc(0);
        chip8.setRegistersAt(0xA, (byte) 0x21);
        OpCodeRunner.executeOpcode(0x3A22, chip8);
        assertEquals(2, chip8.getPc());
    }

    @Test
    public void testOpcode4XNN() {
        chip8.setPc(0);
        chip8.setRegistersAt(0xA, (byte) 0x21);
        OpCodeRunner.executeOpcode(0x4A21, chip8);
        assertEquals(2, chip8.getPc());

        chip8.setPc(0);
        chip8.setRegistersAt(0xA, (byte) 0x21);
        OpCodeRunner.executeOpcode(0x4A22, chip8);
        assertEquals(4, chip8.getPc());
    }

    @Test
    public void testOpcode5XY0() {
        chip8.setPc(0);
        chip8.setRegistersAt(0xA, (byte) 0x21);
        chip8.setRegistersAt(0xB, (byte) 0x21);
        OpCodeRunner.executeOpcode(0x5AB0, chip8);
        assertEquals(4, chip8.getPc());

        chip8.setPc(0);
        chip8.setRegistersAt(0xA, (byte) 0x21);
        chip8.setRegistersAt(0xB, (byte) 0x22);
        OpCodeRunner.executeOpcode(0x5AB0, chip8);
        assertEquals(2, chip8.getPc());
    }

    @Test
    public void testOpcode6XNN() {
        OpCodeRunner.executeOpcode(0x6A12, chip8);

        assertEquals(0x12, chip8.getRegisterAt(0xA));
    }

    @Test
    public void testOpcode7XNN() {
        chip8.setRegistersAt(0xA, (byte) 0x1);
        OpCodeRunner.executeOpcode(0x7A12, chip8);

        assertEquals(0x13, chip8.getRegisterAt(0xA));
    }

    @Test
    public void testOpcode8XY0() {
        chip8.setRegistersAt(0xB, (byte) 0xF0);
        chip8.setRegistersAt(0xA, (byte) 0x22);

        OpCodeRunner.executeOpcode(0x8BA0, chip8);

        assertEquals(0x22, chip8.getRegisterAt(0xB));
    }

    @Test
    public void testOpcode8XY1() {
        chip8.setRegistersAt(0xB, (byte) 0xAA);
        chip8.setRegistersAt(0xA, (byte) 0x22);

        OpCodeRunner.executeOpcode(0x8BA1, chip8);

        assertEquals((byte) (0xAA | 0x22), chip8.getRegisterAt(0xB));
    }

    @Test
    public void testOpcode8XY2() {
        chip8.setRegistersAt(0xB, (byte) 0xAA);
        chip8.setRegistersAt(0xA, (byte) 0x22);

        OpCodeRunner.executeOpcode(0x8BA2, chip8);

        assertEquals((byte) (0xAA & 0x22), chip8.getRegisterAt(0xB));
    }

    @Test
    public void testOpcode8XY3() {
        chip8.setRegistersAt(0xB, (byte) 0xAA);
        chip8.setRegistersAt(0xA, (byte) 0x22);

        OpCodeRunner.executeOpcode(0x8BA3, chip8);

        assertEquals((byte) (0xAA ^ 0x22), chip8.getRegisterAt(0xB));
    }

    @Test
    public void testOpcode8XY4() {
        chip8.setRegistersAt(0xB, (byte) 0x11);
        chip8.setRegistersAt(0xA, (byte) 0x22);

        OpCodeRunner.executeOpcode(0x8BA4, chip8);

        assertEquals(0x33, chip8.getRegisterAt(0xB));
        assertEquals(0, chip8.getRegisterAt(0xF));

        chip8.setRegistersAt(0xB, (byte) 0x11);
        chip8.setRegistersAt(0xA, (byte) 0xFF);

        OpCodeRunner.executeOpcode(0x8BA4, chip8);

        assertEquals(0x10, chip8.getRegisterAt(0xB));
        assertEquals(1, chip8.getRegisterAt(0xF));
    }

    @Test
     public void testOpcode8XY5() {
        chip8.setRegistersAt(0xB, (byte) 0xFF);
        chip8.setRegistersAt(0xA, (byte) 0x11);

        OpCodeRunner.executeOpcode(0x8BA5, chip8);

        assertEquals((byte) 0xEE, chip8.getRegisterAt(0xB));
        assertEquals(1, chip8.getRegisterAt(0xF));

        chip8.setRegistersAt(0xB, (byte) 0x00);
        chip8.setRegistersAt(0xA, (byte) 0x01);

        OpCodeRunner.executeOpcode(0x8BA5, chip8);

        assertEquals((byte) 0xFF, chip8.getRegisterAt(0xB));
        assertEquals(0, chip8.getRegisterAt(0xF));
    }

    @Test
    public void testOpcode8XY6() {
        chip8.setRegistersAt(0xA, (byte) 0b00000001);

        OpCodeRunner.executeOpcode(0x8A06, chip8);

        assertEquals(0b00000010, chip8.getRegisterAt(0xA));
        assertEquals(1, chip8.getRegisterAt(0xF));
    }

    @Test
    public void testOpcode8XY7() {
        chip8.setRegistersAt(0xA, (byte) 0xFF);
        chip8.setRegistersAt(0xB, (byte) 0x11);

        OpCodeRunner.executeOpcode(0x8BA7, chip8);

        assertEquals((byte) 0xEE, chip8.getRegisterAt(0xB));
        assertEquals((byte) 0xFF, chip8.getRegisterAt(0xA));
        assertEquals(1, chip8.getRegisterAt(0xF));

        chip8.setRegistersAt(0xA, (byte) 0x00);
        chip8.setRegistersAt(0xB, (byte) 0x01);

        OpCodeRunner.executeOpcode(0x8BA7, chip8);

        assertEquals((byte) 0xFF, chip8.getRegisterAt(0xB));
        assertEquals((byte) 0x00, chip8.getRegisterAt(0xA));
        assertEquals(0, chip8.getRegisterAt(0xF));
    }

    @Test
    @Ignore
    public void testOpcode8XYE() {
        chip8.setRegistersAt(0xA, (byte) 0b10000000);

        OpCodeRunner.executeOpcode(0x8A0E, chip8);

        assertEquals((byte)0b01000000, chip8.getRegisterAt(0xA));
        assertEquals(1, chip8.getRegisterAt(0xF));
    }

    @Test
    public void testOpcode9XY0() {
        chip8.setPc(0);
        chip8.setRegistersAt(0xA, (byte) 0x10);
        chip8.setRegistersAt(0xB, (byte) 0x10);

        OpCodeRunner.executeOpcode(0x9AB0, chip8);

        assertEquals(2, chip8.getPc());

        chip8.setPc(0);
        chip8.setRegistersAt(0xA, (byte) 0x11);
        chip8.setRegistersAt(0xB, (byte) 0x10);

        OpCodeRunner.executeOpcode(0x9AB0, chip8);

        assertEquals(4, chip8.getPc());
    }

    @Test
    public void testOpcodeANNN() {
        OpCodeRunner.executeOpcode(0xA123, chip8);

        assertEquals(0x0123, chip8.getI());
    }

    @Test
    public void testOpcodeBNNN() {
        chip8.setRegistersAt(0, (byte) 1);

        OpCodeRunner.executeOpcode(0xB123, chip8);

        assertEquals(0x124, chip8.getPc());
    }
}

