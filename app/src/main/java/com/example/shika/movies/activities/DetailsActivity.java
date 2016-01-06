package com.example.shika.movies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shika.movies.R;
import com.example.shika.movies.fragments.FragmentDetails;
import com.example.shika.movies.utils.Tages;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle b = new Bundle();
        b.putInt(Tages.id , getIntent().getExtras().getInt(Tages.id));



        if (savedInstanceState == null) {
            FragmentDetails details = new FragmentDetails();
            details.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, details)
                    .commit();
        }

    }
}
