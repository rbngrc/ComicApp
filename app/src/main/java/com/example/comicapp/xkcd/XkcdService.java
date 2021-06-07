package com.example.comicapp.xkcd;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface XkcdService {

    @GET("{num}/info.0.json")
    Call<Comic> getComic(@Path("num") int num);

}
