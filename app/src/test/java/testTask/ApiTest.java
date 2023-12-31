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
    List<CollectionResponse.Book> responseBooks;

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

        System.out.println("user: " + uniqueUser);
        System.out.println("password: " + uniquePassword);

        User user = new User(uniqueUser, uniquePassword);

        Call<UserResponse> call = userService.createUser("application/json", "application/json", user);
        Response<UserResponse> response = call.execute();
        if (response.isSuccessful()) {
            System.out.println("User created successfully");

            UserResponse userResponse = response.body();
            userId = Objects.requireNonNull(userResponse).getUserId();

            System.out.println("User id is: " + userId);
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

            System.out.println("User token: " + token);
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

    @Test(dependsOnMethods = {"testFilterAuthorsOfBooks", "testCreateUser", "testGenerateAuthToken", "testGetListOfBooks"})
    public void testAddBookToUserCollection() throws IOException {
        BookService bookService = retrofit.create(BookService.class);

        CollectionRequest collectionRequest = new CollectionRequest(userId, Collections.singletonList(new Isbn(books.get(0).getIsbn())));

        Call<CollectionResponse> call = bookService.addBookToUserCollection(
                "application/json",
                "Bearer " + token,
                "application/json",
                collectionRequest
        );
        Response<CollectionResponse> response = call.execute();

        if (response.isSuccessful()) {
            CollectionResponse collectionResponse = response.body();

            responseBooks = Objects.requireNonNull(collectionResponse).getBooks();
            Assert.assertNotNull(responseBooks, "Books should not be null");

            for (CollectionResponse.Book book : responseBooks) {
                System.out.println("Added Book ISBN: " + book.getIsbn());
            }
        } else {
            Assert.fail("Request was not successful. Response code: " + response.code());
        }
    }

    @Test(dependsOnMethods = "testAddBookToUserCollection")
    public void testRetrievedUserDetails() {
        List<String> isbn = new ArrayList<>();
        // Get isbn collection
        for (Books book : books) isbn.add(book.getIsbn());
        // check if isbn of user books are correct
        for (CollectionResponse.Book book : responseBooks) {
            Assert.assertTrue(isbn.contains(book.getIsbn()), book.getIsbn() + " is not in the isbn list");
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