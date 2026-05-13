package ru.mirea.svinarenkomd.mireaproject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("fact")
    Call<CatFact> getCatFact();
}