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
import kee.wkeec482.model.*;
import kee.wkeec482.model.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import static kee.wkeec482.model.Inventory.lookupPart;

/**
 * The type Modify product controller.
 * @author williamkee
 */
public class ModifyProductController implements Initializable {
    /**
     * The product id text field.
     */
    public TextField modifyProductIdTextField;
    /**
     * The product name text field.
     */
    public TextField modifyProductNameTextField;
    /**
     * The product inventory text field.
     */
    public TextField modifyProductInventoryTextField;
    /**
     * The product price text field.
     */
    public TextField modifyProductPriceTextField;
    /**
     * The product max text field.
     */
    public TextField modifyProductMaxTextField;
    /**
     * The product min text field.
     */
    public TextField modifyProductMinTextField;
    /**
     * The Search by part id text field.
     */
    public TextField searchByPartIdTextField;
    /**
     * The parts table table.
     */
    public TableView modifyProductTable;
    /**
     * The part id column.
     */
    public TableColumn modifyProductPartIDCol;
    /**
     * The part name column.
     */
    public TableColumn modifyProductPartNameCol;
    /**
     * The part inventory column.
     */
    public TableColumn modifyProductInvCol;
    /**
     * The part price column.
     */
    public TableColumn modifyProductPriceCol;
    /**
     * The associated parts table.
     */
    public TableView afterModifyProductTable;
    /**
     * The associated part id column.
     */
    public TableColumn afterModifyProductPartIDCol;
    /**
     * The associated part name column.
     */
    public TableColumn afterModifyProductPartNameCol;
    /**
     * The associated part inventory column.
     */
    public TableColumn afterModifyProductInvCol;
    /**
     * The associated part price column.
     */
    public TableColumn afterModifyProductPriceCol;

    /**
     * The Modified product.
     */
    public Product modifiedProduct = null;

    /**
     * The Selected index.
     */
    int selectedIndex = 0;

    /**
     * The associated parts list.
     */
    public ObservableList<Part> modifyPartsList = FXCollections.observableArrayList();

    /**
     * Initializes the two tables on the page.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        modifyProductTable.setItems(Inventory.getAllParts());
        modifyProductPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        afterModifyProductTable.setItems(modifyPartsList);
        afterModifyProductPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        afterModifyProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        afterModifyProductInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        afterModifyProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * On search by part id.
     * Searches by part id or name.
     * @param actionEvent enter
     * @throws IOException Number Format Exception
     */
    public void onSearchByPartId(ActionEvent actionEvent) throws IOException {
        String q = searchByPartIdTextField.getText();
        ObservableList<Part> parts = lookupPart(q);

        if(parts.size() == 0) {
            try {
                int id = Integer.parseInt(q);
                Part p = Inventory.lookupPart(id);
                if (p != null) {
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

        modifyProductTable.setItems(parts);
        searchByPartIdTextField.setText("");
    }


    /**
     * On add part.
     * Adds part to table and to product being modified.
     * RUNTIME ERROR: When I originally did this, it added the parts to the
     * table, but if I went back to the main screen and came back, then the
     * product would have NO associated parts. I was moving the part to the table,
     * but I was not adding it to the product. I had to change lines 200 and 201.
     * @param actionEvent button press
     */
    public void onAddProduct(ActionEvent actionEvent) {
        Part selectedPart = (Part) modifyProductTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Select part from list");
            alert.showAndWait();
            return;
        }
        else {
            modifyPartsList.add(selectedPart);
            afterModifyProductTable.setItems(modifyPartsList);
        }
    }

    /**
     * On remove associated part.
     * Removes part from table and product being modified.
     * @param actionEvent button press.
     */
    public void onRemoveAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) afterModifyProductTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Select part from list");
            alert.showAndWait();
            return;
        }
        else {
            modifyPartsList.remove(selectedPart);
            afterModifyProductTable.setItems(modifyPartsList);

        }
    }

    /**
     * On save modified product.
     * Creates a new product and updates inventory with the new product.
     * @param actionEvent button press
     * @throws IOException Number Format Exception
     */
    public void onSaveAddProduct(ActionEvent actionEvent) throws IOException{
        try {
            Product product = Inventory.lookupProduct(modifiedProduct.getId());
            String name = modifyProductNameTextField.getText();
            int id = modifiedProduct.getId();
            int inv = Integer.parseInt(modifyProductInventoryTextField.getText());
            double cost = Double.parseDouble(modifyProductPriceTextField.getText());
            int max = Integer.parseInt(modifyProductMaxTextField.getText());
            int min = Integer.parseInt(modifyProductMinTextField.getText());

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

            product = new Product(id, name, cost, inv, min, max);
            Inventory.updateProduct(selectedIndex, product);
            for (Part part: modifyPartsList) {
                product.addAssociatedPart(part);
            }

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
     * Returns user back to the main screen
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

    /**
     * Modify product.
     * Sends the product from the main screen to the modify product screen
     * @param selectedIndex index of selected product
     * @param selectedItem  the selected product
     */
    public void modifyProduct(int selectedIndex, Product selectedItem) {
        this.selectedIndex = selectedIndex;
        this.modifiedProduct = selectedItem;
        modifyProductIdTextField.setText(String.valueOf(modifiedProduct.getId()));
        modifyProductNameTextField.setText(modifiedProduct.getName());
        modifyProductInventoryTextField.setText(String.valueOf(modifiedProduct.getStock()));
        modifyProductPriceTextField.setText(String.valueOf(modifiedProduct.getPrice()));
        modifyProductMaxTextField.setText(String.valueOf(modifiedProduct.getMax()));
        modifyProductMinTextField.setText(String.valueOf(modifiedProduct.getMin()));

       modifyPartsList.addAll(modifiedProduct.getAllAssociatedParts());

    }
}