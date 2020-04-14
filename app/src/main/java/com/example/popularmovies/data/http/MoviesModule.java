package com.example.popularmovies.data.http;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MoviesModule {

    private final String BASE_URL = "https://api.themoviedb.org/3/";

    /*
     * It provides the HTTP Client that will be used to perform the requests
     */
    @Provides
    public OkHttpClient provideHttpClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }

    /*
     * It provides the Retrofit client that will perform the requests
     */
    @Provides
    public Retrofit provideRetrofit(String baseURL, OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /*
     * It provides the MoviesAPI module from Retrofit.
     */
    @Provides
    public MoviesAPI provideMoviesService(){
        return provideRetrofit(BASE_URL, provideHttpClient()).create(MoviesAPI.class);
    }

}
