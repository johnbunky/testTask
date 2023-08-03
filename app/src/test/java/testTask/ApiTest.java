package testTask;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import testTask.requests.CollectionRequest;
import testTask.responses.AuthTokenResponse;
import testTask.responses.BookListResponse;
import testTask.responses.CollectionResponse;
import testTask.responses.UserResponse;
import testTask.services.BookService;
import testTask.services.UserService;
import testTask.stuff.Books;
import testTask.stuff.Isbn;
import testTask.stuff.User;

import java.io.IOException;
import java.util.*;

public class ApiTest {
    private static final String BASE_URL = "https://bookstore.toolsqa.com";
    private Retrofit retrofit;
    private String token;
    private String userId;
    private List<Books> books;

    String uniquePassword = "Password!" + UUID.randomUUID().toString().substring(0, 8);
    String uniqueUser = "newUser" + UUID.randomUUID().toString().substring(0, 8);

    @BeforeClass
    public void setUp() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Test
    public void testCreateUser() throws IOException {
        UserService userService = retrofit.create(UserService.class);

        System.out.println("user: " + uniqueUser); // newUseree33aab3
        System.out.println("password: " + uniquePassword);// Password!e7c37dbc

        User user = new User(uniqueUser, uniquePassword);

        Call<UserResponse> call = userService.createUser("application/json", "application/json", user);
        Response<UserResponse> response = call.execute();
        if (response.isSuccessful()) {
            System.out.println("User created successfully");

            UserResponse userResponse = response.body();
            userId = Objects.requireNonNull(userResponse).getUserId();

            System.out.println("User id is: " + userId); // c6cb81da-b060-459c-b19c-38c8aab877c7
        } else {
            Assert.fail("Request was not successful. Response code: " + response.code());
        }
    }

    @Test(dependsOnMethods = "testCreateUser")
    public void testGenerateAuthToken() throws IOException {
        UserService userService = retrofit.create(UserService.class);

        User user = new User(uniqueUser, uniquePassword);

        Call<AuthTokenResponse> call = userService.generateAuthToken("application/json", "application/json", user);
        Response<AuthTokenResponse> response = call.execute();

        if (response.isSuccessful()) {
            AuthTokenResponse authTokenResponse = response.body();
            token = Objects.requireNonNull(authTokenResponse).getToken();

            System.out.println("User token: " + token); // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6Im5ld1VzZXJlZTMzYWFiMyIsInBhc3N3b3JkIjoiUGFzc3dvcmQhZTdjMzdkYmMiLCJpYXQiOjE2OTEwNzYyNTB9.8CR2aRlWVVaFbkaC3atRe8qlCtA84OyDlCNhtBE97LU
            Assert.assertFalse(token.isEmpty(), "We got the token!!!");
        } else {
            Assert.fail("Request was not successful. Response code: " + response.code());
        }
    }

    @Test
    public void testGetListOfBooks() throws IOException {
        BookService bookService = retrofit.create(BookService.class);

        Call<BookListResponse> call = bookService.getListOfBooks("application/json");
        Response<BookListResponse> response = call.execute();

        if (response.isSuccessful()) {
            BookListResponse bookListResponse = response.body();
            books = Objects.requireNonNull(bookListResponse).getBooks();

            System.out.println("Total books: " + books.size());

            Assert.assertFalse(books.isEmpty(), "List of books should not be empty");
        } else {
            Assert.fail("Request was not successful. Response code: " + response.code());
        }
    }

    @Test
    public void testFilterAuthorsOfBooks() throws IOException {
        BookService bookService = retrofit.create(BookService.class);

        Call<BookListResponse> call = bookService.getListOfBooks("application/json");
        Response<BookListResponse> response = call.execute();

        if (response.isSuccessful()) {
            BookListResponse bookListResponse = response.body();
            books = Objects.requireNonNull(bookListResponse).getBooks();
            List<String> authors = new ArrayList<>();

            for (Books book : books) authors.add(book.getAuthor());

            String targetAuthor = "Addy Osmani";
            Assert.assertTrue(authors.contains(targetAuthor), targetAuthor + " is not in the authors list");
        } else {
            Assert.fail("Request was not successful. Response code: " + response.code());
        }
    }

    @Test(dependsOnMethods = {"testFilterAuthorsOfBooks", "testCreateUser", "testGenerateAuthToken"})
    public void testAddBookToUserCollection() throws IOException {
        BookService bookService = retrofit.create(BookService.class);

        CollectionRequest collectionRequest = new CollectionRequest(userId, Collections.singletonList(new Isbn(books.get(0).getIsbn())));//"9781449337711")));

        Call<CollectionResponse> call = bookService.addBookToUserCollection("application/json", "application/json", collectionRequest);
        Response<CollectionResponse> response = call.execute();

        if (response.isSuccessful()) {
            CollectionResponse collectionResponse = response.body();

            Assert.assertNotNull(Objects.requireNonNull(collectionResponse).getCollectionOfIsbns(), "ISBN should not be null");

            System.out.println("Book added to user's collection");
        } else {
            Assert.fail("Request was not successful. Response code: " + response.code());
        }
    }

    @Test(dependsOnMethods = "testAddBookToUserCollection")
    public void testRetrieveUserDetails() throws IOException {
        UserService userService = retrofit.create(UserService.class);

        Call<UserResponse> call = userService.retrieveUserDetails("application/json", userId);
        Response<UserResponse> response = call.execute();

        if (response.isSuccessful()) {
            UserResponse userResponse = response.body();

            // Compare the books in the response with the books stored in the context
            List<Books> storedBooks = books; // Assuming you have books stored in the context
            List<Books> userBooks = Objects.requireNonNull(userResponse).getBooks();

            Assert.assertEquals(userBooks, storedBooks, "User's books should match stored books");

        } else {
            Assert.fail("Request was not successful. Response code: " + response.code());
        }
    }

    private boolean isValidJwtToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}