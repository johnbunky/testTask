package testTask.responses;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("userID")
    private String userID;

    public String getUserId() {
        return userID;
    }
    // Other fields and getters/setters as needed
}

