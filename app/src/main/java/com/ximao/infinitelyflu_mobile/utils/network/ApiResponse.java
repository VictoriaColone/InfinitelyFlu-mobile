package com.ximao.infinitelyflu_mobile.utils.network;

public abstract class ApiResponse {

    public abstract void onSuccess(String response);

    public abstract void onError(String message);
}