package testTask.services;

import retrofit2.Call;
import retrofit2.http.*;
import testTask.stuff.User;
import testTask.responses.AuthTokenResponse;
import testTask.responses.UserResponse;

public interface UserService {
    @POST("Account/v1/User")
    Call<UserResponse> createUser(
            @Header("accept") String accept,
            @Header("Content-Type") String contentType,
            @Body User user
    );

    @POST("/Account/v1/GenerateToken")
    Call<AuthTokenResponse> generateAuthToken(
            @Header("accept") String accept,
            @Header("Content-Type") String contentType,
            @Body User user
    );

    @GET("/Account/v1/User/{userId}")
    @Headers({
            "accept: application/json"
    })
    Call<UserResponse> retrieveUserDetails(
            @Header("Authorization") String authorizationHeader,
            @Header("Content-Type") String contentType,
            @Path("userId") String userId
    );
}
