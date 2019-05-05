package com.example.littlebro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.app.Activity;
import android.util.Log;


public class SmsListener extends BroadcastReceiver{

    private static final String TAG = "SmsListener";
    private static MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0; i<pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String message = smsMessage.getMessageBody();
            Log.d(TAG, message);
            mListener.messageReceived(message);
        }
    }

    public static void bindListener(MessageListener listener){
        mListener = listener;
    }

}
