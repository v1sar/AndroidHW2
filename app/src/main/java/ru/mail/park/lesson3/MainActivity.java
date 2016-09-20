package ru.mail.park.lesson3;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String[] URLs = {"https://gist.githubusercontent.com/anonymous/66e735b3894c5e534f2cf381c8e3165e/raw/8c16d9ec5de0632b2b5dc3e5c114d92f3128561a/gistfile1.txt",
    "https://gist.githubusercontent.com/anonymous/be76b41ddf012b761c15a56d92affeb6/raw/bb1d4f849cb79264b53a9760fe428bbe26851849/gistfile1.txt"};

    static {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .penaltyLog()
                .penaltyDeath()
                .build()
        );
    }

    private TextView text1;
    private TextView text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.open_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnotherActivity.class));
            }
        });

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);

        if (UrlDownloader.getInstance().getCache().get(URLs[0]) != null) {
            text1.setText(UrlDownloader.getInstance().getCache().get(URLs[0]));
        } else {
            text1.setText("Click me");
        }
        if (UrlDownloader.getInstance().getCache().get(URLs[1]) != null) {
            text2.setText(UrlDownloader.getInstance().getCache().get(URLs[1]));
        } else {
            text2.setText("Click me");
        }

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromUrl(URLs[0]);
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromUrl(URLs[1]);
            }
        });

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setText("Click me");
                text2.setText("Click me");
                UrlDownloader.getInstance().clearCache();
            }
        });

        UrlDownloader.getInstance().setCallback(new UrlDownloader.Callback() {
            @Override
            public void onLoaded(String key, String value) {
                onTextLoaded(key, value);
            }
        });
    }

    private void loadFromUrl(String url) {
        textViewForUrl(url).setText("Loading...");
        UrlDownloader.getInstance().load(url);
    }

    private void onTextLoaded(String url, String stringFromUrl) {
        if (stringFromUrl == null) {
            stringFromUrl = "Data unavailable";
            UrlDownloader.getInstance().getCache().put(url, stringFromUrl);
        }
        Toast.makeText(MainActivity.this, stringFromUrl, Toast.LENGTH_SHORT).show();
        textViewForUrl(url).setText(stringFromUrl);
    }

    private TextView textViewForUrl(String url) {
        if (URLs[0].equals(url)) {
            return text1;
        } else if (URLs[1].equals(url)) {
            return text2;
        }
        throw new IllegalArgumentException("Unknown url: " + url);
    }
}
