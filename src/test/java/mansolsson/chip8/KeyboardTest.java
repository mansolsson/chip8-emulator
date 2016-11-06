package mansolsson.chip8;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.input.KeyCode;

public class KeyboardTest {
	private Keyboard keyboard;

	@Before
	public void setUp() {
		keyboard = new Keyboard();
	}

	@Test
	public void pressAndReleaseDecimalPressAndReleasesKeyWithIndex0() {
		keyboard.pressKey(KeyCode.DECIMAL);
		assertTrue(keyboard.isKeyPressed(0));
		keyboard.releaseKey(KeyCode.DECIMAL);
		assertFalse(keyboard.isKeyPressed(0));
	}

	@Test
	public void pressAndReleaseDeletePressAndReleasesKeyWithIndex0() {
		keyboard.pressKey(KeyCode.DELETE);
		assertTrue(keyboard.isKeyPressed(0));
		keyboard.releaseKey(KeyCode.DELETE);
		assertFalse(keyboard.isKeyPressed(0));
	}

	@Test
	public void pressAndReleaseNumpad7PressAndReleasesKeyWithIndex1() {
		keyboard.pressKey(KeyCode.NUMPAD7);
		assertTrue(keyboard.isKeyPressed(1));
		keyboard.releaseKey(KeyCode.NUMPAD7);
		assertFalse(keyboard.isKeyPressed(1));
	}

	@Test
	public void pressAndReleaseHomePressAndReleasesKeyWithIndex1() {
		keyboard.pressKey(KeyCode.HOME);
		assertTrue(keyboard.isKeyPressed(1));
		keyboard.releaseKey(KeyCode.HOME);
		assertFalse(keyboard.isKeyPressed(1));
	}

	@Test
	public void pressAndReleaseNumpad8PressAndReleasesKeyWithIndex2() {
		keyboard.pressKey(KeyCode.NUMPAD8);
		assertTrue(keyboard.isKeyPressed(2));
		keyboard.releaseKey(KeyCode.NUMPAD8);
		assertFalse(keyboard.isKeyPressed(2));
	}

	@Test
	public void pressAndReleaseUpPressAndReleasesKeyWithIndex2() {
		keyboard.pressKey(KeyCode.UP);
		assertTrue(keyboard.isKeyPressed(2));
		keyboard.releaseKey(KeyCode.UP);
		assertFalse(keyboard.isKeyPressed(2));
	}

	@Test
	public void pressAndReleaseNumpad9PressAndReleasesKeyWithIndex3() {
		keyboard.pressKey(KeyCode.NUMPAD9);
		assertTrue(keyboard.isKeyPressed(3));
		keyboard.releaseKey(KeyCode.NUMPAD9);
		assertFalse(keyboard.isKeyPressed(3));
	}

	@Test
	public void pressAndReleasePageUpPressAndReleasesKeyWithIndex3() {
		keyboard.pressKey(KeyCode.PAGE_UP);
		assertTrue(keyboard.isKeyPressed(3));
		keyboard.releaseKey(KeyCode.PAGE_UP);
		assertFalse(keyboard.isKeyPressed(3));
	}

	@Test
	public void pressAndReleaseNumpad9PressAndReleasesKeyWithIndex4() {
		keyboard.pressKey(KeyCode.NUMPAD4);
		assertTrue(keyboard.isKeyPressed(4));
		keyboard.releaseKey(KeyCode.NUMPAD4);
		assertFalse(keyboard.isKeyPressed(4));
	}

	@Test
	public void pressAndReleaseLeftPressAndReleasesKeyWithIndex4() {
		keyboard.pressKey(KeyCode.LEFT);
		assertTrue(keyboard.isKeyPressed(4));
		keyboard.releaseKey(KeyCode.LEFT);
		assertFalse(keyboard.isKeyPressed(4));
	}

	@Test
	public void pressAndReleaseNumpad5PressAndReleasesKeyWithIndex5() {
		keyboard.pressKey(KeyCode.NUMPAD5);
		assertTrue(keyboard.isKeyPressed(5));
		keyboard.releaseKey(KeyCode.NUMPAD5);
		assertFalse(keyboard.isKeyPressed(5));
	}

	@Test
	public void pressAndReleaseClearPressAndReleasesKeyWithIndex5() {
		keyboard.pressKey(KeyCode.CLEAR);
		assertTrue(keyboard.isKeyPressed(5));
		keyboard.releaseKey(KeyCode.CLEAR);
		assertFalse(keyboard.isKeyPressed(5));
	}

	@Test
	public void pressAndReleaseNumpad6PressAndReleasesKeyWithIndex6() {
		keyboard.pressKey(KeyCode.NUMPAD6);
		assertTrue(keyboard.isKeyPressed(6));
		keyboard.releaseKey(KeyCode.NUMPAD6);
		assertFalse(keyboard.isKeyPressed(6));
	}

