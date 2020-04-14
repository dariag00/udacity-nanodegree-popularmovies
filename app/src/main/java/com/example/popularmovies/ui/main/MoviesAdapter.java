package com.example.popularmovies.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.data.http.movies.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>{

    private List<Movie> movieList;

    private final MoviesClickListener onClickListener;

    public MoviesAdapter(MoviesClickListener onClickListener){
        movieList = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.setMovieData(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovieList(List<Movie> movieList){
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster) ImageView posterImageView;

        public MoviesViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setMovieData(Movie movie){
            String imageURL = "http://image.tmdb.org/t/p/" + "w185" + movie.getPosterPath();
            Picasso.get()
                    .load(imageURL)
                    .error(R.drawable.ic_launcher_background)
                    .into(posterImageView);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onMoviesClick(movieList.get(clickedPosition));
        }
    }

    public interface MoviesClickListener {
        void onMoviesClick(Movie clickedMovie);
    }
}
