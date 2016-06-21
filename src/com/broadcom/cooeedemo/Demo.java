package com.broadcom.cooeedemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ToggleButton;
import android.view.View;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

import com.broadcom.cooee.*;

public class Demo 
    extends Activity
{
    static final String TAG = "demo";

    int mLocalIp;
    ToggleButton mBtn = null;
    Thread mThread = null;
    boolean mDone = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        updateWifiInfo();

        mBtn = (ToggleButton) findViewById(R.id.send);
        mBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.send) {
                    if (!mDone) {
                        mDone = true;

                        final String ssid = ((EditText) findViewById(R.id.ssid)).getText().toString();
                        final String password = ((EditText) findViewById(R.id.password)).getText().toString();

                        /* set packet interval */
                        Cooee.SetPacketInterval(20); /* 10ms */

                        if (mThread == null) {
                            mThread = new Thread() {
                                public void run() {
                                    while (mDone) {
                                        //Cooee.send(ssid, password, mLocalIp, "0123456789abcdef");
                                        Cooee.send(ssid, password, mLocalIp);
                                        //Cooee.send(ssid, password);
                                    }
                                }
                            };
                        }

                        mThread.start();
                    } else {
                        mDone = false;
                        mThread = null;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateWifiInfo();
    }

    void updateWifiInfo() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.d(TAG, "connected: "+networkInfo.isConnected());
        if (!networkInfo.isConnected()) {
            Log.d(TAG, getString(R.string.connect_wifi));
            showErrorDialog();
            return;
        }
        
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        mLocalIp = info.getIpAddress();
        Log.d(TAG, String.format("ip: 0x%x", mLocalIp));

        EditText et = (EditText) findViewById(R.id.ssid);
        String ssid = info.getSSID();
        if (ssid.startsWith("\"")) {
            ssid = ssid.substring(1, ssid.length()-1);
        }
        et.setText(ssid);
        Log.d(TAG, "ssid: "+ssid);
    }

    void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.connect_wifi);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        builder.create().show();
    }
}
