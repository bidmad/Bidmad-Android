package com.adop.example.adopsample.Consumable;

import com.adop.sdk.BMAdError;

public interface AdCallback<T> {
    void onSuccess(T value);
    void onFailure(BMAdError error);
}
