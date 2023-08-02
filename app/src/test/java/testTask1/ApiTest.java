package testTask1;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import testTask.responses.AuthTokenResponse;
import testTask1.responses.BookResponse;

import java.util.List;
import java.util.Objects;

public class ApiTest {

    private static final String BASE_URL = "https://bookstore.toolsqa.com";
    private Retrofit retrofit;
    private String userId;
    private String authToken;

    @BeforeClass
    public void setUp() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

     /*@Test
    public void testCreateUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService userService = retrofit.create(ApiService.class);

        // Create a user object with the required data
        User user = new User("username", "password");

        // Call the API method and perform assertions
        // For example:
        Call<UserResponse> call = userService.createUser(user);
        Response<UserResponse> response = call.execute();
        Assert.assertEquals(response.code(), 201);
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
    }*/

    @Test
    public void testGenerateAuthToken() {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<AuthTokenResponse> call = apiService.generateAuthToken();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                if (response.isSuccessful()) {
                    AuthTokenResponse authTokenResponse = response.body();
                    Assert.assertEquals(response.code(), 200, "Response code should be 200 OK");
                    authToken = Objects.requireNonNull(authTokenResponse).getToken();
                } else {
                    Assert.fail("Failed to generate authentication token. Response code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                Assert.fail("API call failed: " + t.getMessage());
            }
        });
    }

    @Test
    public void testGetBooks() {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<BookResponse>> call = apiService.getBooks();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<BookResponse>> call, Response<List<BookResponse>> response) {
                if (response.isSuccessful()) {
                    List<BookResponse> bookList = response.body();
                    Assert.assertEquals(response.code(), 200, "Response code should be 200 OK");
                    //Assert.assertNotNull(bookList, "Book list should not be null");
                    Assert.assertFalse(bookList.isEmpty(), "Book list should not be empty");
                } else {
                    Assert.fail("Failed to retrieve list of books. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BookResponse>> call, Throwable t) {
                Assert.fail("API call failed: " + t.getMessage());
            }
        });
    }
}

