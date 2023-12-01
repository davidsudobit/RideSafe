package com.calldetection;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Locale;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

public class PhoneStateReceiver extends BroadcastReceiver{

    private static String currentPhoneState=TelephonyManager.EXTRA_STATE_IDLE;

    private static final MqttHandler mqttHandler=new MqttHandler();

    private static final String BROKER_URL="tcp://192.168.1.100:1883";
    private static final String CLIENT_ID="CALL_STATE_PUB";
    private static final String TOPIC="CALL_STATUS_TOPIC";

    public PhoneStateReceiver(){
        PhoneStateReceiver.mqttHandler.connect(BROKER_URL, CLIENT_ID);
        PhoneStateReceiver.mqttHandler.subscribe(TOPIC);
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
//            System.out.println("Receiver start");
            Toast.makeText(context, " Receiver started ", (int) 8).show();

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            System.out.println("Current state: " + state);

            if (state == null) return;

//            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
//                Toast.makeText(context,"Ringing State",Toast.LENGTH_SHORT).show();
//            }

            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {

                PhoneStateReceiver.currentPhoneState = TelephonyManager.EXTRA_STATE_OFFHOOK;
                PhoneStateReceiver.mqttHandler.publish(TOPIC,"CALL_STARTED");
                Toast.makeText(context, "Received State", Toast.LENGTH_SHORT).show();

            }

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                if(PhoneStateReceiver.currentPhoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                    PhoneStateReceiver.mqttHandler.publish(TOPIC,"CALL_ENDED");

                PhoneStateReceiver.currentPhoneState = TelephonyManager.EXTRA_STATE_IDLE;
                Toast.makeText(context, "Idle State", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
