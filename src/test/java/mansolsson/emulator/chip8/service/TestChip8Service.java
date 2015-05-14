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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
        // TODO
    }

    @Test
    public void testScreenUpdated() {
        // TODO
    }

    @Test
    public void testPopStackIntoPc() {
        // TODO
    }

    @Test
    public void testStorePcInStack() {
        // TODO
    }

    @Test
    public void setPc() {
        // TODO
    }

    @Test
    public void testRegistryAtEqualsValue() {
        // TODO
    }

    @Test
    public void testRegistryAtEqualsRegistryAt() {
        // TODO
    }

    @Test
    public void testSetRegistryAt() {
        // TODO
    }

    @Test
    public void testAddToRegistryAt() {
        // TODO
    }

    @Test
    public void testGetRegistryAt() {
        // TODO
    }

    @Test
    public void testOrRegistryAt() {
        // TODO
    }

    @Test
    public void testAndRegistryAt() {
        // TODO
    }

    @Test
    public void testXorRegistryAt() {
        // TODO
    }

    @Test
    public void testSubtractFromRegistry() {
        // TODO
    }

    @Test
    public void testSetAddressRegister() {
        // TODO
    }

    @Test
    public void testGetAddressRegister() {
        // TODO
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
