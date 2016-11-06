package mansolsson.chip8;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class ScreenPainter extends AnimationTimer {
	private Chip8 chip8;
	private GraphicsContext graphicsContext;

	public ScreenPainter(Chip8 chip8, GraphicsContext graphicsContext) {
		this.chip8 = chip8;
		this.graphicsContext = graphicsContext;
	}

	@Override
	public void handle(long currentNanoTime) {
		graphicsContext.clearRect(0, 0, Screen.WIDTH * Screen.SCALE, Screen.HEIGHT * Screen.SCALE);
		boolean[][] screen = chip8.getScreen().getPixels();
		for (int x = 0; x < screen.length; x++) {
			for (int y = 0; y < screen[x].length; y++) {
				if (screen[x][y]) {
					graphicsContext.fillRect(x * Screen.SCALE, y * Screen.SCALE, Screen.SCALE, Screen.SCALE);
				}
			}
		}
	}
}
