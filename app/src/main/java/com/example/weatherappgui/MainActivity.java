package com.example.weatherappgui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
EditText et;
TextView tv;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        setContentView(R.layout.activity_main);
        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
    }
    public void get(View v){
        String apikey = "628ec55eb06d9ced73a0cfd88cc213a2";
        String city = et.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=628ec55eb06d9ced73a0cfd88cc213a2";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        @SuppressLint("SetTextI18n") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject object = response.getJSONObject("main");

                        String temperature = object.getString("temp");
                        double temp = Double.parseDouble(temperature) - 273.15;
                        tv.setText(Double.toString(temp).substring(0, 5));

                        // Background image
                        JSONArray weather = response.getJSONArray("weather");
                        JSONObject weatherObject = weather.getJSONObject(0);
                        String condition = weatherObject.getString("main");
                        setBackgroundAndIcon(condition, temp);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // Handle errors here
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                });


        queue.add(request);
    }
    private void setBackgroundAndIcon(String condition,Double temp) {
        //icon
        ImageView iconImageView = findViewById(R.id.weatherIcon);
        iconImageView.setVisibility(View.VISIBLE);

        ImageView logo = findViewById(R.id.celciusLogo);
        logo.setVisibility(View.VISIBLE);
        //background
        LinearLayout layout = findViewById(R.id.mainLayout);
        layout.setBackgroundResource(R.drawable.spalsh_screen);

        if(temp<5){
            layout.setBackgroundResource(R.drawable.snow_background);  // Assuming you have clear background image
            setWeatherIcon(R.drawable.snow);
        }
        else if (condition.equals("clear")) {
            layout.setBackgroundResource(R.drawable.sunny_background);  // Assuming you have clear background image
            setWeatherIcon(R.drawable.sunny);
        }
        else if (condition.equals("Clouds") || condition.equals("Mist")) {
            layout.setBackgroundResource(R.drawable.colud_background);  // Assuming you have cloudy background image
            setWeatherIcon(R.drawable.cloud_black);
        }
        else if(condition.equals("Rain") ){
            layout.setBackgroundResource(R.drawable.rain_background);  // Assuming you have rainy background image
            setWeatherIcon(R.drawable.rain);
        }
        else{
            if (temp>5){
                layout.setBackgroundResource(R.drawable.sunny_background);  // Assuming you have clear background image
                setWeatherIcon(R.drawable.sunny);
            }
        }
        // Add more conditions for other weather types as needed
    }

    private void setWeatherIcon(int drawableId) {
        ImageView iconImageView = findViewById(R.id.weatherIcon);
        iconImageView.setImageResource(drawableId);
    }
}