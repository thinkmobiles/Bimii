package com.bimii.mobile.api;

import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.api.models.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.mime.TypedFile;

public interface BimiiApiService {

    @POST("/request_token")
    void requestToken(@Body User user, final Callback<String> callback);

    @FormUrlEncoded
    @POST("/library/games")
    void gameLibrary(@Field("token") String token, final Callback<List<Game>> callback);

    @Headers("Content-Type: application/vnd.android.package-archive")
    @FormUrlEncoded
    @Multipart
    @POST("/library/games/download'")
    void downloadGame(@Field("token") String tokem, @Field("game_id") int game_id, Callback<TypedFile> callback);

}
