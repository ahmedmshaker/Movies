package com.example.shika.movies.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shika.movies.R;
import com.example.shika.movies.activities.MainActivity;
import com.example.shika.movies.adapters.RecyclerViewAdapter;
import com.example.shika.movies.db.MoviesDbDatasource;
import com.example.shika.movies.utils.MovieData;
import com.example.shika.movies.utils.Tages;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class GridFragment extends Fragment {

    ArrayList<MovieData> datas;


    RequestQueue requestQueue;


    final int ORDER_POPULARITY_DESC = 0;
    final int ORDER_RATING_DESC  = 1 ;
    final int FILTER_FAVORITES = 2;
    int mCurrentOrder = 0 ;
    String PREF_SORT_ORDER = "sort";
    String url = "http://api.themoviedb.org/3/discover/movie?api_key=" + Tages.APi_Key;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    String PREFERENCE_FILE = "SortFileName";

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_grid, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.moviesRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));




        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeMoviesList();
    }

    private void SendjsonRequest(String mUrl) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                datas = reponse(jsonObject);

                if (datas != null) {
                    adapter = new RecyclerViewAdapter(getActivity(), datas);
                    recyclerView.setAdapter(adapter);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(request);
    }


    public ArrayList<MovieData> reponse(JSONObject jsonObject) {


        ArrayList<MovieData> mydata = new ArrayList<>();
        if (jsonObject == null || jsonObject.length() == 0) {
            return null;
        } else {
            try {
                JSONArray arrMovies = jsonObject.getJSONArray(Tages.result);
                for (int i = 0; i < arrMovies.length(); i++) {
                    JSONObject jsonObject1 = arrMovies.getJSONObject(i);
                    //long id=jsonObject1.getLong(KEY_ID);
                    int id = jsonObject1.getInt(Tages.id);
                    String title = jsonObject1.getString(Tages.title);
                    String poster = jsonObject1.getString(Tages.poster_path);

                    mydata.add(new MovieData(id, title, poster));


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return mydata;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_order , menu);
    }



   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        switch (itemId) {
            case R.id.popularity:
                listPopularMovies();

                mCurrentOrder = ORDER_POPULARITY_DESC;
                editor.putInt(PREF_SORT_ORDER, mCurrentOrder);
                editor.apply();
                Toast.makeText(getActivity() , "popularity" , Toast.LENGTH_LONG).show();
                return true;

            case R.id.vote_average:
                listHighestRatedMovies();

                mCurrentOrder = ORDER_RATING_DESC;
                editor.putInt(PREF_SORT_ORDER, mCurrentOrder);
                editor.apply();
                return true;

            case R.id.favourite:
                listFavoriteMovies();

                mCurrentOrder = FILTER_FAVORITES;
                editor.putInt(PREF_SORT_ORDER, mCurrentOrder);
                editor.apply();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void initializeMoviesList() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCE_FILE, Activity.MODE_PRIVATE);
        mCurrentOrder = preferences.getInt(PREF_SORT_ORDER, ORDER_POPULARITY_DESC);

        switch (mCurrentOrder) {
            case ORDER_POPULARITY_DESC:
                listPopularMovies();
                break;
            case ORDER_RATING_DESC:
                listHighestRatedMovies();
                break;
            case FILTER_FAVORITES:
                listFavoriteMovies();
                break;
        }
    }

    public void listPopularMovies() {

        String soryByPopularityUrl  = url + "&sort_by=popularity.desc";
        SendjsonRequest(soryByPopularityUrl);

    }

    public void listHighestRatedMovies(){

        String soryByPopularityUrl  = url + "&sort_by=vote_average.desc";
        SendjsonRequest(soryByPopularityUrl);
    }

    public void listFavoriteMovies(){

        new LoadDataFromDb().execute((Object[])null);

    }

public class LoadDataFromDb extends AsyncTask<Object ,Object,Cursor>{

    @Override
    protected Cursor doInBackground(Object... params) {
        MoviesDbDatasource dbDatasource = new MoviesDbDatasource(getActivity());

        return dbDatasource.getAllMoviesData();
    }

    @Override
    protected void onPostExecute(Cursor cursor) {

        datas = new ArrayList<>();
        super.onPostExecute(cursor);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    datas.add(new MovieData(cursor.getInt(0) , cursor.getString(1) , cursor.getString(2)));
                }while (cursor.moveToNext());

            }

            if (datas!=null){
            adapter = new RecyclerViewAdapter(getActivity(), datas);
            recyclerView.setAdapter(adapter);}
        }
    }
}

}
