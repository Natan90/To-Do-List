package todo.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class  AddTacheController {
    @FXML
    private TextField titreField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField completedField;
    @FXML
    private TextField dateField;

    @FXML
    private Button retourButton;


    @FXML
    private void handleAjouter() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        String date = dateField.getText();


        if (!titre.isEmpty() && !description.isEmpty() && !date.isEmpty()){
            TodoController.handleAddTask(titre, description, date);
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.close();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/todo/demo/to-do-view.fxml"));
                Parent root = loader.load();
                Stage stagePrincipal = new Stage();
                stagePrincipal.setScene(new Scene(root));
                stagePrincipal.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/todo/demo/to-do-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setScene(new Scene(root, 400, 400));
            stage.show();

            Stage currentStage = (Stage) titreField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
