package com.stuffer.stuffers.myService;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
/*
import com.upi.hcesdk.api.CDCVMResult;
import com.upi.hcesdk.api.CVMType;
import com.upi.hcesdk.api.CVMVerifyingEntity;
import com.upi.hcesdk.api.service.HceApiService;
import com.upi.hcesdk.exception.ApduProcessingException;
import com.upi.hcesdk.exception.DBNotInitialisedException;
import com.upi.hcesdk.exception.NoActiveCardException;
import com.upi.hcesdk.exception.NoMoreTokenException;

import java.sql.SQLException;

import static com.upi.hcesdk.apdu.ISO7816.SW1SW2_6984;*/

public class MyApduService extends HostApduService {
    String TAG = "Host Card Emulator";
    String STATUS_SUCCESS = "9000";
    String STATUS_FAILED = "6F00";
    String CLA_NOT_SUPPORTED = "6E00";
    String INS_NOT_SUPPORTED = "6D00";
    String AID = "A0000002471001";
    String SELECT_INS = "A4";
    String DEFAULT_CLA = "00";
    int MIN_APDU_LENGTH = 12;

    @Override
    public byte[] processCommandApdu(byte[] paramArrayOfByte, Bundle bundle) {

        /*CDCVMResult cdcvmResult = new CDCVMResult(true, CVMType.PASSCODE, CVMVerifyingEntity.MOBILE_APPLICATION);
        byte[] apduResponse = null;
        try {
            apduResponse = HceApiService.getInstance().processApdu(paramArrayOfByte, cdcvmResult);
        }
        catch ( DBNotInitialisedException e1) {
            Log.d(TAG, "DB is not initialised");
        }
        catch ( ApduProcessingException e2) {
            Log.d(TAG, "APDU Processing Exception");
        }
        catch (SQLException e3) {
            Log.d(TAG, "SQL Exception");
        }
        catch( NoActiveCardException e4) {
            Log.d(TAG, "No active card exception");
        }
        catch( NoMoreTokenException e5) {
            Log.d(TAG, "No more token exception");
        }
        if ( apduResponse != null ) {
            return apduResponse;
        }
        return SW1SW2_6984;*/
        return null;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "Deactivated: " + reason);
    }
}
