package util.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.tinylog.Logger;

import java.io.IOException;

/**
 * Provides helper methods for controllers.
 */
public class ControllerHelper {

    /**
     * Loads an FXML resource from the classpath using the {@code FXMLLoader}
     * instance specified and shows the resulting scene on a stage.
     *
     * @param fxmlLoader the {@code FXMLLoader} instance
     * @param resourceName the name of the FXML resource in the classpath
     * @param stage the stage on which the resulting scene is shown
     * @throws IOException if any I/O error occurs
     */
    public static void loadAndShowFXML(FXMLLoader fxmlLoader, String resourceName, Stage stage) throws IOException {
        Logger.trace("Loading FXML resource {}", resourceName);
        fxmlLoader.setLocation(fxmlLoader.getClass().getResource(resourceName));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
