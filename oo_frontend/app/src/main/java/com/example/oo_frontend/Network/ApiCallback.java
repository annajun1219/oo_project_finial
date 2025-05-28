package com.example.oo_frontend.Network;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onFailure(String errorMessage);
}
