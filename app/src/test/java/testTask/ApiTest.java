package testTask;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Objects;

public class ApiTest {

    private static final String BASE_URL = "https://api.example.com/";
    private Retrofit retrofit;
    private String userId; // To store the user ID

    @BeforeClass
    public void setUp() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Test
    public void testCreateUser() {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<UserResponse> call = apiService.createUser();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    Assert.assertEquals(response.code(), 200, "Response code should be 200 OK");
                    userId = Objects.requireNonNull(userResponse).getUserId();

                } else {
                    Assert.fail("Failed to create user. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Assert.fail("API call failed: " + t.getMessage());
            }
        });
    }

    // You can add more test methods to further test the API
}

