package mansolsson.emulator.chip8;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestOpCodeRunner {
    private Chip8 chip8;

    @Before
    public void prepare() {
        chip8 = new Chip8();
        chip8.init();
    }

    @Test
    public void testOpcode00E0() {
        chip8.setRedrawScreen(false);
        byte[] graphics = chip8.getGraphics();
        for (int i = 0; i < graphics.length; i++) {
            graphics[i] = 1;
        }

        OpCodeRunner.executeOpcode(0x00E0, chip8);

        for (byte graphic : graphics) {
            assertEquals(0, graphic);
        }
        assertTrue(chip8.isRedrawScreen());
    }

    @Test
    public void testOpcode00EE() {
        chip8.addToStack(100);

        OpCodeRunner.executeOpcode(0x00EE, chip8);

        assertEquals(102, chip8.getPc());
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
        chip8.setRegistersAt(0xA, (byte) 0b10000001);

        OpCodeRunner.executeOpcode(0x8A06, chip8);

        assertEquals(0b01000000, chip8.getRegisterAt(0xA));
        assertEquals(1, chip8.getRegisterAt(0xF));

        chip8.setRegistersAt(0xA, (byte) 0b10000000);

        OpCodeRunner.executeOpcode(0x8A06, chip8);

        assertEquals(0b01000000, chip8.getRegisterAt(0xA));
        assertEquals(0, chip8.getRegisterAt(0xF));
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
    public void testOpcode8XYE() {
        chip8.setRegistersAt(0xA, (byte) 0b10000001);

        OpCodeRunner.executeOpcode(0x8A0E, chip8);

        assertEquals((byte) 0b00000010, chip8.getRegisterAt(0xA));
        assertEquals(1, chip8.getRegisterAt(0xF));

        chip8.setRegistersAt(0xA, (byte) 0b00000001);

        OpCodeRunner.executeOpcode(0x8A0E, chip8);

        assertEquals((byte)0b00000010, chip8.getRegisterAt(0xA));
        assertEquals(0, chip8.getRegisterAt(0xF));
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

        assertEquals(0x0123, chip8.getAddressRegister());
    }

    @Test
    public void testOpcodeBNNN() {
        chip8.setRegistersAt(0, (byte) 1);

        OpCodeRunner.executeOpcode(0xB123, chip8);

        assertEquals(0x124, chip8.getPc());
    }

    @Test
    public void testOpcodeCXNN() {
        chip8.setPc(0x200);
        chip8.setRegistersAt(1, (byte)0xAB);

        OpCodeRunner.executeOpcode(0xC100, chip8);

        assertEquals(0, chip8.getRegisterAt(1));
        assertEquals(0x202, chip8.getPc());
    }

    // TODO: DXYN

    @Test
    public void testOpcodeEX9E() {
        chip8.setPc(0x200);
        chip8.setRegistersAt(1, (byte) 2);
        chip8.getKeys()[2] = 1;

        OpCodeRunner.executeOpcode(0xE19E, chip8);

        assertEquals(0x204, chip8.getPc());

        chip8.setPc(0x200);
        chip8.setRegistersAt(1, (byte) 2);
        chip8.getKeys()[2] = 0;

        OpCodeRunner.executeOpcode(0xE19E, chip8);

        assertEquals(0x202, chip8.getPc());
    }

    @Test
    public void testOpcodeEXA1() {
        chip8.setPc(0x200);
        chip8.setRegistersAt(1, (byte) 2);
        chip8.getKeys()[2] = 0;

        OpCodeRunner.executeOpcode(0xE1A1, chip8);

        assertEquals(0x204, chip8.getPc());

        chip8.setPc(0x200);
        chip8.setRegistersAt(1, (byte) 2);
        chip8.getKeys()[2] = 1;

        OpCodeRunner.executeOpcode(0xE1A1, chip8);

        assertEquals(0x202, chip8.getPc());
    }

    @Test
    public void testOpcodeFX07() {
        chip8.setDelayTimer((byte) 0x12);

        OpCodeRunner.executeOpcode(0xFA07, chip8);

        assertEquals(0x12, chip8.getRegisterAt(0xA));
    }

    @Test
    public void testFX0A() {
        chip8.setPc(0x200);

        OpCodeRunner.executeOpcode(0xFB0A, chip8);

        assertEquals(0x200, chip8.getPc());

        chip8.getKeys()[0] = 1;

        OpCodeRunner.executeOpcode(0xFB0A, chip8);

        assertEquals(0x202, chip8.getPc());
    }

    @Test
    public void testOpcodeFX15() {
        chip8.setRegistersAt(0xA, (byte)0x12);

        OpCodeRunner.executeOpcode(0xFA15, chip8);

        assertEquals(0x12, chip8.getDelayTimer());
    }

    @Test
    public void testOpcodeFX18() {
        chip8.setPc(0x200);
        chip8.setSoundTimer((byte) 0);
        chip8.setRegistersAt(0xA, (byte) 0x12);

        OpCodeRunner.executeOpcode(0xFA18, chip8);

        assertEquals(0x202, chip8.getPc());
        assertEquals(0x12, chip8.getSoundTimer());
    }

    @Test
    public void testOpcodeFX1E() {
        chip8.setPc(0x200);
        chip8.setAddressRegister(0x100);
        chip8.setRegistersAt(0xB, (byte) 0x10);

        OpCodeRunner.executeOpcode(0xFB1E, chip8);

        assertEquals(0x110, chip8.getAddressRegister());
        assertEquals(0x202, chip8.getPc());
    }

    // TODO: FX29

    // TODO: FX33

    // TODO: FX55

    // TODO: FX65
}

