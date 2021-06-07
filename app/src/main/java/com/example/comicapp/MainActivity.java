package com.example.comicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.xkcd.Comic;
import com.example.comicapp.xkcd.XkcdService;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

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
    int id;

    DBHelper myDB;

    private Executor bdExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.imageView);
        editText = findViewById(R.id.editTextNumber);
        txt = findViewById(R.id.textView);
        btn = findViewById(R.id.button); // VER COMIC
        btn2 = findViewById(R.id.button2); // ANTERIOR
        btn3 = findViewById(R.id.button3); // SIGUIENTE

        myDB = new DBHelper(this);

        bdExecutor = ((MyApplication) getApplication()).diskIOExecutor;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = Integer.parseInt(editText.getText().toString());

                if (editText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Para continuar introduce un número", Toast.LENGTH_SHORT).show();
                } else {
                    bdExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            Boolean comprobarComic = myDB.comprobarComic(id);
                            if (comprobarComic) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mostrarComic(id);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        guardarComic(id);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Para continuar introduce un número", Toast.LENGTH_SHORT).show();
                } else {
                    bdExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            id = id - 1;
                            Boolean comprobarComic = myDB.comprobarComic(id);
                            if (comprobarComic) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mostrarComic(id);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        guardarComic(id);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Para continuar introduce un número", Toast.LENGTH_SHORT).show();
                } else {
                    bdExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            id = id + 1;
                            Boolean comprobarComic = myDB.comprobarComic(id);
                            if (comprobarComic) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mostrarComic(id);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        guardarComic(id);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void guardarComic(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        xkcdService = retrofit.create(XkcdService.class);
        Call<Comic> request = xkcdService.getComic(id);

        request.enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                if (response.code() != 200){
                    Toast.makeText(MainActivity.this, "Compruebe la conexion", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = response.body().getImg();
                String title = response.body().getTitle();

                myDB.insertarBaseDatos(id, url, title);
                Toast.makeText(MainActivity.this, "Guardado", Toast.LENGTH_SHORT).show();

                mostrarComic(id);
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
            }
        });
    }

    public void mostrarComic(int id){
        Cursor cursor = myDB.leerBaseDatos(id);
        if(cursor.getCount() == 0){
            Toast.makeText(MainActivity.this, "No se puede conectar", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()){
            int num = cursor.getInt(0);
            String url = cursor.getString(1);
            String title = cursor.getString(2);

//            Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
            Picasso.get().load(url).into(img);
            txt.setText(num + ": " + title);
        }
    }
}
