package com.example.myapplication.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.response.ReviewResponse;
import com.example.myapplication.util.UserDetail;

import java.util.List;
import java.util.Objects;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewResponse> reviewResponses;


    public ReviewAdapter(List<ReviewResponse> reviewResponses) {
        this.reviewResponses = reviewResponses;

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewResponse response=reviewResponses.get(position);

        if(Objects.equals(response.getUserId(), UserDetail.id)){
            holder.you.setVisibility(View.VISIBLE);
        }
        holder.ratingBar.setRating(response.getRating());
        holder.comment.setText(response.getComment());
        holder.date.setText(response.getCreateAt());
        holder.username.setText(response.getUsername());

    }

    @Override
    public int getItemCount() {
        if(reviewResponses==null){
            return 0;
        }
        return reviewResponses.size();

    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView username,date,comment,you;
        RatingBar ratingBar;



        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.userImage);
            username=itemView.findViewById(R.id.textViewUserNameReview);
            date=itemView.findViewById(R.id.dateTextView);
            comment=itemView.findViewById(R.id.commentTextView);
            ratingBar=itemView.findViewById(R.id.ratingBarReview);
            you=itemView.findViewById(R.id.textViewYou);
        }
    }
}
