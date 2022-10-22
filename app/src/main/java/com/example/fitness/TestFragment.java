package com.example.fitness;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;

public class TestFragment extends Fragment {

    EditText editTextDesc;
    Button btnUpdate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_test, container, false);
        final String TAG = "exercise";
        TextView theTextView = v.findViewById(R.id.viewExercise);
//        editTextDesc = v.findViewById(R.id.editText);
//        btnUpdate = v.findViewById(R.id.updateText);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        Map<String,Object> user = new HashMap<>();
//        user.put("fistName", "Moid");
//        user.put("lastName", "Shafi");
//        user.put("description", "Student");

        db.collection("Muscles/Biceps/Bicep Exercises")
                        .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                String exercise = document.get("Desc").toString();
                                                theTextView.setText(exercise);
                                            }
                                        } else{
                                            Log.d(TAG, "Error getting documents: " , task.getException());
                                        }
                                    }
                                });
        Log.d("TestFragment", "onCreateView was called");

//        btnUpdate.setOnClickListener(
//                if(editTextDesc.)
//        );

        return v;
    }
    
}