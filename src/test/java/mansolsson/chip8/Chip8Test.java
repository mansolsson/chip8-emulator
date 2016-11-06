package mansolsson.chip8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.input.KeyCode;

public class Chip8Test {
	private Chip8 chip8;

	@Before
	public void setUp() {
		chip8 = new Chip8();
	}

	@Test
	public void opcode00E0ClearsScreen() {
		for (int y = 0; y < 32; y++) {
			for (int x = 0; x < 64; x++) {
				chip8.getScreen().setPixel(x, y, true);
			}
		}
		putOpcodeInMemory(0x00E0);
		chip8.runNextInstruction();
		for (int y = 0; y < 32; y++) {
			for (int x = 0; x < 64; x++) {
				assertFalse(chip8.getScreen().getPixel(x, y));
			}
		}
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode00EEStoresAddressInStackToPc() {
		chip8.getStack().push(10);
		putOpcodeInMemory(0x00EE);
		chip8.runNextInstruction();
		assertEquals(10, chip8.getPc());
	}

	@Test
	public void opcode1NNNStoresNNNInPc() {
		putOpcodeInMemory(0x1231);
		chip8.runNextInstruction();
		assertEquals(0x231, chip8.getPc());
	}

	@Test
	public void opcode2NNNStoresNextInstructionInStackAndSetsPcToNNN() {
		putOpcodeInMemory(0x2341);
		chip8.runNextInstruction();
		assertEquals(Integer.valueOf(514), chip8.getStack().peek());
		assertEquals(0x341, chip8.getPc());
	}

	@Test
	public void opcode3XNNSkipNextInstructionIfVXEqualsNN() {
		chip8.getV()[1] = 0x24;
		putOpcodeInMemory(0x3124);
		chip8.runNextInstruction();
		assertEquals(516, chip8.getPc());
	}

	@Test
	public void opcode3XNNDoNotSkipNextInstructionIfVXNotEqualsNN() {
		chip8.getV()[1] = 0x24;
		putOpcodeInMemory(0x3125);
		chip8.runNextInstruction();
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode4XNNDoNotSkipNextInstructionIfVXEqualsNN() {
		chip8.getV()[1] = 0x24;
		putOpcodeInMemory(0x4124);
		chip8.runNextInstruction();
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode4XNNSkipNextInstructionIfVXDoesNotEqualsNN() {
		chip8.getV()[1] = 0x25;
		putOpcodeInMemory(0x4124);
		chip8.runNextInstruction();
		assertEquals(516, chip8.getPc());
	}

	@Test
	public void opcode5XY0DoNotSkipNextInstructionIfVXDoNotEqualsVY() {
		chip8.getV()[1] = 4;
		chip8.getV()[2] = 3;
		putOpcodeInMemory(0x5120);
		chip8.runNextInstruction();
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode5XY0SkipNextInstructionIfVXEqualsVY() {
		chip8.getV()[1] = 4;
		chip8.getV()[2] = 4;
		putOpcodeInMemory(0x5120);
		chip8.runNextInstruction();
		assertEquals(516, chip8.getPc());
	}

	@Test
	public void opcode6XNNSetVXToNN() {
		putOpcodeInMemory(0x6418);
		chip8.runNextInstruction();
		assertEquals(0x18, chip8.getV()[4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode7XNNAddsNNToVX() {
		chip8.getV()[3] = 2;
		putOpcodeInMemory(0x7316);
		chip8.runNextInstruction();
		assertEquals(0x18, chip8.getV()[3]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode7XNNAddsNNToVXOverflowAfterFF() {
		chip8.getV()[3] = 0x14;
		putOpcodeInMemory(0x73F0);
		chip8.runNextInstruction();
		assertEquals(4, chip8.getV()[3]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY0SetVXToVY() {
		chip8.getV()[2] = 89;
		putOpcodeInMemory(0x8320);
		chip8.runNextInstruction();
		assertEquals(89, chip8.getV()[3]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY1SetVXToVXOrVY() {
		chip8.getV()[3] = 0b11_00_11_01;
		chip8.getV()[2] = 0b00_10_11_11;
		putOpcodeInMemory(0x8321);
		chip8.runNextInstruction();
		assertEquals(0b11_10_11_11, chip8.getV()[3]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY2SetVXToVXAndVY() {
		chip8.getV()[1] = 0b11_00_11_01;
		chip8.getV()[3] = 0b00_10_11_11;
		putOpcodeInMemory(0x8132);
		chip8.runNextInstruction();
		assertEquals(0b00_00_11_01, chip8.getV()[1]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY3SetVXToVXXorVY() {
		chip8.getV()[1] = 0b11_00_11_01;
		chip8.getV()[2] = 0b00_10_11_11;
		putOpcodeInMemory(0x8123);
		chip8.runNextInstruction();
		assertEquals(0b11_10_00_10, chip8.getV()[1]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY4AddsVYToVXNoOverflowSetsVFTo0() {
		chip8.getV()[1] = 10;
		chip8.getV()[2] = 55;
		chip8.getV()[0xF] = 14;
		putOpcodeInMemory(0x8124);
		chip8.runNextInstruction();
		assertEquals(65, chip8.getV()[1]);
		assertEquals(0, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY4AddsVYToVXOverflowSetsVFTo1() {
		chip8.getV()[1] = 250;
		chip8.getV()[2] = 55;
		chip8.getV()[0xF] = 14;
		putOpcodeInMemory(0x8124);
		chip8.runNextInstruction();
		assertEquals(49, chip8.getV()[1]);
		assertEquals(1, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY5SubVYFromVXNoBorrowSetsVFTo1() {
		chip8.getV()[1] = 250;
		chip8.getV()[2] = 55;
		chip8.getV()[0xF] = 14;
		putOpcodeInMemory(0x8125);
		chip8.runNextInstruction();
		assertEquals(195, chip8.getV()[1]);
		assertEquals(1, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY5SubVYFromVXBorrowSetsVFTo0() {
		chip8.getV()[1] = 1;
		chip8.getV()[2] = 3;
		chip8.getV()[0xF] = 14;
		putOpcodeInMemory(0x8125);
		chip8.runNextInstruction();
		assertEquals(254, chip8.getV()[1]);
		assertEquals(0, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY6VFLeastBitInVXThenVXShiftRight1() {
		chip8.getV()[1] = 0b10_11_00_01;
		chip8.getV()[0xF] = 14;
		putOpcodeInMemory(0x8106);
		chip8.runNextInstruction();
		assertEquals(0b01_01_10_00, chip8.getV()[1]);
		assertEquals(1, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY7VXSetToVYMinusVXNoBorrowVFSetTo1() {
		chip8.getV()[1] = 6;
		chip8.getV()[2] = 10;
		chip8.getV()[0xF] = 14;
		putOpcodeInMemory(0x8127);
		chip8.runNextInstruction();
		assertEquals(4, chip8.getV()[1]);
		assertEquals(1, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XY7VXSetToVYMinusVXBorrowVFSetTo0() {
		chip8.getV()[1] = 10;
		chip8.getV()[2] = 5;
		chip8.getV()[0xF] = 14;
		putOpcodeInMemory(0x8127);
		chip8.runNextInstruction();
		assertEquals(251, chip8.getV()[1]);
		assertEquals(0, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode8XYESetHighestBitInVXToVFThenShiftLeft1() {
		chip8.getV()[1] = 0b10_11_00_00;
		chip8.getV()[0xF] = 14;
		putOpcodeInMemory(0x810E);
		chip8.runNextInstruction();
		assertEquals(0b01_10_00_00, chip8.getV()[1]);
		assertEquals(1, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcode9XY0SkipInstructionIfVXDoNotEqualVY() {
		chip8.getV()[1] = 3;
		chip8.getV()[2] = 4;
		putOpcodeInMemory(0x9120);
		chip8.runNextInstruction();
		assertEquals(516, chip8.getPc());
	}

	@Test
	public void opcode9XY0DoNotSkipInstructionIfVXEqualVY() {
		chip8.getV()[1] = 3;
		chip8.getV()[2] = 3;
		putOpcodeInMemory(0x9120);
		chip8.runNextInstruction();
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeANNNSetIToNNN() {
		putOpcodeInMemory(0xA435);
		chip8.runNextInstruction();
		assertEquals(0x435, chip8.getI());
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeBNNNSetsPcToNNNPlusV0() {
		chip8.getV()[0] = 3;
		putOpcodeInMemory(0xB124);
		chip8.runNextInstruction();
		assertEquals(0x127, chip8.getPc());
	}

	@Test
	public void opcodeCXNNSetVXToRandomAndNN() {
		chip8.getV()[2] = 1;
		putOpcodeInMemory(0xC200);
		chip8.runNextInstruction();
		assertEquals(0, chip8.getV()[2]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeDXYNDrawSpriteAtIToScreenAtXYNoPixelTurnedOffSetVFTo0() {
		chip8.setI(4);
		chip8.getMemory()[4] = 0b10_00_10_11;
		chip8.getMemory()[5] = 0b00_00_11_01;
		chip8.getScreen().setPixel(10, 2, true);
		chip8.getV()[9] = 4;
		chip8.getV()[8] = 1;
		putOpcodeInMemory(0xD982);
		chip8.runNextInstruction();
		assertTrue(chip8.getScreen().getPixel(4, 1));
		assertFalse(chip8.getScreen().getPixel(5, 1));
		assertFalse(chip8.getScreen().getPixel(6, 1));
		assertFalse(chip8.getScreen().getPixel(7, 1));
		assertTrue(chip8.getScreen().getPixel(8, 1));
		assertFalse(chip8.getScreen().getPixel(9, 1));
		assertTrue(chip8.getScreen().getPixel(10, 1));
		assertTrue(chip8.getScreen().getPixel(11, 1));
		assertFalse(chip8.getScreen().getPixel(4, 2));
		assertFalse(chip8.getScreen().getPixel(5, 2));
		assertFalse(chip8.getScreen().getPixel(6, 2));
		assertFalse(chip8.getScreen().getPixel(7, 2));
		assertTrue(chip8.getScreen().getPixel(8, 2));
		assertTrue(chip8.getScreen().getPixel(9, 2));
		assertTrue(chip8.getScreen().getPixel(10, 2));
		assertTrue(chip8.getScreen().getPixel(11, 2));
		assertEquals(0, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeDXYNDrawSpriteAtIToScreenAtXYPixelTurnedOffSetVFTo1() {
		chip8.setI(4);
		chip8.getMemory()[4] = 0b10_00_10_11;
		chip8.getMemory()[5] = 0b00_00_11_01;
		chip8.getScreen().setPixel(11, 2, true);
		chip8.getV()[9] = 4;
		chip8.getV()[8] = 1;
		putOpcodeInMemory(0xD982);
		chip8.runNextInstruction();
		assertTrue(chip8.getScreen().getPixel(4, 1));
		assertFalse(chip8.getScreen().getPixel(5, 1));
		assertFalse(chip8.getScreen().getPixel(6, 1));
		assertFalse(chip8.getScreen().getPixel(7, 1));
		assertTrue(chip8.getScreen().getPixel(8, 1));
		assertFalse(chip8.getScreen().getPixel(9, 1));
		assertTrue(chip8.getScreen().getPixel(10, 1));
		assertTrue(chip8.getScreen().getPixel(11, 1));
		assertFalse(chip8.getScreen().getPixel(4, 2));
		assertFalse(chip8.getScreen().getPixel(5, 2));
		assertFalse(chip8.getScreen().getPixel(6, 2));
		assertFalse(chip8.getScreen().getPixel(7, 2));
		assertTrue(chip8.getScreen().getPixel(8, 2));
		assertTrue(chip8.getScreen().getPixel(9, 2));
		assertFalse(chip8.getScreen().getPixel(10, 2));
		assertFalse(chip8.getScreen().getPixel(11, 2));
		assertEquals(1, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeDXYNDrawSpriteAtIToScreenAtXYWrapsAroundScreenX() {
		chip8.setI(4);
		chip8.getMemory()[4] = 0b10_00_10_11;
		chip8.getMemory()[5] = 0b00_00_11_01;
		chip8.getV()[9] = 60;
		chip8.getV()[8] = 1;
		putOpcodeInMemory(0xD982);
		chip8.runNextInstruction();
		assertTrue(chip8.getScreen().getPixel(60, 1));
		assertFalse(chip8.getScreen().getPixel(61, 1));
		assertFalse(chip8.getScreen().getPixel(62, 1));
		assertFalse(chip8.getScreen().getPixel(63, 1));
		assertTrue(chip8.getScreen().getPixel(0, 1));
		assertFalse(chip8.getScreen().getPixel(1, 1));
		assertTrue(chip8.getScreen().getPixel(2, 1));
		assertTrue(chip8.getScreen().getPixel(3, 1));
		assertFalse(chip8.getScreen().getPixel(60, 2));
		assertFalse(chip8.getScreen().getPixel(61, 2));
		assertFalse(chip8.getScreen().getPixel(62, 2));
		assertFalse(chip8.getScreen().getPixel(63, 2));
		assertTrue(chip8.getScreen().getPixel(0, 2));
		assertTrue(chip8.getScreen().getPixel(1, 2));
		assertFalse(chip8.getScreen().getPixel(2, 2));
		assertTrue(chip8.getScreen().getPixel(3, 2));
		assertEquals(0, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeDXYNDrawSpriteAtIToScreenAtXYWrapsAroundScreenY() {
		chip8.setI(4);
		chip8.getMemory()[4] = 0b10_00_10_11;
		chip8.getMemory()[5] = 0b00_00_11_01;
		chip8.getV()[9] = 4;
		chip8.getV()[8] = 31;
		putOpcodeInMemory(0xD982);
		chip8.runNextInstruction();
		assertTrue(chip8.getScreen().getPixel(4, 31));
		assertFalse(chip8.getScreen().getPixel(5, 31));
		assertFalse(chip8.getScreen().getPixel(6, 31));
		assertFalse(chip8.getScreen().getPixel(7, 31));
		assertTrue(chip8.getScreen().getPixel(8, 31));
		assertFalse(chip8.getScreen().getPixel(9, 31));
		assertTrue(chip8.getScreen().getPixel(10, 31));
		assertTrue(chip8.getScreen().getPixel(11, 31));
		assertFalse(chip8.getScreen().getPixel(4, 0));
		assertFalse(chip8.getScreen().getPixel(5, 0));
		assertFalse(chip8.getScreen().getPixel(6, 0));
		assertFalse(chip8.getScreen().getPixel(7, 0));
		assertTrue(chip8.getScreen().getPixel(8, 0));
		assertTrue(chip8.getScreen().getPixel(9, 0));
		assertFalse(chip8.getScreen().getPixel(10, 0));
		assertTrue(chip8.getScreen().getPixel(11, 0));
		assertEquals(0, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeEX9ESkikpInstructionWhenKeyInVXIsPressed() {
		chip8.getV()[3] = 6;
		// Key with index 6 pressed
		chip8.getKeyboard().pressKey(KeyCode.NUMPAD6);
		putOpcodeInMemory(0xE39E);
		chip8.runNextInstruction();
		assertEquals(516, chip8.getPc());
	}

	@Test
	public void opcodeEX9EDoNotSkipInstructionWhenKeyInVXIsNotPressed() {
		chip8.getV()[3] = 6;
		putOpcodeInMemory(0xE39E);
		chip8.runNextInstruction();
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeEXA1SkipInstructionWhenKeyInVXIsNotPressed() {
		chip8.getV()[3] = 6;
		putOpcodeInMemory(0xE3A1);
		chip8.runNextInstruction();
		assertEquals(516, chip8.getPc());
	}

	@Test
	public void opcodeEXA1DoNotSkipInstructionWhenKeyInVXIsPressed() {
		chip8.getV()[3] = 6;
		// Key with index 6 pressed
		chip8.getKeyboard().pressKey(KeyCode.NUMPAD6);
		putOpcodeInMemory(0xE3A1);
		chip8.runNextInstruction();
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX07SetVXToDelayTimer() {
		chip8.getDelayTimer().setTime(14);
		putOpcodeInMemory(0xF807);
		chip8.runNextInstruction();
		assertEquals(14, chip8.getV()[8]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX0AStoreKeyPressedIntoVXAndMoveToNextInstruction() {
		// Key with index 3 pressed
		chip8.getKeyboard().pressKey(KeyCode.NUMPAD9);
		putOpcodeInMemory(0xF90A);
		chip8.runNextInstruction();
		assertEquals(3, chip8.getV()[9]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX0ADoNotMoveToNextInstructionIfNoKeyPressed() {
		putOpcodeInMemory(0xF90A);
		chip8.runNextInstruction();
		assertEquals(0, chip8.getV()[9]);
		assertEquals(512, chip8.getPc());
	}

	@Test
	public void opcodeFX15SetDelayTimerToVX() {
		chip8.getV()[10] = 65;
		putOpcodeInMemory(0xFA15);
		chip8.runNextInstruction();
		assertEquals(65, chip8.getDelayTimer().getTime());
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX18SetSoundTimerToVX() {
		chip8.getV()[11] = 78;
		putOpcodeInMemory(0xFB18);
		chip8.runNextInstruction();
		assertEquals(78, chip8.getSoundTimer().getTime());
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX1EAddVXToIOverflowSetVFTo1() {
		chip8.setI(0xFF2);
		chip8.getV()[3] = 0x12;
		putOpcodeInMemory(0xF31E);
		chip8.runNextInstruction();
		assertEquals(4, chip8.getI());
		assertEquals(1, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX1EAddVXToINoOverflowSetVFTo0() {
		chip8.setI(0xFF2);
		chip8.getV()[3] = 2;
		putOpcodeInMemory(0xF31E);
		chip8.runNextInstruction();
		assertEquals(0xFF4, chip8.getI());
		assertEquals(0, chip8.getV()[0xF]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory0() {
		chip8.getV()[4] = 0;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(0, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory1() {
		chip8.getV()[4] = 1;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(5, chip8.getI());
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory2() {
		chip8.getV()[4] = 2;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(2 * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory3() {
		chip8.getV()[4] = 3;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(3 * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory4() {
		chip8.getV()[4] = 4;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(4 * 5, chip8.getI());
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory5() {
		chip8.getV()[4] = 5;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(5 * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory6() {
		chip8.getV()[4] = 6;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(6 * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory7() {
		chip8.getV()[4] = 7;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(7 * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory8() {
		chip8.getV()[4] = 8;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(8 * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemory9() {
		chip8.getV()[4] = 9;
		chip8.setI(10);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(9 * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b0001_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemoryA() {
		chip8.getV()[4] = 0xA;
		chip8.setI(1);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(0xA * 5, chip8.getI());
		assertEquals(0b0110_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemoryB() {
		chip8.getV()[4] = 0xB;
		chip8.setI(1);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(0xB * 5, chip8.getI());
		assertEquals(0b1110_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1110_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1110_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemoryC() {
		chip8.getV()[4] = 0xC;
		chip8.setI(1);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(0xC * 5, chip8.getI());
		assertEquals(0b0111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b0111_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemoryD() {
		chip8.getV()[4] = 0xD;
		chip8.setI(1);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(0xD * 5, chip8.getI());
		assertEquals(0b1110_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1001_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1110_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemoryE() {
		chip8.getV()[4] = 0xE;
		chip8.setI(1);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(0xE * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1110_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX29SetIToLocationOfVXInMemoryF() {
		chip8.getV()[4] = 0xF;
		chip8.setI(1);
		putOpcodeInMemory(0xF429);
		chip8.runNextInstruction();
		assertEquals(0xF * 5, chip8.getI());
		assertEquals(0b1111_0000, chip8.getMemory()[chip8.getI()]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 1]);
		assertEquals(0b1110_0000, chip8.getMemory()[chip8.getI() + 2]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 3]);
		assertEquals(0b1000_0000, chip8.getMemory()[chip8.getI() + 4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX33StoreDecimalNumberInVXIntoMemoryAtIAndForward() {
		chip8.getV()[5] = 239;
		chip8.setI(90);
		putOpcodeInMemory(0xF533);
		chip8.runNextInstruction();
		assertEquals(2, chip8.getMemory()[90]);
		assertEquals(3, chip8.getMemory()[91]);
		assertEquals(9, chip8.getMemory()[92]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX55StoreStoreV0ToVXInMemoryAtIToIX() {
		chip8.getV()[0] = 10;
		chip8.getV()[1] = 2;
		chip8.getV()[2] = 34;
		chip8.getV()[3] = 1;
		chip8.getV()[4] = 9;
		chip8.setI(100);
		putOpcodeInMemory(0xF455);
		chip8.runNextInstruction();
		assertEquals(10, chip8.getMemory()[100]);
		assertEquals(2, chip8.getMemory()[101]);
		assertEquals(34, chip8.getMemory()[102]);
		assertEquals(1, chip8.getMemory()[103]);
		assertEquals(9, chip8.getMemory()[104]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void opcodeFX65StoreStoreMemoryAtIToIXInV0ToVX() {
		chip8.getMemory()[100] = 10;
		chip8.getMemory()[101] = 2;
		chip8.getMemory()[102] = 34;
		chip8.getMemory()[103] = 1;
		chip8.getMemory()[104] = 9;
		chip8.setI(100);
		putOpcodeInMemory(0xF465);
		chip8.runNextInstruction();
		assertEquals(10, chip8.getV()[0]);
		assertEquals(2, chip8.getV()[1]);
		assertEquals(34, chip8.getV()[2]);
		assertEquals(1, chip8.getV()[3]);
		assertEquals(9, chip8.getV()[4]);
		assertEquals(514, chip8.getPc());
	}

	@Test
	public void unknownOpcodeInGroup0GeneratesError() {
		try {
			putOpcodeInMemory(0x0011);
			chip8.runNextInstruction();
			fail();
		} catch (final UnknownOpcodeException e) {
			assertEquals("Unknown opcode 0x11", e.getMessage());
		}
	}

	@Test
	public void unknownOpcodeInGroup5GeneratesError() {
		try {
			putOpcodeInMemory(0x5A23);
			chip8.runNextInstruction();
			fail();
		} catch (final UnknownOpcodeException e) {
			assertEquals("Unknown opcode 0x5a23", e.getMessage());
		}
	}

	@Test
	public void unknownOpcodeInGroup8GeneratesError() {
		try {
			putOpcodeInMemory(0x8A29);
			chip8.runNextInstruction();
			fail();
		} catch (final UnknownOpcodeException e) {
			assertEquals("Unknown opcode 0x8a29", e.getMessage());
		}
	}

	@Test
	public void unknownOpcodeInGroup9GeneratesError() {
		try {
			putOpcodeInMemory(0x9A21);
			chip8.runNextInstruction();
			fail();
		} catch (final UnknownOpcodeException e) {
			assertEquals("Unknown opcode 0x9a21", e.getMessage());
		}
	}

	@Test
	public void unknownOpcodeInGroupEGeneratesError() {
		try {
			putOpcodeInMemory(0xEA21);
			chip8.runNextInstruction();
			fail();
		} catch (final UnknownOpcodeException e) {
			assertEquals("Unknown opcode 0xea21", e.getMessage());
		}
	}

	@Test
	public void unknownOpcodeInGroupFGeneratesError() {
		try {
			putOpcodeInMemory(0xFA21);
			chip8.runNextInstruction();
			fail();
		} catch (final UnknownOpcodeException e) {
			assertEquals("Unknown opcode 0xfa21", e.getMessage());
		}
	}

	@Test
	public void loadProgramIntoMemory() {
		final int startOfProgram = 512;
		final byte[] program = new byte[] { 50, 60, 12, 1, 8, 6, 4, 5, -120 };
		chip8.loadProgram(program);
		for (int i = startOfProgram; i < startOfProgram + program.length; i++) {
			assertEquals(program[i - startOfProgram] & 0xFF, chip8.getMemory()[i]);
		}
	}

	public void putOpcodeInMemory(final int opcode) {
		chip8.getMemory()[chip8.getPc()] = (opcode >> 8) & 0xFF;
		chip8.getMemory()[chip8.getPc() + 1] = opcode & 0xFF;
	}
}
