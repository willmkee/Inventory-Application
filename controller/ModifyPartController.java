package kee.wkeec482.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;


/**
 * The type Modify part controller.
 * @author williamkee
 */
public class ModifyPartController implements Initializable {
    /**
     * The Part in-house radio button.
     */
    public RadioButton partInHouseRadio;
    /**
     * The Part outsourced radio button.
     */
    public RadioButton partOutsourcedRadio;
    /**
     * The Part machine id or company name label.
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
     * The Part price text field.
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
     * The In house and outsourced radio buttons toggle group.
     */
    public ToggleGroup inHouseOutsourcedRadioButtons;

    /**
     * The Modified part.
     */
    public Part modifiedPart = null;

    /**
     * The Selected index.
     */
    int selectedIndex = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Modify part.
     * Sends the part info from the main screen to the modify part screen.
     * @param selectedIndex index of selected part
     * @param modifiedPart  the modified part
     */
    public void modifyPart(int selectedIndex, Part modifiedPart) {
        this.selectedIndex = selectedIndex;
        this.modifiedPart = modifiedPart;
        partIdTextField.setText(String.valueOf(modifiedPart.getId()));
        partNameTextField.setText(modifiedPart.getName());
        partInvTextField.setText(String.valueOf(modifiedPart.getStock()));
        partPriceCostTextField.setText(String.valueOf(modifiedPart.getPrice()));
        partMaxTextField.setText(String.valueOf(modifiedPart.getMax()));
        partMinTextField.setText(String.valueOf(modifiedPart.getMin()));
        if (modifiedPart instanceof InHouse) {
            partInHouseRadio.setSelected(true);
            partMachineIDCompanyNameLabel.setText("Machine ID");
            machineIdCompanyTextField.setText(String.valueOf(((InHouse) modifiedPart).getMachineId()));
        }
        else {
            partOutsourcedRadio.setSelected(true);
            partMachineIDCompanyNameLabel.setText("Company Name");
            machineIdCompanyTextField.setText(((Outsourced) modifiedPart).getCompanyName());
        }
    }

    /**
     * On in house part.
     * Sets textfield label to Machine ID.
     * @param actionEvent radio button selection
     */
    public void onInHousePart(ActionEvent actionEvent) {
        partMachineIDCompanyNameLabel.setText("Machine ID");
    }

    /**
     * On outsourced part.
     * Sets textfield label to Company Name.
     * @param actionEvent radio button selection
     */
    public void onOutsourcedPart(ActionEvent actionEvent) {
        partMachineIDCompanyNameLabel.setText("Company Name");
    }

    /**
     * On save modified part.
     * Creates a new part and then replaces the original part in the inventory.
     * @param actionEvent button press
     * @throws IOException Number Format Exception
     */
    public void onSaveModifiedPart(ActionEvent actionEvent) throws IOException {
        try {
            Part part = Inventory.lookupPart(modifiedPart.getId());
            String name = partNameTextField.getText();
            int id = modifiedPart.getId();
            int inv = Integer.parseInt(partInvTextField.getText());
            double cost = Double.parseDouble(partPriceCostTextField.getText());
            int max = Integer.parseInt(partMaxTextField.getText());
            int min = Integer.parseInt(partMinTextField.getText());

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
            if (part instanceof InHouse) {
                int machineId = Integer.parseInt(machineIdCompanyTextField.getText());
                InHouse newPart = new InHouse(id, name, cost, inv, max, min, machineId);
                Inventory.updatePart(selectedIndex, newPart);
                machineIdCompanyTextField.setText("");
            } else {
                String companyName = machineIdCompanyTextField.getText();
                Outsourced newPart = new Outsourced(id, name, cost, inv, max, min, companyName);
                Inventory.updatePart(selectedIndex, newPart);
                machineIdCompanyTextField.setText("");
            }
            Parent addPart = FXMLLoader.load(getClass().getResource("/kee/wkeec482/MainScreen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(addPart);
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
     * On cancel modified part.
     * Sends user back to main screen.
     * @param actionEvent button press.
     * @throws IOException Null Pointer Exception.
     */
    public void onCancelModifiedPart (ActionEvent actionEvent) throws IOException {
            Parent addPart = FXMLLoader.load(getClass().getResource("/kee/wkeec482/MainScreen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(addPart);
            stage.setScene(scene);
            stage.show();
        }
    }