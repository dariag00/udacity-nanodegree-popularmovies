package com.example.popularmovies.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.MoviesRepository;
import com.example.popularmovies.data.http.MoviesAPI;
import com.example.popularmovies.data.http.movies.Movie;
import com.example.popularmovies.root.App;
import com.example.popularmovies.ui.detail.DetailsActivity;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesClickListener {

    @BindView(R.id.rv_movies)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;

    @BindView(R.id.tv_error)
    TextView errorView;

    private MoviesAdapter moviesAdapter;
    private MoviesRepository repository;

    @Inject
    MoviesAPI moviesAPI;

    private MainViewModel viewModel;
    private String orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);

        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
        moviesRecyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(moviesAdapter);

        repository = MoviesRepository.getInstance(this.getApplication(), moviesAPI);

        if(savedInstanceState != null && savedInstanceState.containsKey(Constants.INSTANCE_ORDER_BY)){
            orderType = savedInstanceState.getString(Constants.INSTANCE_ORDER_BY);
        }else{
            orderType = Constants.ORDER_BY_POPULARITY;
        }
        setInitialData(orderType);
    }

    /*
     * It gets a list of movies from the API based on the order type specified.
     */
    private void getMovies(String orderType){

        prepareUIForDataLoad();

        viewModel.updateLiveData(orderType);
        viewModel.getMoviesLiveData().observe(this, movies -> {
            progressBar.setVisibility(View.INVISIBLE);
            if(movies != null) {
                moviesRecyclerView.setVisibility(View.VISIBLE);
                moviesAdapter.setMovieList(movies);
            } else {
                moviesRecyclerView.setVisibility(View.INVISIBLE);
                errorView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString(Constants.INSTANCE_ORDER_BY, orderType);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void setInitialData(String orderType){

        prepareUIForDataLoad();

        MainViewModelFactory factory = new MainViewModelFactory(repository, orderType);
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
        viewModel.getMoviesLiveData().observe(this, movies -> {
            progressBar.setVisibility(View.INVISIBLE);
            if(movies != null) {
                moviesAdapter.setMovieList(movies);
            } else {
                errorView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void prepareUIForDataLoad(){
        errorView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
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
            orderType = Constants.ORDER_BY_POPULARITY;
            getMovies(orderType);
            return true;
        }

        if(id == R.id.action_order_by_top_rated){
            orderType = Constants.ORDER_BY_TOP_RATED;
            getMovies(orderType);
            return true;
        }

        if(id == R.id.action_order_by_favorite){
            orderType = Constants.ORDER_BY_FAVORITES;
            getMovies(orderType);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the item
        int widthDivider = 500;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }


}
