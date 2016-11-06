package mansolsson.chip8;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.input.KeyCode;

public class Keyboard {
	private static final Map<KeyCode, Integer> KEY_MAPPING = new ConcurrentHashMap<>();

	static {
		KEY_MAPPING.put(KeyCode.DECIMAL, 0);
		KEY_MAPPING.put(KeyCode.DELETE, 0);

		KEY_MAPPING.put(KeyCode.NUMPAD7, 1);
		KEY_MAPPING.put(KeyCode.HOME, 1);
		KEY_MAPPING.put(KeyCode.NUMPAD8, 2);
		KEY_MAPPING.put(KeyCode.UP, 2);
		KEY_MAPPING.put(KeyCode.NUMPAD9, 3);
		KEY_MAPPING.put(KeyCode.PAGE_UP, 3);

		KEY_MAPPING.put(KeyCode.NUMPAD4, 4);
		KEY_MAPPING.put(KeyCode.LEFT, 4);
		KEY_MAPPING.put(KeyCode.NUMPAD5, 5);
		KEY_MAPPING.put(KeyCode.CLEAR, 5);
		KEY_MAPPING.put(KeyCode.NUMPAD6, 6);
		KEY_MAPPING.put(KeyCode.RIGHT, 6);

		KEY_MAPPING.put(KeyCode.NUMPAD1, 7);
		KEY_MAPPING.put(KeyCode.END, 7);
		KEY_MAPPING.put(KeyCode.NUMPAD2, 8);
		KEY_MAPPING.put(KeyCode.DOWN, 8);
		KEY_MAPPING.put(KeyCode.NUMPAD3, 9);
		KEY_MAPPING.put(KeyCode.PAGE_DOWN, 9);

		KEY_MAPPING.put(KeyCode.NUMPAD0, 10);
		KEY_MAPPING.put(KeyCode.INSERT, 10);

		KEY_MAPPING.put(KeyCode.ENTER, 11);
		KEY_MAPPING.put(KeyCode.DIVIDE, 12);
		KEY_MAPPING.put(KeyCode.MULTIPLY, 13);
		KEY_MAPPING.put(KeyCode.SUBTRACT, 14);
		KEY_MAPPING.put(KeyCode.ADD, 15);
	}

	private final AtomicBoolean[] keys = new AtomicBoolean[16];

	public Keyboard() {
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new AtomicBoolean(false);
		}
	}

	public void pressKey(final KeyCode keyCode) {
		if (KEY_MAPPING.containsKey(keyCode)) {
			keys[KEY_MAPPING.get(keyCode)].set(true);
		}
	}

	public void releaseKey(final KeyCode keyCode) {
		if (KEY_MAPPING.containsKey(keyCode)) {
			keys[KEY_MAPPING.get(keyCode)].set(false);
		}
	}

	public boolean isKeyPressed(final int index) {
		return keys[index].get();
	}

	public int numberOfKeys() {
		return keys.length;
	}
}
