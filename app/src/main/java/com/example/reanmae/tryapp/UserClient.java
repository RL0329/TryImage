package com.example.reanmae.tryapp;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Rean Mae on 07/03/2018.
 */

public interface UserClient {


    @GET("/{user}")
    Call<List<GitHubRepo>> reposForUser(
            @Path("user") String user
    );

    @POST("posts")
    Call<GitHubRepo> adduser(@Body GitHubRepo posts);

    @Multipart
    @POST("comments")
    Call<GitHubRepo> uploadImage(@Part MultipartBody.Part image);

}
