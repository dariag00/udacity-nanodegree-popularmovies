package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmovies.http.MoviesAPI;
import com.example.popularmovies.http.movies.Movie;
import com.example.popularmovies.http.movies.MovieList;
import com.example.popularmovies.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesClickListener {

    @BindView(R.id.rv_movies)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;

    @BindView(R.id.tv_error)
    TextView errorView;

    private MoviesAdapter moviesAdapter;

    @Inject
    MoviesAPI moviesAPI;

    private static final int NUMBER_OF_COLUMNS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);

        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        moviesRecyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(moviesAdapter);

        getMovies(Constants.ORDER_BY_POPULARITY);
    }

    /*
     * It gets a list of movies from the API based on the order type specified.
     */
    public void getMovies(String orderType){

        Call<MovieList> moviesCall = null;
        progressBar.setVisibility(View.VISIBLE);
        if(orderType.equals(Constants.ORDER_BY_POPULARITY)){
            moviesCall = moviesAPI.getMoviesByPopularity(Constants.API_KEY);
        }else if(orderType.equals(Constants.ORDER_BY_TOP_RATED)){
            moviesCall = moviesAPI.getMoviesByTopRated(Constants.API_KEY);
        }

        if(moviesCall != null) {
            moviesCall.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.INVISIBLE);
                        List<Movie> movies = response.body().getMovieList();
                        moviesAdapter.setMovieList(movies);
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        errorView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    t.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
                    errorView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onMoviesClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.MOVIE_EXTRA, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_order_by_popularity){
            getMovies(Constants.ORDER_BY_POPULARITY);
            return true;
        }

        if(id == R.id.action_order_by_top_rated){
            getMovies(Constants.ORDER_BY_TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
