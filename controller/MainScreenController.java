package kee.wkeec482.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import kee.wkeec482.model.Inventory;
import javafx.fxml.Initializable;
import kee.wkeec482.model.Part;
import kee.wkeec482.model.Product;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import static kee.wkeec482.model.Inventory.lookupPart;
import static kee.wkeec482.model.Inventory.lookupProduct;

/**
 * The Main screen controller.
 * @author williamkee
 */
public class MainScreenController implements Initializable{
    /**
     * The Part search text field.
     */
    public TextField partSearchTextField;
    /**
     * The Part id column.
     */
    public TableColumn partIdCol;
    /**
     * The Part name column.
     */
    public TableColumn partNameCol;
    /**
     * The Part inventory level column.
     */
    public TableColumn partInventoryLevelCol;
    /**
     * The Part price cost column.
     */
    public TableColumn partPriceCostCol;
    /**
     * The Product search text field.
     */
    public TextField productSearchTextField;
    /**
     * The Product id column.
     */
    public TableColumn productIdCol;
    /**
     * The Product name column.
     */
    public TableColumn productNameCol;
    /**
     * The Product inventory column.
     */
    public TableColumn productInventoryCol;
    /**
     * The Product price cost column.
     */
    public TableColumn productPriceCostCol;
    /**
     * The Parts table.
     */
    public TableView partsTable;
    /**
     * The Product table.
     */
    public TableView productTable;

    /**
     * Initializes Part and Product tables
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    /**
     * Searches part table by name or ID.
     *
     * @param actionEvent button press.
     * @throws IOException dialogue box.
     */
    public void onSearchPart(ActionEvent actionEvent) throws IOException{
        String q = partSearchTextField.getText();
        ObservableList<Part> parts = Inventory.lookupPart(q);
        if(parts.size() == 0) {
            try {
            int id = Integer.parseInt(q);
            Part p = Inventory.lookupPart(id);
            if (p != null){
                parts.add(p);}
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Query Error");
                alert.setHeaderText("Query Error");
                alert.setContentText("Part does not exist");

                alert.showAndWait();
                return;
            }
        }
             catch (NumberFormatException e) {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Query Error");
                 alert.setHeaderText("Query Error");
                 alert.setContentText("Part does not exist");

                 alert.showAndWait();
                return;
            }
            }
        partsTable.setItems(parts);
        partSearchTextField.setText("");

    }


    /**
     * On add part button.
     * Opens add part window.
     * @param actionEvent button press.
     * @throws IOException Null Pointer Exception.
     */
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        Parent addPart = FXMLLoader.load(getClass().getResource("/kee/wkeec482/AddPart.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(addPart);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * On modify part button.
     * Opens modify part window.
     * @param actionEvent button press
     * @throws IOException Null Pointer Exception.
     */
    public void onModifyPart(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kee/wkeec482/ModifyPart.fxml"));
        Parent root = loader.load();
        ModifyPartController MPC = loader.getController();
        try {
            MPC.modifyPart(partsTable.getSelectionModel().getSelectedIndex(), (Part) partsTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Null Pointer Exception");
            alert.setHeaderText("No Part Selected");
            alert.setContentText("You must select a part to modify!");

            alert.showAndWait();
            return;
        }
    }


    /**
     * On delete part.
     * Removes part from table and inventory.
     * @param actionEvent button press
     */
    public void onDeletePart(ActionEvent actionEvent) {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedPart == null) {
                throw new NullPointerException();
            }
            Inventory.deletePart(selectedPart);
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Null Pointer Exception");
            alert.setHeaderText("No Part Selected");
            alert.setContentText("You must select a part to delete!");

            alert.showAndWait();
            return;
        }

    }

    /**
     * On product search.
     * Searchers for product by name or ID.
     * @param actionEvent enter.
     */
    public void onProductSearch(ActionEvent actionEvent) {
        String q = productSearchTextField.getText();
        ObservableList<Product> products = lookupProduct(q);

        if(products.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Product p = Inventory.lookupProduct(id);
                if (p != null) {
                    products.add(p);}
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Query Error");
                    alert.setHeaderText("Query Error");
                    alert.setContentText("Product does not exist");

                    alert.showAndWait();
                    return;
                }

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Query Error");
                alert.setHeaderText("Query Error");
                alert.setContentText("Product does not exist");

                alert.showAndWait();
                return;
            }
        }

        productTable.setItems(products);
        productSearchTextField.setText("");
    }

    /**
     * On add product.
     * Opens Add Product window.
     * @param actionEvent button press.
     * @throws IOException Null Pointer Exception
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent addPart = FXMLLoader.load(getClass().getResource("/kee/wkeec482/AddProduct.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(addPart);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * On modify product.
     * Opens Modify Product Window.
     * @param actionEvent button press
     * @throws IOException Null Pointer Exception
     */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kee/wkeec482/ModifyProduct.fxml"));
        Parent root = loader.load();
        ModifyProductController MPC = loader.getController();
        try {
            MPC.modifyProduct(productTable.getSelectionModel().getSelectedIndex(), (Product) productTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Null Pointer Exception");
            alert.setHeaderText("No Product Selected");
            alert.setContentText("You must select a product to modify!");

            alert.showAndWait();
            return;
        }
    }

    /**
     * On delete product.
     * Deletes product from table and inventory.
     * @param actionEvent button press.
     */
    public void onDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = (Product)productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct.getAllAssociatedParts().size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Product Failure!");
            alert.setHeaderText("Product may not be deleted");
            alert.setContentText("You must remove associated parts before deleting product!");

            alert.showAndWait();
            return;
        }
        else {
            try {
                if (selectedProduct == null) {
                    throw new NullPointerException();
                }
                Inventory.deleteProduct(selectedProduct);
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Null Pointer Exception");
                alert.setHeaderText("No Product Selected");
                alert.setContentText("You must select a product to delete!");

                alert.showAndWait();
                return;
            }
        }
    }

    /**
     * On exit.
     * Closes window and ends program.
     * @param actionEvent button press
     */
    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }



}