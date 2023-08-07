package com.example.rgb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchRandomColor();
            }
        });
    }
    private void fetchRandomColor(){
        String url = "http://172.16.24.191:5000/random";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateColor(jsonData);
                        }
                    });
                }
            }

        });
    }
    private void updateColor(String jsonData) {
        // Parse JSON and update TextView with the color
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int red = jsonObject.getInt("red");
            int green = jsonObject.getInt("green");
            int blue = jsonObject.getInt("blue");

            String color = String.format("#%02x%02x%02x", red, green, blue);
            text.setText("Random Color: " + color);
            text.setBackgroundColor(Color.rgb(red,green,blue));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        

}