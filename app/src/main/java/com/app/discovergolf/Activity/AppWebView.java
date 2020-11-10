package com.app.discovergolf.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.discovergolf.R;

/*------------parms required title  and url------------*/

public class AppWebView extends AppCompatActivity {
    Handler handler= new Handler();
    WebView webview;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.app_web_view);
        ImageView back=(ImageView)findViewById(R.id.back);
      /*  TextView name=(TextView)findViewById(R.id.name);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        name.setText(getIntent().getStringExtra("title"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        WebView webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebViewClient());
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        scheduleSendLocation();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setAllowFileAccess(true);

        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl(getIntent().getStringExtra("url"));
        Log.e("url",getIntent().getStringExtra("url")+"==");
    }
    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

    }
    private final int FIVE_SECONDS = 5000;
    public void scheduleSendLocation() {

        handler.postDelayed(new Runnable() {
            public void run() {
                int originalMode = audioManager.getMode();
Log.e("====>>>>>>>>","==="+originalMode);

                //   Toast.makeText(AppWebView.this, "++"+originalMode, Toast.LENGTH_SHORT).show();
//                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
             //   boolean state = !audioManager.isMicrophoneMute();
             //   audioManager.setMicrophoneMute(state);
                // set mode back
            //    audioManager.setMode(originalMode);

                handler.postDelayed(this, FIVE_SECONDS);
            }
        }, FIVE_SECONDS);
    }
}
