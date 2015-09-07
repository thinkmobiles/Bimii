package com.bimii.mobile.api;

import com.bimii.mobile.api.models.User;
import com.bimii.mobile.api.models.based.Game;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface BimiiApiService {

    @POST("/request_token")
    void requestToken(@Body User user, final Callback<String> callback);

    @FormUrlEncoded
    @POST("/library/games")
    void gameLibrary(@Field("token") String token, final Callback<List<Game>> callback);

//    @FormUrlEncoded
//    @Streaming
//    @POST("/library/games/download")
//    Response downloadGame(@Field("token") String token, @Field("game_id") int game_id);

}
