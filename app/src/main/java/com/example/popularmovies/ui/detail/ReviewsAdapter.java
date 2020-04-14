package com.example.popularmovies.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.data.http.movies.Review;
import com.example.popularmovies.data.http.movies.ReviewList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends  RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {


    private ReviewList reviewList;

    public ReviewsAdapter(){
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.setReviewData(reviewList.getResults().get(position));
    }

    @Override
    public int getItemCount() {
        if(reviewList != null){
            return reviewList.getResults().size();
        }
        return 0;
    }

    public void setReviewList(ReviewList reviewList){
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_review_content)
        TextView reviewContentView;
        @BindView(R.id.tv_review_author)
        TextView authorView;

        ReviewsViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setReviewData(Review review){
            reviewContentView.setText(review.getContent());
            authorView.setText(review.getAuthor());
        }
    }
}
