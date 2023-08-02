package testTask1;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import testTask.responses.AuthTokenResponse;
import testTask1.responses.BookResponse;

import java.util.List;

public interface ApiService {
    @POST("/Account/v1/User")
    @Headers({
            "accept: application/json",
            "Content-Type: application/json"
    })
    Call<ResponseBody> createUser(@Body User user);

    @GET("/Account/v1/GenerateToken")
    @Headers("Authorization: Bearer {token}")
    Call<AuthTokenResponse> generateAuthToken();

    @GET("/getBooks")
    Call<List<BookResponse>> getBooks();
}

