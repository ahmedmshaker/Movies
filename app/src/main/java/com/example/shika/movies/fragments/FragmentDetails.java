package com.example.shika.movies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shika.movies.R;
import com.example.shika.movies.activities.DetailsActivity;
import com.example.shika.movies.adapters.RecyclerViewAdapter;
import com.example.shika.movies.adapters.VideoRecycler;
import com.example.shika.movies.db.MoviesDbDatasource;
import com.example.shika.movies.utils.MovieData;
import com.example.shika.movies.utils.Tages;
import com.example.shika.movies.utils.VideoData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentDetails extends Fragment {


    ImageView poster;
    ImageView backedbon;
    TextView title;
    TextView date;
    TextView rating;
    TextView summery;

    String poster_path = null;
    ImageView favourite_button;


    ArrayList<VideoData> datas;
    String url = "http://api.themoviedb.org/3/movie/";


    LinearLayout linearLayout , linearLayoutReview;
    public FragmentDetails() {
        // Required empty public constructor
    }


    RequestQueue requestQueue;
    Bundle b = null ;

    MoviesDbDatasource dbDatasource;
    int id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         dbDatasource= new MoviesDbDatasource(getActivity());

        requestQueue= Volley.newRequestQueue(getActivity());
        b= getArguments();
        id = b.getInt(Tages.id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_movie, container, false);


        favourite_button = (ImageView) v.findViewById(R.id.movie_detail_favorite_image_button);
        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty()&& poster_path != null){
                dbDatasource.SaveMovie(id , title.getText().toString() ,poster_path);
                Toast.makeText(getActivity() , "Your Favourite Saved",Toast.LENGTH_LONG).show();}
            }
        });
        linearLayoutReview = (LinearLayout) v.findViewById(R.id.movie_detail_reviews_container);
         linearLayout = (LinearLayout) v.findViewById(R.id.movie_detail_videos_container);
         backedbon = (ImageView) v.findViewById(R.id.movie_detail_backdrop_image_view);

         poster = (ImageView) v.findViewById(R.id.movie_detail_poster_image_view);


        rating = (TextView) v.findViewById(R.id.movie_detail_rating_text_view);
        title = (TextView) v.findViewById(R.id.movie_detail_title_text_view);
        date = (TextView) v.findViewById(R.id.movie_detail_release_date_text_view);
        summery = (TextView) v.findViewById(R.id.movie_detail_overview_text_view);
         return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (b!=null){
            String movieDetailByid = url + id +"?api_key="+ Tages.APi_Key;
            String myurl = url +id + "/videos?api_key="+Tages.APi_Key;
            String reviewUrl = url + id+ "/reviews?api_key="+ Tages.APi_Key;

            SendjsonRequestByID(movieDetailByid);
            SendjsonRequest(myurl);
            SendjsonRequestReview(reviewUrl);
        }else {
            Toast.makeText(getActivity() , "B == Null" , Toast.LENGTH_LONG).show();
        }




        }


    @Override
    public void onDetach() {
        super.onDetach();
      //  mListener = null;
    }

    private void SendjsonRequest(String req){

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET ,req,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


              datas = reponse(jsonObject);


                if (datas != null){

                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.videolist, null );

                    RecyclerView recyclerView  = (RecyclerView) v.findViewById(R.id.lv_video);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    VideoRecycler adapter =new VideoRecycler(getActivity() , datas);
                    recyclerView.setAdapter(adapter);
                    linearLayout.addView(v);


                }



            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(request);
    }

    public ArrayList<VideoData> reponse(JSONObject jsonObject) {


        ArrayList<VideoData> mydata = new ArrayList<>();
        if (jsonObject == null || jsonObject.length() == 0) {
            return null;
        } else {
            try {
                JSONArray arrMovies = jsonObject.getJSONArray(Tages.result);
                for (int i = 0; i < arrMovies.length(); i++) {
                    JSONObject jsonObject1 = arrMovies.getJSONObject(i);



                    String Key = jsonObject1.getString(Tages.Video_Key);

                    String Name = jsonObject1.getString(Tages.Video_Name);

                    mydata.add(new VideoData(Key , Name));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return mydata;
    }


    private void SendjsonRequestByID(String req){

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET ,req,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                if (jsonObject != null){



                    try {
                        String image_path = Tages.BASE_IMAGE_PATH ;
                        title.setText(jsonObject.getString(Tages.title));
                        summery.setText(jsonObject.getString(Tages.overview));

                        rating.setText(jsonObject.getDouble(Tages.vote_average)+"");
                        Picasso.with(getActivity()).load(image_path + Tages.BACKDROP_DEFAULT_SIZE + jsonObject.getString(Tages.backdrop_path)).into(backedbon);


                        poster_path = jsonObject.getString(Tages.poster_path);
                        Picasso.with(getActivity()).load(image_path + Tages.COVER_DEFAULT_SIZE + poster_path).into(poster);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
               /* if (datas!=null){
                    adapter = new RecyclerViewAdapter(getActivity() , datas);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(getActivity() , Tages.BASE_IMAGE_PATH+Tages.COVER_DEFAULT_SIZE+datas.get(0).getPosterImage(), Toast.LENGTH_LONG).show();
                }*/



            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(request);
    }

    private void SendjsonRequestReview(String req){

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET ,req,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                if (jsonObject != null){






                    readReview(jsonObject);

                }
               /* if (datas!=null){
                    adapter = new RecyclerViewAdapter(getActivity() , datas);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(getActivity() , Tages.BASE_IMAGE_PATH+Tages.COVER_DEFAULT_SIZE+datas.get(0).getPosterImage(), Toast.LENGTH_LONG).show();
                }*/



            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(request);
    }

    public void readReview(JSONObject jsonObject){

        if (jsonObject == null || jsonObject.length() == 0) {
            return ;
        } else {
            try {
                JSONArray arrMovies = jsonObject.getJSONArray(Tages.result);
                for (int i = 0; i < arrMovies.length(); i++) {
                    JSONObject jsonObject1 = arrMovies.getJSONObject(i);
                    //long id=jsonObject1.getLong(KEY_ID);

                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.movie_review_detail, null );

                    TextView auther = (TextView) v.findViewById(R.id.review_author_text_view);
                    TextView content = (TextView) v.findViewById(R.id.review_content_text_view);

                    auther.setText(jsonObject1.getString(Tages.author));
                    content.setText(jsonObject1.getString(Tages.content));


                    linearLayoutReview.addView(v);




                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
