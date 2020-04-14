package com.example.popularmovies.ui.detail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.data.http.movies.Trailer;
import com.example.popularmovies.data.http.movies.TrailerList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private TrailerList trailerList;
    private final TrailerClickListener trailerClickListener;

    public TrailersAdapter(TrailerClickListener trailerClickListener){
        this.trailerClickListener = trailerClickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.setTrailerData(trailerList.getResults().get(position));
    }

    @Override
    public int getItemCount() {
        if(trailerList != null){
            return trailerList.getResults().size();
        }
        return 0;
    }

    public void setTrailerList(TrailerList trailerList){
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }


    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_trailer_name)
        TextView trailerNameView;

        @BindView(R.id.iv_share_icon)
        ImageView shareIconView;

        Context context;

        public TrailerViewHolder(View itemView, Context context){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            this.context = context;
        }

        void setTrailerData(Trailer trailer){
            trailerNameView.setText(trailer.getName());
            shareIconView.setOnClickListener(view -> {
                setShareableLink(trailer);
            });
        }

        void setShareableLink(Trailer trailer){
            ShareCompat.IntentBuilder
                    /* The from method specifies the Context from which this share is coming from */
                    .from((Activity) context)
                    .setType("text/plain")
                    .setChooserTitle("Sharing Movie Trailer")
                    .setText("https://www.youtube.com/watch?v=" + trailer.getKey())
                    .startChooser();

        }

        @Override
        public void onClick(View view) {
            trailerClickListener.onTrailerClick(trailerList.getResults().get(getAdapterPosition()));
        }
    }

    public interface TrailerClickListener {
        void onTrailerClick(Trailer clickedTrailer);
    }
}
