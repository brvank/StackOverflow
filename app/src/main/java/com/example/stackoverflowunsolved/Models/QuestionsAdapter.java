package com.example.stackoverflowunsolved.Models;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stackoverflowunsolved.DetailsWebViewActivity;
import com.example.stackoverflowunsolved.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Questions> questions;

    public QuestionsAdapter(ArrayList<Questions> q, Context c){
        this.questions = q;
        this.context = c;
    }

    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.question_box,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {

        //creating an extra int variable to remove the warning
        int position = pos;

        //setting the question in the text view of question
        holder.tvQuestion.setText(questions.get(position).getTitle());

        //creating the hour-min from the unix epoch time
        Date dateAndTime = new Date(Long.parseLong(questions.get(position).getDateCreated())*1000);
        int hour = Integer.parseInt(String.valueOf(dateAndTime).substring(11,13));
        int min = Integer.parseInt(String.valueOf(dateAndTime).substring(14,16));

        String amORpm = null;
        if(hour>=12){
            if(hour>12) {
                hour -= 12;
                amORpm = "PM";
            }else{
                amORpm = "AM";
            }
        }

        String minute = "";
        if(min<10){
            minute = "0";
        }
        minute += String.valueOf(min);

        //setting the question creating time
        holder.tvCreatedDate.setText(new String(hour + ":" + minute + " " + amORpm));

        //setting the on click listener on the constraint layout created for the holding the question for one unit
        holder.llQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating intent to move from questions activity to details activity
                Intent intent = new Intent(context, DetailsWebViewActivity.class);
                intent.putExtra("questionURL", questions.get(position).getQuestionURL());
                context.startActivity(intent);
            }
        });

        //setting the tags in the view
        ArrayList<String> tags = questions.get(pos).getTags();

        if(tags.size() == 2){
            holder.tvTag1.setText(tags.get(0));
            holder.tvTag2.setText(tags.get(1));
        }else if(tags.size() == 1){
            holder.tvTag1.setText(tags.get(0));
            holder.tvTag2.setVisibility(View.GONE);
        }else if(tags.size() == 0){
            holder.tvTag1.setVisibility(View.GONE);
            holder.tvTag2.setVisibility(View.GONE);
        }else{
            holder.tvTag1.setText(tags.get(0));
            holder.tvTag2.setText(tags.get(1));
            holder.tvTag3.setText("+" + (tags.size() - 2));
        }

        //downloading and setting the owner image
        Glide.with(context)
                .load(questions.get(position).getProfileImageURL())
                .error(R.drawable.no_image)
                .into(holder.ivOwnerProfile);

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivOwnerProfile;
        TextView tvQuestion, tvCreatedDate, tvTag1, tvTag2, tvTag3;
        LinearLayout llQuestions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivOwnerProfile = itemView.findViewById(R.id.iv_owner_profile);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            tvCreatedDate = itemView.findViewById(R.id.tv_created_date);
            llQuestions = itemView.findViewById(R.id.ll_question);
            tvTag1 = itemView.findViewById(R.id.tv_tag1);
            tvTag2 = itemView.findViewById(R.id.tv_tag2);
            tvTag3 = itemView.findViewById(R.id.tv_tag3);

        }
    }
}
