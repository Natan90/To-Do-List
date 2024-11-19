module todo.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens todo.demo to javafx.fxml;
    exports todo.demo;
}