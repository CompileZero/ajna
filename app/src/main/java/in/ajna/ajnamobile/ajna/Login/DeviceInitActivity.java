package in.ajna.ajnamobile.ajna.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import in.ajna.ajnamobile.ajna.R;

import static in.ajna.ajnamobile.ajna.PlayerConfig.API_KEY;

public class DeviceInitActivity extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;

    YouTubePlayer.OnInitializedListener onInitializedListener;

    AppCompatButton btnProceed;
    Boolean disconnected = false;
    private WifiManager wifiManager;
    private int networkId;
    private BroadcastReceiver myWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            SupplicantState newState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            switch (newState) {
                case ASSOCIATED:
                    Log.d("WIFI", "CONNECTED");
                    disconnected = false;
                    break;
                case DISCONNECTED:
                    if (!disconnected) {
                        Log.d("WIFI", "DISCONNECTED");
                        disconnected = true;
                        wifiManager.enableNetwork(networkId, true);
                        wifiManager.reconnect();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_init);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        btnProceed = findViewById(R.id.btnProceed);

        youTubePlayerView = findViewById(R.id.youtubePlayerView);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("u-rcLZzUbqU");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(API_KEY, onInitializedListener);
        //connectWifi();

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                WifiInfo wifiInfo;
//
//                wifiInfo = wifiManager.getConnectionInfo();
//
//                String ssid=findSSIDForWifiInfo(wifiManager,wifiInfo);
//                Toast.makeText(DeviceInitActivity.this, "ssid-"+ssid, Toast.LENGTH_SHORT).show();
//                if(ssid.equals("\"AJNA8266\"")){
//                    Intent intent=new Intent(DeviceInitActivity.this,WifiManagerActivity.class);
//                    startActivity(intent);
//
//                }
//                else{
//                    connectWifi();
//                }
//                if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
//                }

            }
        });

    }

    @Override
    protected void onStart() {
        this.registerReceiver(this.myWifiReceiver, new IntentFilter(
                WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
        super.onStart();

    }

    @Override
    protected void onStop() {
        this.unregisterReceiver(this.myWifiReceiver);
        super.onStop();

    }

    public String findSSIDForWifiInfo(WifiManager manager, WifiInfo wifiInfo) {

        List<WifiConfiguration> listOfConfigurations = manager.getConfiguredNetworks();
        for (int index = 0; index < listOfConfigurations.size(); index++) {
            WifiConfiguration configuration = listOfConfigurations.get(index);
            if (configuration.networkId == wifiInfo.getNetworkId()) {
                return configuration.SSID;
            }
        }
        return null;
    }

    public void connectWifi() {

        final WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"AJNA8266\"";
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        networkId = wifiManager.addNetwork(config);
        wifiManager.disconnect();

    }
}
