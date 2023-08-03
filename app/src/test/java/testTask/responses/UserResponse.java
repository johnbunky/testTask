package testTask.responses;

import com.google.gson.annotations.SerializedName;
import testTask.stuff.Books;

import java.util.List;

public class UserResponse {
    @SerializedName("userID")
    private String userID;
    private List<Books> books;

    public String getUserId() {
        return userID;
    }

    public List<Books> getBooks() {
        return books;
    }

    public void setBooks(List<Books> books) {
        this.books = books;
    }
}

