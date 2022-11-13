package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.fitness.Pages.MuscleExerciseList.MuscleExerciseListActivity;
import com.example.fitness.Pages.SearchPage.SearchPageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, MuscleExerciseListActivity.class));



//        FragmentManager fm = getSupportFragmentManager();
//        Fragment TestFragment = new TestFragment();
//        fm.beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container, TestFragment).commit();
        Log.d("MainActivity", "onCreate was called");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("MainActivity", "onPause was called");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("MainActivity", "onResume was called");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("MainActivity", "onDestroy was called");
    }
}