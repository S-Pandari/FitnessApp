package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.fitness.Pages.MuscleExerciseList.MuscleExerciseListActivity;
import com.example.fitness.Pages.SearchPage.SearchPageActivity;

public class MainActivity extends AppCompatActivity {

    private Button mStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStart = findViewById(R.id.start_btn);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MuscleExerciseListActivity.class));
            }
        });
//        startActivity(new Intent(this, MuscleExerciseListActivity.class));



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