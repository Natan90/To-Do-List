package todo.demo;

public class Task {
    private String titre;
    private String description;
    private boolean completed;

    // Constructeur, getters et setters
    public Task(String titre, String description, boolean completed) {
        this.titre = titre;
        this.description = description;
        this.completed = completed;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
}

