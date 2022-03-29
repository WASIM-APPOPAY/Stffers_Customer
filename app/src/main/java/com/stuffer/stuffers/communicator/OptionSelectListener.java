package com.stuffer.stuffers.communicator;

import org.json.JSONObject;

public interface OptionSelectListener {
    public void onSelectConfirm(String param, JSONObject userData);
}
