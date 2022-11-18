package kee.wkeec482.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import kee.wkeec482.model.InHouse;
import kee.wkeec482.model.Inventory;
import kee.wkeec482.model.Outsourced;
import kee.wkeec482.model.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * The type Add part controller.
 * @author williamkee
 */
public class AddPartController {

    /**
     * The Part In House radio button.
     */
    public RadioButton partInHouseRadio;
    /**
     * The Part outsourced radio.
     */
    public RadioButton partOutsourcedRadio;
    /**
     * The Part machine id and company name label.
     */
    public Label partMachineIDCompanyNameLabel;
    /**
     * The Part id text field.
     */
    public TextField partIdTextField;
    /**
     * The Part name text field.
     */
    public TextField partNameTextField;
    /**
     * The Part inventory text field.
     */
    public TextField partInvTextField;
    /**
     * The Part price/cost text field.
     */
    public TextField partPriceCostTextField;
    /**
     * The Part max text field.
     */
    public TextField partMaxTextField;
    /**
     * The Machine id or company text field.
     */
    public TextField machineIdCompanyTextField;
    /**
     * The Part min text field.
     */
    public TextField partMinTextField;
    /**
     * The Part min label.
     */
    public Label partMinLabel;
    /**
     * The In house outsourced.
     */
    public ToggleGroup inHouseOutsourced;

    /**
     * The constant nextPartID.
     */
    public static int nextPartID = 4;

    /**
     * The In-house boolean.
     */
    public boolean inHouse = true;

    /**
     * On in house part radio button.
     *
     * @param actionEvent radio button select
     */
    public void onInHousePart(ActionEvent actionEvent) {
        partMachineIDCompanyNameLabel.setText("Machine ID");
        inHouse = true;
    }

    /**
     * On outsourced part radio button.
     *
     * @param actionEvent radio button select
     */
    public void onOutsourcedPart(ActionEvent actionEvent) {
        partMachineIDCompanyNameLabel.setText("Company Name");
        inHouse = false;
    }

    /**
     * On save part.
     *
     * @param actionEvent button press
     * @throws IOException Number Format Exception
     */
    public void onSavePart(ActionEvent actionEvent) throws IOException {
        try {
            int id = nextPartID;
            String name = partNameTextField.getText();
            int inv = Integer.parseInt(partInvTextField.getText());
            double cost = Double.parseDouble(partPriceCostTextField.getText());
            int max = Integer.parseInt(partMaxTextField.getText());
            int min = Integer.parseInt(partMinTextField.getText());
            nextPartID += 1;

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
            if (inHouse) {
                int machineId = Integer.parseInt(machineIdCompanyTextField.getText());
                InHouse newPart = new InHouse(id, name, cost, inv, min, max, machineId);
                Inventory.addPart(newPart);
                machineIdCompanyTextField.setText("");
            } else {
                String companyName = machineIdCompanyTextField.getText();
                Outsourced newPart = new Outsourced(id, name, cost, inv, min, max, companyName);
                Inventory.addPart(newPart);
                machineIdCompanyTextField.setText("");
            }
            Parent addPart = FXMLLoader.load(getClass().getResource("/kee/wkeec482/MainScreen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(addPart);
            stage.setScene(scene);
            stage.show();
        }
        catch (NumberFormatException e) {
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
     * On cancel part.
     * Sends user back to main screen.
     * @param actionEvent button press
     * @throws IOException Null Pointer Exception
     */
    public void onCancelPart(ActionEvent actionEvent) throws IOException {
        Parent addPart = FXMLLoader.load(getClass().getResource("/kee/wkeec482/MainScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(addPart);
        stage.setScene(scene);
        stage.show();
    }
}