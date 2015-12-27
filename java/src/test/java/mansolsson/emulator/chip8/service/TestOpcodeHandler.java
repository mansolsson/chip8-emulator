package mansolsson.emulator.chip8.service;

import mansolsson.emulator.chip8.model.Chip8Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestOpcodeHandler {
    @Mock
    private Chip8Service chip8Service;
    @InjectMocks
    private OpcodeHandler opcodeHandler;

    @Test
    public void testExecuteOpcode0x00E0() {
        opcodeHandler.executeOpcode(0x00E0);

        verify(chip8Service).clearScreen();
        verify(chip8Service).setScreenToBeUpdated();
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x00EE() {
        opcodeHandler.executeOpcode(0x00EE);

        verify(chip8Service).popStackIntoPc();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x1XXX() {
        opcodeHandler.executeOpcode(0x1A32);

        verify(chip8Service).setPc(0xA32);
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x2XXX() {
        opcodeHandler.executeOpcode(0x2A32);

        verify(chip8Service).storePcInStack();
        verify(chip8Service).setPc(0xA32);
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x3XXXWhenEquals() {
        when(chip8Service.registryAtEqualsValue(0xB, (byte) 0x43)).thenReturn(Boolean.TRUE);

        opcodeHandler.executeOpcode(0x3B43);

        verify(chip8Service).registryAtEqualsValue(0xB, (byte) 0x43);
        verify(chip8Service, times(2)).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x3XXXWhenNotEquals() {
        when(chip8Service.registryAtEqualsValue(0xB, (byte) 0x43)).thenReturn(Boolean.FALSE);

        opcodeHandler.executeOpcode(0x3B43);

        verify(chip8Service).registryAtEqualsValue(0xB, (byte) 0x43);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x4XXXWhenEquals() {
        when(chip8Service.registryAtEqualsValue(0xB, (byte) 0x43)).thenReturn(Boolean.TRUE);

        opcodeHandler.executeOpcode(0x4B43);

        verify(chip8Service).registryAtEqualsValue(0xB, (byte) 0x43);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x4XXXWhenNotEquals() {
        when(chip8Service.registryAtEqualsValue(0xB, (byte) 0x43)).thenReturn(Boolean.FALSE);

        opcodeHandler.executeOpcode(0x4B43);

        verify(chip8Service).registryAtEqualsValue(0xB, (byte) 0x43);
        verify(chip8Service, times(2)).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x5XXXWhenEquals() {
        when(chip8Service.registryAtEqualsRegistryAt(0xB, 0xA)).thenReturn(Boolean.TRUE);

        opcodeHandler.executeOpcode(0x5BAF);

        verify(chip8Service).registryAtEqualsRegistryAt(0xB, 0xA);
        verify(chip8Service, times(2)).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x5XXXWhenNotEquals() {
        when(chip8Service.registryAtEqualsRegistryAt(0xB, 0xA)).thenReturn(Boolean.FALSE);

        opcodeHandler.executeOpcode(0x5BAF);

        verify(chip8Service).registryAtEqualsRegistryAt(0xB, 0xA);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x6XXX() {
        opcodeHandler.executeOpcode(0x6A23);

        verify(chip8Service).setRegistryAt(0xA, (byte) 0x23);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x7XXX() {
        opcodeHandler.executeOpcode(0x7A23);

        verify(chip8Service).addToRegistryAt(0xA, (byte) 0x23);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x8XX0() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);

        opcodeHandler.executeOpcode(0x8AB0);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).setRegistryAt(0xA, (byte) 0xFF);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x8XX1() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);

        opcodeHandler.executeOpcode(0x8AB1);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).orRegistryAt(0xA, (byte) 0xFF);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x8XX2() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);

        opcodeHandler.executeOpcode(0x8AB2);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).andRegistryAt(0xA, (byte) 0xFF);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x8XX3() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);

        opcodeHandler.executeOpcode(0x8AB3);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).xorRegistryAt(0xA, (byte) 0xFF);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x8XX4WithCarry() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);
        when(chip8Service.addToRegistryAt(0xA, (byte) 0xFF)).thenReturn(Boolean.TRUE);

        opcodeHandler.executeOpcode(0x8AB4);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).addToRegistryAt(0xA, (byte) 0xFF);
        verify(chip8Service).setRegistryAt(0xF, (byte) 0x1);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x8XX4WithoutCarry() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);
        when(chip8Service.addToRegistryAt(0xA, (byte) 0xFF)).thenReturn(Boolean.FALSE);

        opcodeHandler.executeOpcode(0x8AB4);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).addToRegistryAt(0xA, (byte) 0xFF);
        verify(chip8Service).setRegistryAt(0xF, (byte) 0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x8XX5WithBorrow() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);
        when(chip8Service.subtractFromRegistry(0xA, (byte) 0xFF)).thenReturn(Boolean.TRUE);

        opcodeHandler.executeOpcode(0x8AB5);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).subtractFromRegistry(0xA, (byte) 0xFF);
        verify(chip8Service).setRegistryAt(0xF, (byte) 0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x8XX5WithoutBorrow() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);
        when(chip8Service.subtractFromRegistry(0xA, (byte) 0xFF)).thenReturn(Boolean.FALSE);

        opcodeHandler.executeOpcode(0x8AB5);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).subtractFromRegistry(0xA, (byte) 0xFF);
        verify(chip8Service).setRegistryAt(0xF, (byte) 0x1);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode8XX6LeastSignificantBitSet() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x1);

        opcodeHandler.executeOpcode(0x8A06);

        verify(chip8Service).setRegistryAt(0xF, (byte) 0x1);
        verify(chip8Service, times(2)).getRegistryAt(0xA);
        verify(chip8Service).setRegistryAt(0xA, (byte) 0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode8XX6LeastSignificantBitNotSet() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x2);

        opcodeHandler.executeOpcode(0x8A06);

        verify(chip8Service).setRegistryAt(0xF, (byte) 0x0);
        verify(chip8Service, times(2)).getRegistryAt(0xA);
        verify(chip8Service).setRegistryAt(0xA, (byte) 0x1);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode8XX7WithBorrow() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);
        when(chip8Service.subtractRegistryFromValue(0xA, (byte) 0xFF)).thenReturn(Boolean.TRUE);

        opcodeHandler.executeOpcode(0x8AB7);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).subtractRegistryFromValue(0xA, (byte) 0xFF);
        verify(chip8Service).setRegistryAt(0xF, (byte) 0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode8XX7WithoutBorrow() {
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0xFF);
        when(chip8Service.subtractRegistryFromValue(0xA, (byte) 0xFF)).thenReturn(Boolean.FALSE);

        opcodeHandler.executeOpcode(0x8AB7);

        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).subtractRegistryFromValue(0xA, (byte) 0xFF);
        verify(chip8Service).setRegistryAt(0xF, (byte) 0x1);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode8XXEWithGreatestBitSet() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0b10000000);

        opcodeHandler.executeOpcode(0x8A0E);

        verify(chip8Service).setRegistryAt(0xF, (byte) 0x1);
        verify(chip8Service, times(2)).getRegistryAt(0xA);
        verify(chip8Service).setRegistryAt(0xA, (byte) 0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode8XXEWithGreatestBitNotSet() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0b01000000);

        opcodeHandler.executeOpcode(0x8A0E);

        verify(chip8Service).setRegistryAt(0xF, (byte) 0x0);
        verify(chip8Service, times(2)).getRegistryAt(0xA);
        verify(chip8Service).setRegistryAt(0xA, (byte) 0b10000000);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x9XXXWhenEquals() {
        when(chip8Service.registryAtEqualsRegistryAt(0xB, 0xA)).thenReturn(Boolean.TRUE);

        opcodeHandler.executeOpcode(0x9BAF);

        verify(chip8Service).registryAtEqualsRegistryAt(0xB, 0xA);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0x9XXXWhenNotEquals() {
        when(chip8Service.registryAtEqualsRegistryAt(0xB, 0xA)).thenReturn(Boolean.FALSE);

        opcodeHandler.executeOpcode(0x9BAF);

        verify(chip8Service).registryAtEqualsRegistryAt(0xB, 0xA);
        verify(chip8Service, times(2)).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xAXXX() {
        opcodeHandler.executeOpcode(0xA123);

        verify(chip8Service).setAddressRegister(0x123);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xBXXX() {
        when(chip8Service.getRegistryAt(0x0)).thenReturn((byte) 0xFF);

        opcodeHandler.executeOpcode(0xB123);

        verify(chip8Service).getRegistryAt(0x0);
        verify(chip8Service).setPc(0x123 + 0xFF);
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xCXXX() {
        opcodeHandler.executeOpcode(0xCA00);

        verify(chip8Service).setRegistryAt(0xA, (byte) 0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xDXXX() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x1);
        when(chip8Service.getRegistryAt(0xB)).thenReturn((byte) 0x2);

        opcodeHandler.executeOpcode(0xDAB4);

        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).getRegistryAt(0xB);
        verify(chip8Service).updateGraphics(0x1, 0x2, 0x4);
        verify(chip8Service).setScreenToBeUpdated();
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xEX9EWhenKeyPressed() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x1);
        when(chip8Service.getKey(1)).thenReturn(true);

        opcodeHandler.executeOpcode(0xEA9E);

        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).getKey(1);
        verify(chip8Service, times(2)).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xEX9EWhenKeyNotPressed() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x1);
        when(chip8Service.getKey(0xA)).thenReturn(false);

        opcodeHandler.executeOpcode(0xEA9E);

        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).getKey(0x1);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xEXA1WhenKeyPressed() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x1);
        when(chip8Service.getKey(1)).thenReturn(true);

        opcodeHandler.executeOpcode(0xEAA1);

        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).getKey(0x1);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xEXA1WhenKeyNotPressed() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x1);
        when(chip8Service.getKey(anyInt())).thenReturn(false);

        opcodeHandler.executeOpcode(0xEAA1);

        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).getKey(1);
        verify(chip8Service, times(2)).movePcToNextInstruction();
    }

    @Test
    public void testExecuteOpcode0xFX07() {
        when(chip8Service.getDelayTimer()).thenReturn((byte) 0x10);

        opcodeHandler.executeOpcode(0xFA07);

        verify(chip8Service).getDelayTimer();
        verify(chip8Service).setRegistryAt(0xA, (byte) 0x10);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xFX0AWhenKeyPressed() {
        when(chip8Service.getKey(0)).thenReturn(false);
        when(chip8Service.getKey(1)).thenReturn(false);
        when(chip8Service.getKey(2)).thenReturn(false);
        when(chip8Service.getKey(3)).thenReturn(true);

        opcodeHandler.executeOpcode(0xFA0A);

        verify(chip8Service, times(4)).getKey(anyInt());
        verify(chip8Service).setRegistryAt(0xA, (byte) 0x3);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xFX0AWhenKeyNotPressed() {
        when(chip8Service.getKey(anyInt())).thenReturn(false);

        opcodeHandler.executeOpcode(0xFA0A);

        verify(chip8Service, times(Chip8Constants.NR_OF_KEYS)).getKey(anyInt());
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xFX15() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x10);

        opcodeHandler.executeOpcode(0xFA15);

        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).setDelayTimer((byte) 0x10);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xFX18() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0x10);

        opcodeHandler.executeOpcode(0xFA18);

        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).setSoundTimer((byte) 0x10);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xFX1EWithOverflow() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0xFE);
        when(chip8Service.getAddressRegister()).thenReturn(0xFFF);

        opcodeHandler.executeOpcode(0xFA1E);

        verify(chip8Service).getAddressRegister();
        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).setAddressRegister(0xFE - 1);
        verify(chip8Service).setRegistryAt(0xF, (byte) 1);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xFX1EWithoutOverflow() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0xFE);
        when(chip8Service.getAddressRegister()).thenReturn(0x10);

        opcodeHandler.executeOpcode(0xFA1E);

        verify(chip8Service).getAddressRegister();
        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).setAddressRegister(0x10 + 0xFE);
        verify(chip8Service).setRegistryAt(0xF, (byte) 0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xFX29() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 0xFE);

        opcodeHandler.executeOpcode(0xFA29);

        verify(chip8Service).getRegistryAt(0xA);
        verify(chip8Service).setAddressRegister(0xFE * 5);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xFX33() {
        byte[] memory = new byte[]{0, 0, 0, 0, 0};
        when(chip8Service.getMemory()).thenReturn(memory);
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte) 123);
        when(chip8Service.getAddressRegister()).thenReturn(1);

        opcodeHandler.executeOpcode(0xFA33);

        verify(chip8Service, times(3)).getMemory();
        verify(chip8Service, times(3)).getRegistryAt(0xA);
        verify(chip8Service, times(3)).getAddressRegister();
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);

        assertEquals(0, memory[0]);
        assertEquals(1, memory[1]);
        assertEquals(2, memory[2]);
        assertEquals(3, memory[3]);
        assertEquals(0, memory[4]);
    }

    @Test
    public void testExecuteOpcode0xFX55() {
        byte[] memory = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        when(chip8Service.getMemory()).thenReturn(memory);
        when(chip8Service.getAddressRegister()).thenReturn(1);
        for (int i = 0; i < 0xF; i++) {
            when(chip8Service.getRegistryAt(i)).thenReturn((byte) (i + 1));
        }

        opcodeHandler.executeOpcode(0xFF55);

        verify(chip8Service).getMemory();
        verify(chip8Service).getAddressRegister();
        verify(chip8Service, times(0xF)).getRegistryAt(anyInt());
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);

        assertEquals(0, memory[0]);
        assertEquals(1, memory[1]);
        assertEquals(2, memory[2]);
        assertEquals(3, memory[3]);
        assertEquals(4, memory[4]);
        assertEquals(5, memory[5]);
        assertEquals(6, memory[6]);
        assertEquals(7, memory[7]);
        assertEquals(8, memory[8]);
        assertEquals(9, memory[9]);
        assertEquals(10, memory[10]);
        assertEquals(11, memory[11]);
        assertEquals(12, memory[12]);
        assertEquals(13, memory[13]);
        assertEquals(14, memory[14]);
        assertEquals(15, memory[15]);
        assertEquals(0, memory[16]);
    }

    @Test
    public void testExecuteOpcode0xFX65() {
        byte[] memory = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
        when(chip8Service.getMemory()).thenReturn(memory);
        when(chip8Service.getAddressRegister()).thenReturn(1);

        opcodeHandler.executeOpcode(0xFF65);

        verify(chip8Service).getMemory();
        verify(chip8Service).getAddressRegister();
        verify(chip8Service).setRegistryAt(0x0, (byte) 1);
        verify(chip8Service).setRegistryAt(0x1, (byte) 2);
        verify(chip8Service).setRegistryAt(0x2, (byte) 3);
        verify(chip8Service).setRegistryAt(0x3, (byte) 4);
        verify(chip8Service).setRegistryAt(0x4, (byte) 5);
        verify(chip8Service).setRegistryAt(0x5, (byte) 6);
        verify(chip8Service).setRegistryAt(0x6, (byte) 7);
        verify(chip8Service).setRegistryAt(0x7, (byte) 8);
        verify(chip8Service).setRegistryAt(0x8, (byte) 9);
        verify(chip8Service).setRegistryAt(0x9, (byte) 10);
        verify(chip8Service).setRegistryAt(0xA, (byte) 11);
        verify(chip8Service).setRegistryAt(0xB, (byte) 12);
        verify(chip8Service).setRegistryAt(0xC, (byte) 13);
        verify(chip8Service).setRegistryAt(0xD, (byte) 14);
        verify(chip8Service).setRegistryAt(0xE, (byte) 15);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }
}
