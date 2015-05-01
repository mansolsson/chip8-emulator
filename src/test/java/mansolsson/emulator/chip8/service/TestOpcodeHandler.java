package mansolsson.emulator.chip8.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
        verify(chip8Service).movePcToNextInstruction();
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
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte)0x1);

        opcodeHandler.executeOpcode(0x8A06);

        verify(chip8Service).setRegistryAt(0xF, (byte) 0x1);
        verify(chip8Service, times(2)).getRegistryAt(0xA);
        verify(chip8Service).setRegistryAt(0xA, (byte)0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode8XX6LeastSignificantBitNotSet() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte)0x2);

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
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte)0b10000000);

        opcodeHandler.executeOpcode(0x8A0E);

        verify(chip8Service).setRegistryAt(0xF, (byte) 0x1);
        verify(chip8Service, times(2)).getRegistryAt(0xA);
        verify(chip8Service).setRegistryAt(0xA, (byte) 0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode8XXEWithGreatestBitNotSet() {
        when(chip8Service.getRegistryAt(0xA)).thenReturn((byte)0b01000000);

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
        when(chip8Service.getRegistryAt(0x0)).thenReturn((byte)0xFF);

        opcodeHandler.executeOpcode(0xB123);

        verify(chip8Service).getRegistryAt(0x0);
        verify(chip8Service).setPc(0x123 + 0xFF);
        verifyNoMoreInteractions(chip8Service);
    }

    @Test
    public void testExecuteOpcode0xCXXX() {
        opcodeHandler.executeOpcode(0xCA00);

        verify(chip8Service).setRegistryAt(0xA, (byte)0x0);
        verify(chip8Service).movePcToNextInstruction();
        verifyNoMoreInteractions(chip8Service);
    }
}
