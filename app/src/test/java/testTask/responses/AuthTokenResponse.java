package testTask.responses;

import com.google.gson.annotations.SerializedName;

public class AuthTokenResponse {
    @SerializedName("token")
    private String token;

    // Getters and setters for the fields
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