	@Test
	public void pressAndReleaseRightPressAndReleasesKeyWithIndex6() {
		keyboard.pressKey(KeyCode.RIGHT);
		assertTrue(keyboard.isKeyPressed(6));
		keyboard.releaseKey(KeyCode.RIGHT);
		assertFalse(keyboard.isKeyPressed(6));
	}

	@Test
	public void pressAndReleaseNumpad1PressAndReleasesKeyWithIndex7() {
		keyboard.pressKey(KeyCode.NUMPAD1);
		assertTrue(keyboard.isKeyPressed(7));
		keyboard.releaseKey(KeyCode.NUMPAD1);
		assertFalse(keyboard.isKeyPressed(7));
	}

	@Test
	public void pressAndReleaseEndPressAndReleasesKeyWithIndex7() {
		keyboard.pressKey(KeyCode.END);
		assertTrue(keyboard.isKeyPressed(7));
		keyboard.releaseKey(KeyCode.END);
		assertFalse(keyboard.isKeyPressed(7));
	}

	@Test
	public void pressAndReleaseNumpad2PressAndReleasesKeyWithIndex8() {
		keyboard.pressKey(KeyCode.NUMPAD2);
		assertTrue(keyboard.isKeyPressed(8));
		keyboard.releaseKey(KeyCode.NUMPAD2);
		assertFalse(keyboard.isKeyPressed(8));
	}

	@Test
	public void pressAndReleaseDownPressAndReleasesKeyWithIndex8() {
		keyboard.pressKey(KeyCode.DOWN);
		assertTrue(keyboard.isKeyPressed(8));
		keyboard.releaseKey(KeyCode.DOWN);
		assertFalse(keyboard.isKeyPressed(8));
	}

	@Test
	public void pressAndReleaseNumpad3PressAndReleasesKeyWithIndex9() {
		keyboard.pressKey(KeyCode.NUMPAD3);
		assertTrue(keyboard.isKeyPressed(9));
		keyboard.releaseKey(KeyCode.NUMPAD3);
		assertFalse(keyboard.isKeyPressed(9));
	}

	@Test
	public void pressAndReleasePageDownPressAndReleasesKeyWithIndex9() {
		keyboard.pressKey(KeyCode.PAGE_DOWN);
		assertTrue(keyboard.isKeyPressed(9));
		keyboard.releaseKey(KeyCode.PAGE_DOWN);
		assertFalse(keyboard.isKeyPressed(9));
	}

	@Test
	public void pressAndReleaseNumpad0PressAndReleasesKeyWithIndex10() {
		keyboard.pressKey(KeyCode.NUMPAD0);
		assertTrue(keyboard.isKeyPressed(10));
		keyboard.releaseKey(KeyCode.NUMPAD0);
		assertFalse(keyboard.isKeyPressed(10));
	}

	@Test
	public void pressAndReleaseInsertPressAndReleasesKeyWithIndex10() {
		keyboard.pressKey(KeyCode.INSERT);
		assertTrue(keyboard.isKeyPressed(10));
		keyboard.releaseKey(KeyCode.INSERT);
		assertFalse(keyboard.isKeyPressed(10));
	}

	@Test
	public void pressAndReleaseEnterPressAndReleasesKeyWithIndex11() {
		keyboard.pressKey(KeyCode.ENTER);
		assertTrue(keyboard.isKeyPressed(11));
		keyboard.releaseKey(KeyCode.ENTER);
		assertFalse(keyboard.isKeyPressed(11));
	}

	@Test
	public void pressAndReleaseDividePressAndReleasesKeyWithIndex12() {
		keyboard.pressKey(KeyCode.DIVIDE);
		assertTrue(keyboard.isKeyPressed(12));
		keyboard.releaseKey(KeyCode.DIVIDE);
		assertFalse(keyboard.isKeyPressed(12));
	}

	@Test
	public void pressAndReleaseMultiplyPressAndReleasesKeyWithIndex13() {
		keyboard.pressKey(KeyCode.MULTIPLY);
		assertTrue(keyboard.isKeyPressed(13));
		keyboard.releaseKey(KeyCode.MULTIPLY);
		assertFalse(keyboard.isKeyPressed(13));
	}

	@Test
	public void pressAndReleaseSubtractPressAndReleasesKeyWithIndex14() {
		keyboard.pressKey(KeyCode.SUBTRACT);
		assertTrue(keyboard.isKeyPressed(14));
		keyboard.releaseKey(KeyCode.SUBTRACT);
		assertFalse(keyboard.isKeyPressed(14));
	}

	@Test
	public void pressAndReleaseAddPressAndReleasesKeyWithIndex15() {
		keyboard.pressKey(KeyCode.ADD);
		assertTrue(keyboard.isKeyPressed(15));
		keyboard.releaseKey(KeyCode.ADD);
		assertFalse(keyboard.isKeyPressed(15));
	}

	@Test
	public void canHandleKeyPressedAndReleasedThatIsNotMapped() {
		keyboard.pressKey(KeyCode.DIGIT1);
		keyboard.releaseKey(KeyCode.DIGIT1);
	}
}
