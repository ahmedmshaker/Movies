package com.example.shika.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shika.movies.R;
import com.example.shika.movies.activities.DetailsActivity;
import com.example.shika.movies.utils.MovieData;
import com.example.shika.movies.utils.Tages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shika on 12/18/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {


    Context context;
    ArrayList<MovieData> arrayList;
    ClickCallback callback;
    LayoutInflater inflater;
    public RecyclerViewAdapter(Context context , ArrayList<MovieData> arrayList){
       this.arrayList = arrayList;
        this.context = context;
        callback = (ClickCallback) context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item , parent , false);
        viewHolder mviewHolder=new viewHolder(view);
        return mviewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {


        String image_path = Tages.BASE_IMAGE_PATH + Tages.COVER_DEFAULT_SIZE+arrayList.get(position).getPosterImage();
     Picasso.with(context).load(Uri.parse(image_path))
             .into(holder.imageView);
        holder.textView.setText(arrayList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;


        public viewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_card_backdrop_image_view);
            textView = (TextView) itemView.findViewById(R.id.movie_card_title_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    callback.onMovieCardClicked(arrayList.get(getPosition()).getId());
                 /*   Intent intent = new Intent(context , DetailsActivity.class);
                    intent.putExtra(Tages.id , arrayList.get(getPosition()).getId());
                    intent.putExtra(Tages.backdrop_path,arrayList.get(getPosition()).getBackdrop());
                    intent.putExtra(Tages.poster_path,arrayList.get(getPosition()).getPosterImage());
                    intent.putExtra(Tages.title,arrayList.get(getPosition()).getTitle());

                    intent.putExtra(Tages.overview,arrayList.get(getPosition()).getOverView());

                    intent.putExtra(Tages.release_date,arrayList.get(getPosition()).getRelease_date());

                    context.startActivity(intent);*/
                }
            });
        }
    }



    public interface ClickCallback {

        void onMovieCardClicked(int id);

    }

}
