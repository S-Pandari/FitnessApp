package com.example.fitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.UUID;

public class TestFragment extends Fragment {

    private EditText mName, mDesc;
    private Button mSaveBtn, mShowBtn;
    private FirebaseFirestore db;
    private String uName, uDesc, uId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_test, container, false);
        final String TAG = "exercise";
        TextView theTextView = v.findViewById(R.id.viewExercise);
        mName = v.findViewById(R.id.edit_title);
        mDesc = v.findViewById(R.id.edit_desc);
        mSaveBtn = v.findViewById(R.id.save_btn);
        mShowBtn = v.findViewById(R.id.show_btn);

        db = FirebaseFirestore.getInstance();
        // getIntent().getExtras
        Bundle bundle = ((Activity) getContext()).getIntent().getExtras();

        if (bundle != null) {
            mSaveBtn.setText("Update");
            uName = bundle.getString("uName");
            uId = bundle.getString("uId");
            uDesc = bundle.getString("uDesc");
            mName.setText(uName);
            mDesc.setText(uDesc);
        } else {
            mSaveBtn.setText("Save");
        }

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ShowActivity.class));
            }
        });

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

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                String desc = mDesc.getText().toString();

                Bundle bundleOne = ((Activity) getContext()).getIntent().getExtras();
                if (bundleOne != null) {
                    String id = uId;
                    updateToFireStore(id, name, desc);
                }
                String id = UUID.randomUUID().toString();

                saveToFireStore(id, name, desc);
            }
        });

        return v;
    }

    private void updateToFireStore(String id, String name, String desc) {
        db.collection("Documents").document(id).update("name", name, "desc", desc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Data Updated!", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }

    private void saveToFireStore(String id, String name, String desc) {
        if (!name.isEmpty() && !desc.isEmpty()){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("desc", desc);

            db.collection("Documents").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(),"Empty Fields not Allowed", Toast.LENGTH_SHORT).show();
        }
    }
    
}