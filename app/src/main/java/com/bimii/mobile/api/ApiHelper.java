package com.bimii.mobile.api;


import com.bimii.mobile.api.models.Token;

import retrofit.RestAdapter;

public final class ApiHelper {

    private static ApiHelper mInstanceHelper;

    public static ApiHelper getInstance(){
        if (mInstanceHelper == null)
            mInstanceHelper = new ApiHelper();
        return mInstanceHelper;
    }

    private final RestAdapter mRestAdapter = new RestAdapter .Builder()
            .setEndpoint(ApiConstants.API_BASE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    private final BimiiApiService bas = mRestAdapter.create(BimiiApiService.class);

    private final Token mToken = new Token();

    public BimiiApiService getBimiiService(){
        return bas;
    }

    //------------------- TOKEN METHODS ----------------------//
    public Token getToken(){
        return mToken;
    }

    public void updateToken(final String token){
        mToken.token = token;
    }
    //-------------------------------------------------------//

}
