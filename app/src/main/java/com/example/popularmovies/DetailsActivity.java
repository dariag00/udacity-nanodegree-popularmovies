package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.http.movies.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent previousIntent = getIntent();
        if(previousIntent.hasExtra(Constants.MOVIE_EXTRA)){
            Movie movie = (Movie) previousIntent.getSerializableExtra(Constants.MOVIE_EXTRA);
            setMovieData(movie);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
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
}
