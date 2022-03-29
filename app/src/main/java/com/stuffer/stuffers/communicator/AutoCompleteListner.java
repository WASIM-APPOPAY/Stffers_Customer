package com.stuffer.stuffers.communicator;


import com.stuffer.stuffers.models.PredictionModel;

public interface AutoCompleteListner {
    void onSuccess(PredictionModel prediction);
    void onError(String err);
}
