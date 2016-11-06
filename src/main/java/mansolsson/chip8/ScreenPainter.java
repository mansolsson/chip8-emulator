package mansolsson.chip8;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class ScreenPainter extends AnimationTimer {
	private final Chip8 chip8;
	private final GraphicsContext graphicsContext;

	public ScreenPainter(final Chip8 chip8, final GraphicsContext graphicsContext) {
		this.chip8 = chip8;
		this.graphicsContext = graphicsContext;
	}

	@Override
	public void handle(final long currentNanoTime) {
		graphicsContext.clearRect(0, 0, Screen.WIDTH * Screen.SCALE, Screen.HEIGHT * Screen.SCALE);
		final boolean[][] screen = chip8.getScreen().getPixels();
		for (int x = 0; x < screen.length; x++) {
			for (int y = 0; y < screen[x].length; y++) {
				if (screen[x][y]) {
					graphicsContext.fillRect(x * Screen.SCALE, y * Screen.SCALE, Screen.SCALE, Screen.SCALE);
				}
			}
		}
	}
}
