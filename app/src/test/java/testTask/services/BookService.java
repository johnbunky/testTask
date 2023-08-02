package testTask.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import testTask.CollectionRequest;
import testTask.CollectionResponse;
import testTask.responses.BookListResponse;

public interface BookService {
    @GET("/BookStore/v1/Books")
    Call<BookListResponse> getListOfBooks(
            @Header("accept") String accept
    );

    @POST("/BookStore/v1/Books")
    Call<CollectionResponse> addBookToUserCollection(
            @Header("accept") String accept,
            @Header("Content-Type") String contentType,
            @Body CollectionRequest collectionRequest
    );
}