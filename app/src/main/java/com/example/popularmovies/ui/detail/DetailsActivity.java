package com.example.popularmovies.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.Constants;
import com.example.popularmovies.R;
import com.example.popularmovies.data.MoviesRepository;
import com.example.popularmovies.data.http.MoviesAPI;
import com.example.popularmovies.data.http.movies.Movie;
import com.example.popularmovies.data.http.movies.Trailer;
import com.example.popularmovies.root.App;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.TrailerClickListener {

    @BindView(R.id.tv_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.tv_vote_average)
    TextView voteAvgTextView;
    @BindView(R.id.iv_movie_poster)
    ImageView posterImageView;
    @BindView(R.id.tv_synopsis)
    TextView synopsisTextView;
    @BindView(R.id.tv_original_title)
    TextView originalTitleTextView;
    @BindView(R.id.tv_vote_count)
    TextView voteCountTextView;
    @BindView(R.id.tv_popularity)
    TextView popularityTextView;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    //Error Views
    @BindView(R.id.cv_reviews_error)
    CardView reviewsErrorView;
    @BindView(R.id.tv_trailers_error)
    TextView trailerErrorView;

    @BindView(R.id.rv_trailers)
    RecyclerView trailersRecyclerView;
    private TrailersAdapter trailersAdapter;

    @BindView(R.id.rv_reviews)
    RecyclerView reviewRecyclerView;
    private ReviewsAdapter reviewsAdapter;

    private MoviesRepository repository;

    private Movie givenMovie = null;

    @Inject
    MoviesAPI moviesAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent previousIntent = getIntent();

        if(previousIntent != null && previousIntent.hasExtra(Constants.MOVIE_EXTRA)){
            givenMovie = (Movie) previousIntent.getSerializableExtra(Constants.MOVIE_EXTRA);
            setMovieData(givenMovie);
        }else if(savedInstanceState != null && savedInstanceState.containsKey(Constants.INSTANCE_BUNDLE_MOVIE)){
            Bundle bundle = savedInstanceState.getBundle(Constants.INSTANCE_BUNDLE_MOVIE);
            if(bundle != null && bundle.containsKey(Constants.INSTANCE_MOVIE)){
                givenMovie = (Movie) bundle.getSerializable(Constants.INSTANCE_MOVIE);
            }
        }

        ((App) getApplication()).getComponent().inject(this);

        repository = MoviesRepository.getInstance(this.getApplication(), moviesAPI);

        setReviewsData();
        setTrailersData();

        floatingActionButton.setOnClickListener(view -> {
            if(givenMovie != null) {
                LiveData<Movie> observer = repository.getMovieById(givenMovie.getId());
                observer.observe(DetailsActivity.this, movie -> {
                    if(movie == null){
                        repository.addMovieToFavorites(givenMovie);
                        Toast.makeText(DetailsActivity.this, "Movie added to favorites!", Toast.LENGTH_SHORT).show();
                    }else{
                        repository.deleteMovie(movie);
                        Toast.makeText(DetailsActivity.this, "Movie deleted from favorites successfully!", Toast.LENGTH_SHORT).show();
                    }
                    observer.removeObservers(DetailsActivity.this);
                });
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.INSTANCE_MOVIE, givenMovie);
        outState.putBundle(Constants.INSTANCE_BUNDLE_MOVIE, bundle);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void setReviewsData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewsAdapter = new ReviewsAdapter();
        reviewRecyclerView.setAdapter(reviewsAdapter);
        reviewRecyclerView.setNestedScrollingEnabled(false);

        DetailsViewModelFactory viewModelFactory = new DetailsViewModelFactory(repository, givenMovie.getId());
        DetailsViewModel detailsViewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        detailsViewModel.getReviewListLiveData().observe(this, reviewList -> {
            if(reviewList != null) {
                reviewsErrorView.setVisibility(View.INVISIBLE);
                reviewsAdapter.setReviewList(reviewList);
            }else{
                reviewsErrorView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setTrailersData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trailersRecyclerView.setLayoutManager(layoutManager);
        trailersAdapter = new TrailersAdapter(this);
        trailersRecyclerView.setAdapter(trailersAdapter);
        trailersRecyclerView.setNestedScrollingEnabled(false);
        trailersRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        DetailsViewModelFactory viewModelFactory = new DetailsViewModelFactory(repository, givenMovie.getId());
        DetailsViewModel detailsViewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        detailsViewModel.getTrailerListLiveData().observe(this, trailerList -> {
            if(trailerList != null) {
                trailerErrorView.setVisibility(View.INVISIBLE);
                trailersAdapter.setTrailerList(trailerList);
            }else{
                trailerErrorView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /*
     * It sets the movie data in the view
     */
    private void setMovieData(Movie movie){
        if(movie != null) {
            setTitle(movie.getTitle());
            releaseDateTextView.setText(movie.getReleaseDate());
            synopsisTextView.setText(movie.getOverview());
            popularityTextView.setText(String.valueOf(movie.getPopularity()));
            voteCountTextView.setText(String.format(getString(R.string.vote_count_text), movie.getVoteCount()));
            originalTitleTextView.setText(movie.getOriginalTitle());

            voteAvgTextView.setText(String.valueOf(movie.getVoteAverage())); //format max 1 decimal

            double voteAverage = movie.getVoteAverage();
            if(voteAverage >= 7.0){
                voteAvgTextView.setBackgroundColor(getResources().getColor(R.color.greenMark));
            }else if(voteAverage>=5.0){
                voteAvgTextView.setBackgroundColor(getResources().getColor(R.color.yellowMark));
            }else{
                voteAvgTextView.setBackgroundColor(getResources().getColor(R.color.redMark));
            }


            String imageURL = "http://image.tmdb.org/t/p/" + "w185" + movie.getPosterPath();
            Picasso.get()
                    .load(imageURL)
                    .error(R.drawable.ic_launcher_background)
                    .into(posterImageView);
        }
    }

    @Override
    public void onTrailerClick(Trailer clickedTrailer) {
        String baseURL = "https://www.youtube.com/watch?v=";
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseURL + clickedTrailer.getKey()));
        startActivity(youtubeIntent);
    }
}
