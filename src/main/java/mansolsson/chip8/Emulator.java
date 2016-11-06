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
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Emulator extends Application {
    private OpcodeRunnerThread opcodeRunnerThread;
    private TimerCountdownThread timerCountdownThread;
    private ScreenPainter chip8AnimationTimer;
    private final Chip8 chip8 = new Chip8();

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws IOException {
        final Canvas canvas = new Canvas(Screen.WIDTH * Screen.SCALE, Screen.HEIGHT * Screen.SCALE);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        final VBox vbox = new VBox();
        vbox.getChildren().addAll(createMenuBar(stage, graphicsContext), canvas);

        stage.setTitle("Chip-8 Emulator");
        stage.setScene(createScene(vbox, graphicsContext));
        stage.show();
    }

    private Scene createScene(final VBox vbox, final GraphicsContext graphicsContext) {
        final Scene scene = new Scene(vbox);
        scene.setOnKeyPressed(keyEvent -> {
            chip8.getKeyboard().pressKey(keyEvent.getCode());
        });
        scene.setOnKeyReleased(keyEvent -> {
            chip8.getKeyboard().releaseKey(keyEvent.getCode());
        });
        scene.setOnDragOver(dragEvent -> {
            if (dragEvent.getDragboard().hasFiles()) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
            } else {
                dragEvent.consume();
            }
        });
        scene.setOnDragDropped(dragEvent -> {
            final Dragboard dragBoard = dragEvent.getDragboard();
            if (dragBoard.hasFiles()) {
                final File file = dragBoard.getFiles().get(0);
                if (file != null) {
                    stopCurrentProgram();
                    startNewProgram(graphicsContext, file);
                }
                dragEvent.setDropCompleted(true);
            } else {
                dragEvent.setDropCompleted(false);
            }
            dragEvent.consume();
        });
        return scene;
    }

    private MenuBar createMenuBar(final Stage stage, final GraphicsContext graphicsContext) {
        final MenuItem menuItem = new MenuItem("Load Chip-8 Program");
        menuItem.setOnAction(actionEvent -> {
            final File file = chooseFile(stage);
            if (file != null) {
                stopCurrentProgram();
                startNewProgram(graphicsContext, file);
            }
        });

        final Menu menu = new Menu("File");
        menu.getItems().addAll(menuItem);

        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu);
        return menuBar;
    }

    private File chooseFile(final Stage stage) {
        final FileChooser fileChooser = new FileChooser();
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
            timerCountdownThread.shutdown();
        }
    }

    private void startNewProgram(final GraphicsContext graphicsContext, final File file) {
        try {
            chip8.init();
            final byte[] programContent = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            chip8.loadProgram(programContent);
            opcodeRunnerThread = new OpcodeRunnerThread(chip8);
            timerCountdownThread = new TimerCountdownThread(chip8);
            if (chip8AnimationTimer == null) {
                chip8AnimationTimer = new ScreenPainter(chip8, graphicsContext);
            }
            chip8AnimationTimer.start();
            opcodeRunnerThread.start();
            timerCountdownThread.start();
        } catch (final IOException e) {
            System.err.println("Failed to read file " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        stopCurrentProgram();
    }
}
