package testTask;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    // Other fields and getters/setters as needed
}

