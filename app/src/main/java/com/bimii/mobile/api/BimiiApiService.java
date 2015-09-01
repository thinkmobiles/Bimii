package com.bimii.mobile.api;

import com.bimii.mobile.api.models.Game;
import com.bimii.mobile.api.models.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.mime.TypedFile;

public interface BimiiApiService {

    @POST("/request_token")
    void requestToken(@Body User user, final Callback<String> callback);

    @POST("/library/games")
    void gameLibrary(@Field("token") String token, final Callback<List<Game>> callback);

    @Headers("Content-Type: application/vnd.android.package-archive")
    @Multipart
    @POST("/library/games/request")
    void downloadGame(@Field("token") String tokem, @Field("game_id") int game_id, Callback<TypedFile> callback);

    @POST("/library/games/install")
    void installGame(@Field("token") String tokem, @Field("game_id") int game_id, Callback<Boolean> callback);

    @POST("/library/games/uninstall")
    void unisstallGame(@Field("token") String tokem, @Field("game_id") int game_id, Callback<Boolean> callback);

}
