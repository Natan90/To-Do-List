package todo.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TodoController {

    @FXML
    private ListView<HBox> taskList;
    @FXML
    private TextField taskTitle;
    @FXML
    private TextField taskDescription;

    private static final String FILE_NAME = "taches.json";

    @FXML
    public void initialize() throws IOException {
        initJson();
        afficherTaches();
    }

    @FXML
    public static void handleAddTask(String titre, String description, String date) {
        addTask(titre, description, date);
    }


    public void initJson(){
        System.out.println("Initialisation du fichier json...");
        if (!Files.exists(Paths.get(FILE_NAME))) {
            System.out.println("Fichier json inexistant, création du fichier...");
            createJsonFile();
        }else{
            System.out.println("Fichier json existant");
        }

    }
    private void createJsonFile() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray tachesArray = new JSONArray();
            jsonObject.put("taches", tachesArray);

            Files.write(Paths.get(FILE_NAME), jsonObject.toString(2).getBytes(), StandardOpenOption.CREATE_NEW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTask(String titre, String description, String date) {

        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray tachesArray = jsonObject.getJSONArray("taches");

            JSONObject newTask = new JSONObject();
            newTask.put("id", tachesArray.length() + 1);
            newTask.put("titre", titre);
            newTask.put("description", description);
            newTask.put("completed", false);
            newTask.put("date", date);

            tachesArray.put(newTask);
            jsonObject.put("taches", tachesArray);

            Files.write(Paths.get(FILE_NAME), jsonObject.toString(2).getBytes(), StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void afficherTaches() throws IOException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray tachesArray = jsonObject.getJSONArray("taches");

            taskList.getItems().clear();

            for (int i = 0; i < tachesArray.length(); i++) {
                JSONObject tache = tachesArray.getJSONObject(i);

                String titre = tache.getString("titre");
                String description = tache.getString("description");
                boolean completed = tache.getBoolean("completed");

                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(completed);
                checkBox.setOnAction(event -> updateTaskCompletion(tache.getInt("id"), checkBox.isSelected()));

                Label taskLabel = new Label("Titre: " + titre + "\nDescription: " + description);
                HBox taskItem = new HBox(checkBox, taskLabel);

                taskList.getItems().add(taskItem);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTaskCompletion(int id, boolean selected) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray tachesArray = jsonObject.getJSONArray("taches");

            for (int i = 0; i < tachesArray.length(); i++) {
                JSONObject tache = tachesArray.getJSONObject(i);
                if (tache.getInt("id") == id) {
                    tache.put("completed", selected);
                    break;
                }
            }

            Files.write(Paths.get(FILE_NAME), jsonObject.toString(2).getBytes(), StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
    }
    }
    @FXML
    private void switchAddTacheView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/todo/demo/add-tache.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setScene(new Scene(root, 400, 400));
            stage.show();

            Stage currentStage = (Stage) taskList.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
