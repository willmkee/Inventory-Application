package kee.wkeec482;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kee.wkeec482.model.Part;
import kee.wkeec482.model.Inventory;
import kee.wkeec482.model.Outsourced;
import kee.wkeec482.model.Product;
import kee.wkeec482.model.InHouse;
import java.io.IOException;

/**
 * The type Main.
 * @author williamkee
 */
public class Main extends Application {
    /**
     * Loads the FXML stage for the Main Screen.
     * @param stage
     * @throws IOException Null Pointer Exception
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The entry point of application.
     * Creates sample products and parts
     * FUTURE ENHANCEMENT: I think the biggest enhancement that could be made
     * would be to attach it to some sort of database so that the entire
     * inventory could be seen at one time, with products and associated parts.
     * It would also allow the added information to be saved after the project was exited.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        //Include Test Data
        Part wheel = new Outsourced(1, "wheel", 25.00, 50, 1, 100, "Trek");
        Inventory.addPart(wheel);
        Part brakes = new InHouse(2, "brakes", 50.00, 19, 1, 1300, 1);
        Inventory.addPart(brakes);
        Part seat = new InHouse(3, "seat", 10.00, 19, 1, 1300, 2);
        Inventory.addPart(seat);
        Product bicycle = new Product(1000, "bicycle", 300.00, 10, 1, 50);
        bicycle.addAssociatedPart(wheel);
        bicycle.addAssociatedPart(brakes);
        bicycle.addAssociatedPart(seat);
        Inventory.addProduct(bicycle);
        Product tricycle = new Product(1001, "tricycle", 150.00, 25, 1, 193);
        tricycle.addAssociatedPart(wheel);
        tricycle.addAssociatedPart(seat);
        Inventory.addProduct(tricycle);
        launch();


    }
}