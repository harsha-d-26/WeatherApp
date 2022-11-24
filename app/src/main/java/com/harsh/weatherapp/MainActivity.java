package com.harsh.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    Button btnSearch;
    EditText cityName;
    RecyclerView rcView;
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String app_id = "28ccc04596c5ce00e511d26fd9a3039f";
    public static String city_name = "London";
    public static double lon = 10.99;
    List<DataModel> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearch = findViewById(R.id.search);
        cityName = findViewById(R.id.city_name);
        rcView = findViewById(R.id.rcView);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = new LinkedList<>();
                rcView.setAdapter(new RecAdapter(MainActivity.this,data));
                rcView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rcView.setHasFixedSize(true);
                getCurrentData();
            }
        });

    }

    void getCurrentData() {
        Log.i(TAG,cityName.getText().toString());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call call = service.getCurrentWeatherData(cityName.getText().toString(), app_id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = (WeatherResponse) response.body();
                    assert weatherResponse != null;
                    String temp = String.format("%.2f °C",weatherResponse.main.temp-273.00);
                    String tempMin = String.format("%.2f °C",weatherResponse.main.temp_min-273.00);
                    String tempMax = String.format("%.2f °C",weatherResponse.main.temp_max-273.00);
//                    String stringBuilder = "Country: " +
//                            weatherResponse.sys.country +
//                            "\n" +
//                            "Temperature: " +
//                            weatherResponse.main.temp +
//                            "\n" +
//                            "Temperature(Min): " +
//                            weatherResponse.main.temp_min +
//                            "\n" +
//                            "Temperature(Max): " +
//                            weatherResponse.main.temp_max +
//                            "\n" +
//                            "Humidity: " +
//                            weatherResponse.main.humidity +
//                            "\n" +
//                            "Pressure: " +
//                            weatherResponse.main.pressure;
                    data.add(new DataModel("Country",weatherResponse.sys.country));
                    data.add(new DataModel("Temperature", temp));
                    data.add(new DataModel("Temperature(Min)", tempMin));
                    data.add(new DataModel("Temperature(Max)", tempMax));
                    data.add(new DataModel("Humidity", weatherResponse.main.humidity +"%"));
//                    data.add(new DataModel("Pressure",String.valueOf(weatherResponse.main.pressure)));
                    rcView.getAdapter().notifyDataSetChanged();
//                    Toast.makeText(MainActivity.this,stringBuilder,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}