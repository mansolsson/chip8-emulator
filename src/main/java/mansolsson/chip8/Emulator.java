package mansolsson.chip8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Emulator extends Application {
	private OpcodeRunnerThread opcodeRunnerThread;
	private TimerCountdownThread timerCountdownThread;
	private ScreenPainter chip8AnimationTimer;
	private Chip8 chip8 = new Chip8();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		Canvas canvas = new Canvas(Screen.WIDTH * Screen.SCALE, Screen.HEIGHT * Screen.SCALE);
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

		VBox vbox = new VBox();
		vbox.getChildren().addAll(createMenuBar(stage, graphicsContext), canvas);

		stage.setTitle("Chip-8 Emulator");
		stage.setScene(createScene(vbox));
		stage.show();
	}

	private Scene createScene(VBox vbox) {
		Scene scene = new Scene(vbox);
		scene.setOnKeyPressed(keyEvent -> {
			chip8.getKeyboard().pressKey(keyEvent.getCode());
		});
		scene.setOnKeyReleased(keyEvent -> {
			chip8.getKeyboard().releaseKey(keyEvent.getCode());
		});
		return scene;
	}

	private MenuBar createMenuBar(Stage stage, GraphicsContext graphicsContext) {
		MenuItem menuItem = new MenuItem("Load Chip-8 Program");
		menuItem.setOnAction(actionEvent -> {
			File file = chooseFile(stage);
			if (file != null) {
				stopCurrentProgram();
				startNewProgram(graphicsContext, file);
			}
		});

		Menu menu = new Menu("File");
		menu.getItems().addAll(menuItem);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menu);
		return menuBar;
	}

	private File chooseFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Chip-8 File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Chip-8 Files", "*.ch8"),
				new ExtensionFilter("All Files", "*.*"));
		return fileChooser.showOpenDialog(stage);
	}

	private void stopCurrentProgram() {
		if (opcodeRunnerThread != null) {
			opcodeRunnerThread.shutdownThread();
		}
		if (timerCountdownThread != null) {
			timerCountdownThread.shutdownThread();
		}
	}

	private void startNewProgram(GraphicsContext graphicsContext, File file) {
		try {
			chip8.init();
			byte[] programContent = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
			chip8.loadProgram(programContent);
			opcodeRunnerThread = new OpcodeRunnerThread(chip8);
			timerCountdownThread = new TimerCountdownThread(chip8);
			if (chip8AnimationTimer == null) {
				chip8AnimationTimer = new ScreenPainter(chip8, graphicsContext);
			}
			chip8AnimationTimer.start();
			opcodeRunnerThread.start();
			timerCountdownThread.start();
		} catch (IOException e) {
			System.err.println("Failed to read file " + e.getMessage());
		}
	}

	@Override
	public void stop() {
		stopCurrentProgram();
	}
}
