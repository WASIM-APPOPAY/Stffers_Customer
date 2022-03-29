package com.stuffer.stuffers.communicator;



import org.json.JSONObject;
import com.stuffer.stuffers.models.output.CurrencyResult;

import java.util.List;

public interface UserAccountTransferListener {
    //public void onAccountTransfer(JSONObject indexUser, String currencyResponse,JSONObject baseConversion);
    public void onAccountTransfer(JSONObject indexUser, List<CurrencyResult> currencyResults, JSONObject baseConversion);
}
