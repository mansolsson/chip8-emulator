package mansolsson.emulator.chip8.service;

import mansolsson.emulator.chip8.model.Chip8;
import mansolsson.emulator.chip8.model.Chip8Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestChip8Service {
    @Mock
    private OpcodeHandler opcodeHandler;
    @Mock
    private Chip8 chip8;
    @InjectMocks
    private Chip8Service chip8Service;

    @Test
    public void testLoadProgram() {
        // TODO
    }

    @Test
    public void testExecuteProgramInstruction() {
        // TODO
    }

    @Test
    public void testShouldScreenBeUpdatedWhenScreenShouldBeUpdated() {
        when(chip8.isRedrawScreen()).thenReturn(true);

        boolean result = chip8Service.shouldScreenBeUpdated();

        assertTrue(result);
        verify(chip8).isRedrawScreen();
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testShouldScreenBeUpdatedWhenScreenShouldNotBeUpdated() {
        when(chip8.isRedrawScreen()).thenReturn(false);

        boolean result = chip8Service.shouldScreenBeUpdated();

        assertFalse(result);
        verify(chip8).isRedrawScreen();
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testShouldSoundBePlayedWhenSoundShouldBePlayed() {
        when(chip8.isPlaySound()).thenReturn(true);

        boolean result = chip8Service.shouldSoundBePlayed();

        assertTrue(result);
        verify(chip8).isPlaySound();
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testShouldSoundBePlayedWhenSoundShouldNotBePlayed() {
        when(chip8.isPlaySound()).thenReturn(true);

        boolean result = chip8Service.shouldSoundBePlayed();

        assertTrue(result);
        verify(chip8).isPlaySound();
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testMovePcToNextInstruction() {
        int currentPcValue = 0x202;
        when(chip8.getPc()).thenReturn(currentPcValue);

        chip8Service.movePcToNextInstruction();

        verify(chip8).getPc();
        verify(chip8).setPc(currentPcValue + Chip8Constants.NR_OF_BYTES_PER_INSTRUCTION);
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testClearScreen() {
        byte[] graphics = new byte[Chip8Constants.GRAPHICS_SIZE];
        for(int i = 0; i < graphics.length; i++) {
            graphics[i] = (byte) 1;
        }
        when(chip8.getGraphics()).thenReturn(graphics);

        chip8Service.clearScreen();

        for(byte graphic : graphics) {
            assertEquals((byte) 0, graphic);
        }
        verify(chip8).getGraphics();
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testSetScreenToBeUpdated() {
        chip8Service.setScreenToBeUpdated();

        verify(chip8).setRedrawScreen(true);
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testScreenUpdated() {
        chip8Service.setScreenUpdated();

        verify(chip8).setRedrawScreen(false);
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testPopStackIntoPc() {
        int currentStackPointer = 2;
        when(chip8.getStackPointer()).thenReturn(currentStackPointer);
        int[] stack = new int[Chip8Constants.STACK_SIZE];
        stack[currentStackPointer] = 10;
        when(chip8.getStack()).thenReturn(stack);

        chip8Service.popStackIntoPc();

        verify(chip8, times(2)).getStackPointer();
        verify(chip8).setStackPointer(currentStackPointer - 1);
        verify(chip8).getStack();
        verify(chip8).setPc(10);
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testStorePcInStack() {
        int currentStackPointer = -1;
        when(chip8.getStackPointer()).thenReturn(currentStackPointer);
        int[] stack = new int[Chip8Constants.STACK_SIZE];
        when(chip8.getStack()).thenReturn(stack);
        when(chip8.getPc()).thenReturn(254);

        chip8Service.storePcInStack();

        verify(chip8).getStackPointer();
        verify(chip8).setStackPointer(currentStackPointer + 1);
        verify(chip8).getStack();
        verify(chip8).getPc();
        verifyNoMoreInteractions(chip8);
        assertEquals(254, stack[currentStackPointer + 1]);
    }

    @Test
    public void setPc() {
        int pcValue = 254;

        chip8Service.setPc(pcValue);

        verify(chip8).setPc(pcValue);
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testRegistryAtEqualsValueWhenEquals() {
        byte value = (byte)0xFF;
        when(chip8.getRegisters()).thenReturn(new byte[]{(byte) 0xFF});

        boolean result = chip8Service.registryAtEqualsValue(0x0, value);

        verify(chip8).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertTrue(result);
    }

    @Test
    public void testRegistryAtEqualsValueWhenNotEquals() {
        byte value = (byte)0xFF;
        when(chip8.getRegisters()).thenReturn(new byte[]{(byte)0xFE});

        boolean result = chip8Service.registryAtEqualsValue(0x0, value);

        verify(chip8).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertFalse(result);
    }

    @Test
    public void testRegistryAtEqualsRegistryAtWhenEquals() {
        when(chip8.getRegisters()).thenReturn(new byte[]{(byte) 0xFF, (byte) 0xFF});

        boolean result = chip8Service.registryAtEqualsRegistryAt(0x0, 0x1);

        verify(chip8, times(2)).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertTrue(result);
    }

    @Test
    public void testRegistryAtEqualsRegistryAtWhenNotEquals() {
        when(chip8.getRegisters()).thenReturn(new byte[]{(byte) 0xFE, (byte) 0xFF});

        boolean result = chip8Service.registryAtEqualsRegistryAt(0x0, 0x1);

        verify(chip8, times(2)).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertFalse(result);
    }

    @Test
    public void testSetRegistryAt() {
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        when(chip8.getRegisters()).thenReturn(registers);
        byte value = (byte) 0xFB;
        int registerIndex = 0x1;

        chip8Service.setRegistryAt(registerIndex, value);

        verify(chip8).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertEquals(value, registers[registerIndex]);
    }

    @Test
    public void testAddToRegistryAtWithCarry() {
        int registerIndex = 0;
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        registers[registerIndex] = (byte)0xFE;
        when(chip8.getRegisters()).thenReturn(registers);

        boolean result = chip8Service.addToRegistryAt(registerIndex, (byte) 0x2);

        verify(chip8, times(2)).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertTrue(result);
        assertEquals((byte) 0, registers[registerIndex]);
    }

    @Test
    public void testAddToRegistryAtWithoutCarry() {
        int registerIndex = 0;
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        registers[registerIndex] = (byte)0xFE;
        when(chip8.getRegisters()).thenReturn(registers);

        boolean result = chip8Service.addToRegistryAt(registerIndex, (byte) 0x1);

        verify(chip8, times(2)).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertFalse(result);
        assertEquals((byte) 0xFF, registers[registerIndex]);
    }

    @Test
    public void testGetRegistryAt() {
        int registerIndex = 0;
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        registers[registerIndex] = (byte)0xFE;
        when(chip8.getRegisters()).thenReturn(registers);

        byte result = chip8Service.getRegistryAt(registerIndex);

        verify(chip8).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertEquals((byte) 0xFE, result);
    }

    @Test
    public void testOrRegistryAt() {
        int registerIndex = 0;
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        registers[registerIndex] = (byte) 0b10101010;
        when(chip8.getRegisters()).thenReturn(registers);
        byte input = (byte) 0b11110000;
        byte expectedResult = (byte) 0b11111010;

        chip8Service.orRegistryAt(registerIndex, input);

        verify(chip8).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertEquals(expectedResult, registers[registerIndex]);
    }

    @Test
    public void testAndRegistryAt() {
        int registerIndex = 0;
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        registers[registerIndex] = (byte) 0b10101010;
        when(chip8.getRegisters()).thenReturn(registers);
        byte input = (byte) 0b11110000;
        byte expectedResult = (byte) 0b10100000;

        chip8Service.andRegistryAt(registerIndex, input);

        verify(chip8).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertEquals(expectedResult, registers[registerIndex]);
    }

    @Test
    public void testXorRegistryAt() {
        int registerIndex = 0;
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        registers[registerIndex] = (byte) 0b10101010;
        when(chip8.getRegisters()).thenReturn(registers);
        byte input = (byte) 0b11110000;
        byte expectedResult = (byte) 0b01011010;

        chip8Service.xorRegistryAt(registerIndex, input);

        verify(chip8).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertEquals(expectedResult, registers[registerIndex]);
    }

    @Test
    public void testSubtractFromRegistryWithoutBorrow() {
        int registerIndex = 0;
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        registers[registerIndex] = (byte)0xFE;
        when(chip8.getRegisters()).thenReturn(registers);

        boolean result = chip8Service.subtractFromRegistry(registerIndex, (byte) 0xFE);

        verify(chip8, times(2)).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertEquals((byte) 0x0, registers[registerIndex]);
        assertFalse(result);
    }

    @Test
    public void testSubtractFromRegistryWithBorrow() {
        int registerIndex = 0;
        byte[] registers = new byte[Chip8Constants.NR_OF_REGISTERS];
        registers[registerIndex] = (byte)0xFE;
        when(chip8.getRegisters()).thenReturn(registers);

        boolean result = chip8Service.subtractFromRegistry(registerIndex, (byte) 0xFF);

        verify(chip8, times(2)).getRegisters();
        verifyNoMoreInteractions(chip8);
        assertEquals((byte) 0xFF, registers[registerIndex]);
        assertTrue(result);
    }

    @Test
    public void testSetAddressRegister() {
        int input = 10;

        chip8Service.setAddressRegister(input);

        verify(chip8).setAddressRegister(input);
        verifyNoMoreInteractions(chip8);
    }

    @Test
    public void testGetAddressRegister() {
        int expectedValue = 10;
        when(chip8.getAddressRegister()).thenReturn(expectedValue);

        int result = chip8Service.getAddressRegister();

        verify(chip8).getAddressRegister();
        verifyNoMoreInteractions(chip8);
        assertEquals(expectedValue, result);
    }

    @Test
    public void testGetMemory() {
        // TODO
    }

    @Test
    public void testGetGraphics() {
        // TODO
    }

    @Test
    public void testGetDelayTimer() {
        // TODO
    }

    @Test
    public void testSetDelayTimer() {
        // TODO
    }

    @Test
    public void testSetSoundTimer() {
        // TODO
    }

    @Test
    public void testGetKey() {
        // TODO
    }

    @Test
    public void testSetKey() {
        // TODO
    }

    @Test
    public void testSubtractRegistryFromValue() {
        // TODO
    }

    @Test
    public void testUpdateGraphics() {
        // TODO
    }
}
