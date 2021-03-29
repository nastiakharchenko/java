import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.log4j.Log4j;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import repository.OrganizationRepository;
import repository.OrganizationRepositoryImpl;

import java.io.IOException;
import java.util.Arrays;

import static config.Constants.*;

@Log4j
public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Organization");

        initRootLayout();

        showOrganizationOverview();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOrganizationOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("OrganizationOverview.fxml"));
            AnchorPane organizationOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(organizationOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(final String[] args) {
        if (args.length == 1) {
            OrganizationRepository repository = new OrganizationRepositoryImpl();
            repository.importFromCsv(args[0]);
            System.out.println("\nUnsorted array: ");
            System.out.println(repository.toString());
            System.out.println("\nSorted array: ");
            repository.sort();
            System.out.println(repository.toString());
            repository.exportToJson(args[0].substring(0, args[0].lastIndexOf(DOT)) + JSON);
        } else {
            log.debug(RUN_WITH_WRONG_ARGUMENTS + Arrays.toString(args));
            System.out.println(ENTER_VALID_ARGUMENT_MESSAGE);
        }
        launch(args);
    }
}