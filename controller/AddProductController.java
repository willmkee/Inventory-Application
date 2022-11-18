package kee.wkeec482.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import kee.wkeec482.model.Inventory;
import kee.wkeec482.model.Part;
import kee.wkeec482.model.Product;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import static kee.wkeec482.model.Inventory.lookupPart;


/**
 * The type Add product controller.
 * @author williamkee
 */
public class AddProductController implements Initializable {
    /**
     * The product id text field.
     */
    public TextField addProductIdTextField;
    /**
     * The product name text field.
     */
    public TextField addProductNameTextField;
    /**
     * The product inventory text field.
     */
    public TextField addProductInventoryTextField;
    /**
     * The product price text field.
     */
    public TextField addProductPriceTextField;
    /**
     * The product max text field.
     */
    public TextField addProductMaxTextField;
    /**
     * The product min text field.
     */
    public TextField addProductMinTextField;
    /**
     * The Search by part id text field.
     */
    public TextField searchByPartIdTextField;
    /**
     * The Add product parts table.
     */
    public TableView addProductTable;
    /**
     * The part id column.
     */
    public TableColumn addProductPartIDCol;
    /**
     * The part name column.
     */
    public TableColumn addProductPartNameCol;
    /**
     * The part inventory column.
     */
    public TableColumn addProductInvCol;
    /**
     * The part price column.
     */
    public TableColumn addProductPriceCol;
    /**
     * The associated parts table.
     */
    public TableView afterAddProductTable;
    /**
     * The associated part id column.
     */
    public TableColumn afterAddProductPartIDCol;
    /**
     * The associated part name column.
     */
    public TableColumn afterAddProductPartNameCol;
    /**
     * The associated part inventory column.
     */
    public TableColumn afterAddProductInvCol;
    /**
     * The associated part cost column.
     */
    public TableColumn afterAddProductCostCol;

    /**
     * The product parts list.
     */
    public ObservableList<Part> modifyPartsList = FXCollections.observableArrayList();
    /**
     * The constant nextProductID.
     */
    public static int nextProductID = 1002;

    /**
     * Initializes part and associated parts tables.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProductTable.setItems(Inventory.getAllParts());
        addProductPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        afterAddProductTable.setItems(modifyPartsList);
        afterAddProductPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        afterAddProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        afterAddProductInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        afterAddProductCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * On search by part id.
     * Searches part table by name or ID.
     * @param actionEvent enter
     * @throws IOException Part does not exist error.
     */
    public void onSearchByPartId(ActionEvent actionEvent) throws IOException{
        String q = searchByPartIdTextField.getText();
        ObservableList<Part> parts = lookupPart(q);

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

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Query Error");
                alert.setHeaderText("Query Error");
                alert.setContentText("Part does not exist");

                alert.showAndWait();
                return;
            }
        }

        addProductTable.setItems(parts);
        searchByPartIdTextField.setText("");
    }

    /**
     * On add part.
     * Adds part to product and associated parts table.
     * @param actionEvent button press.
     */
    public void onAddProduct(ActionEvent actionEvent) {
        Part selectedPart = (Part) addProductTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Select part from list");
            alert.showAndWait();
            return;
        }
        else {
            modifyPartsList.add(selectedPart);
            afterAddProductTable.setItems(modifyPartsList);
        }
    }

    /**
     * On remove associated part.
     * Removes part from product and from table.
     * @param actionEvent button press.
     */
    public void onRemoveAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) afterAddProductTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Select part from list");
            alert.showAndWait();
            return;
        }
        else {
            modifyPartsList.remove(selectedPart);
            afterAddProductTable.setItems(modifyPartsList);
        }
    }

    /**
     * On save add product.
     *
     * @param actionEvent button press
     * @throws IOException Number Format Exception.
     */
    public void onSaveAddProduct(ActionEvent actionEvent) throws IOException{
        try {
            String name = addProductNameTextField.getText();
            int id = nextProductID;
            int inv = Integer.parseInt(addProductInventoryTextField.getText());
            double cost = Double.parseDouble(addProductPriceTextField.getText());
            int max = Integer.parseInt(addProductMaxTextField.getText());
            int min = Integer.parseInt(addProductMinTextField.getText());

            if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Min/Max Error");
                alert.setContentText("Maximum must be greater than or equal to minimum");

                alert.showAndWait();
                return;
            }
            if ((min > inv) || (inv > max)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Inventory Error");
                alert.setContentText("Inventory must be between max and min");

                alert.showAndWait();
                return;
            }

            Product newProduct = new Product(id, name, cost, inv, min, max);
            Inventory.addProduct(newProduct);
            for (Part part: modifyPartsList) {
                newProduct.addAssociatedPart(part);
            }
            nextProductID += 1;

            Parent addProduct = FXMLLoader.load(getClass().getResource("/kee/wkeec482/MainScreen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(addProduct);
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Number Format Exception");
            alert.setHeaderText("Number Format Exception");
            alert.setContentText("Incorrect input type");

            Exception ex = new NumberFormatException("Incorrect Input Type");

// Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }
    }

    /**
     * On cancel add product.
     * Sends user back to main screen.
     * @param actionEvent button press
     * @throws IOException Null Pointer Exception
     */
    public void onCancelAddProduct(ActionEvent actionEvent) throws IOException {
        Parent addPart = FXMLLoader.load(getClass().getResource("/kee/wkeec482/MainScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(addPart);
        stage.setScene(scene);
        stage.show();
    }
}