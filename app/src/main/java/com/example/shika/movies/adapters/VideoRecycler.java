package com.example.shika.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shika.movies.R;
import com.example.shika.movies.activities.VideoPlay;
import com.example.shika.movies.utils.Tages;
import com.example.shika.movies.utils.VideoData;

import java.util.ArrayList;

/**
 * Created by shika on 12/23/2015.
 */
public class VideoRecycler extends RecyclerView.Adapter<VideoRecycler.viewHolder> {

    ArrayList<VideoData> arrayList;
    Context context;
    LayoutInflater layoutInflater;
    public VideoRecycler(Context context , ArrayList<VideoData> data){
        this.context = context;
        arrayList = data;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.movie_video_button , parent ,false);
        viewHolder holder =new viewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        holder.textView.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView textView;
    public viewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.tv_title);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , VideoPlay.class);
                i.putExtra(Tages.Video_Key , arrayList.get(getPosition()).getKey());
                context.startActivity(i);
            }
        });
    }
}
}
