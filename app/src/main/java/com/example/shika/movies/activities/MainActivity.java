package com.example.shika.movies.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.shika.movies.R;
import com.example.shika.movies.adapters.RecyclerViewAdapter;
import com.example.shika.movies.fragments.FragmentDetails;
import com.example.shika.movies.fragments.GridFragment;
import com.example.shika.movies.utils.Tages;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ClickCallback {

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /*GridFragment gridFragment =new GridFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container , gridFragment)
                .commit();*/

        mTwoPane = findViewById(R.id.movie_detail_container) != null;

    }

    @Override
    public void onMovieCardClicked(int id) {


        Bundle b = new Bundle();
        b.putInt(Tages.id, id);

        if (mTwoPane) {
            Fragment newDetailFragment = new FragmentDetails();
            newDetailFragment.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, newDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Tages.id, id);
            startActivity(intent);

        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order, menu);
        return true;
    }*/



}
