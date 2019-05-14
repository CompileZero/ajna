package in.ajna.ajnamobile.ajna.Login;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import in.ajna.ajnamobile.ajna.R;

public class UserGuideActivity extends AppCompatActivity {
    private WebView webView;
    private MaterialButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);


        btnBack = findViewById(R.id.btnBack);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.google.com");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    UserGuideActivity.super.onBackPressed();
                }
            }
        });
    }


}
