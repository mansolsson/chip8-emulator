package mansolsson.chip8;

public class Screen {
	public static final int HEIGHT = 32;
	public static final int WIDTH = 64;
	public static final int SCALE = 10;

	private boolean[][] pixels = new boolean[WIDTH][HEIGHT];

	public synchronized boolean getPixel(int x, int y) {
		return pixels[x][y];
	}

	public synchronized void clear() {
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				pixels[x][y] = false;
			}
		}
	}

	public synchronized void setPixel(int x, int y, boolean pixel) {
		pixels[x][y] = pixel;
	}

	public synchronized boolean drawSprite(int x, int y, int[] rows) {
		boolean pixelTurnedOff = false;
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < rows.length; row++) {
				int currentY = (y + row) % HEIGHT;
				int currentX = (x + column) % WIDTH;
				boolean screenValue = pixels[currentX][currentY];
				boolean spriteValue = ((rows[row] >> (7 - column)) & 1) == 1;
				pixels[currentX][currentY] = spriteValue ^ screenValue;
				if (screenValue && spriteValue) {
					pixelTurnedOff = true;
				}
			}
		}
		return pixelTurnedOff;
	}

	public synchronized boolean[][] getPixels() {
		return pixels.clone();
	}
}
