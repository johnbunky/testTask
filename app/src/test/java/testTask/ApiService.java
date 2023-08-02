package testTask;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/createUser")
    Call<UserResponse> createUser();
}

