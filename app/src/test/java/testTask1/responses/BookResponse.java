package testTask1.responses;

import com.google.gson.annotations.SerializedName;

public class BookResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    // Getters and setters for the fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

