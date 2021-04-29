package com.example.comicapp;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comicapp.xkcd.Comic;
import com.example.comicapp.xkcd.XkcdService;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView txt;
    EditText editText;
    Button btn, btn2, btn3;
    XkcdService xkcdService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.imageView);
        editText = findViewById(R.id.editTextNumber);
        btn = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editText.getText().toString());
                devolverComic(id);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editText.getText().toString());
                devolverComic(id);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editText.getText().toString())+1;
                devolverComic(id);
            }
        });
    }

    private void devolverComic(int id) {
        // Construir retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // instanciacion de la interfaz
        xkcdService = retrofit.create(XkcdService.class);
        Call<Comic> request = xkcdService.getComic(id);

        request.enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                // Comprobando la respuesta
                if (response.code() != 200){
                    txt.setText("Comprueba la conexión");
                    return;
                }

                // añadir imagen de la url a 'img' https://imgs.xkcd.com/comics/angular_momentum.jpg
                String url = "";
                url = response.body().getImg();

                // mostrar imagen usando picasso
                Picasso.get().load(url).into(img);

            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
            }
        });
    }


}