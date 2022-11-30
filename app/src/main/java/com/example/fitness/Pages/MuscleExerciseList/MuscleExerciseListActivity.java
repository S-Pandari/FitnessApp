package com.example.fitness.Pages.MuscleExerciseList;
import com.example.fitness.R;
import com.example.fitness.ShowActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Bind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.UUID;

public class MuscleExerciseListActivity extends AppCompatActivity {
    private static final String TAG = "MuscleExerciseListActivity";

//    private EditText mName, mDesc;
//    private Button mSaveBtn, mShowBtn;
//    private FirebaseFirestore db;
//    private String uName, uDesc, uId;

    //@Bind(R.id.edit_name) EditText mName;
    //@Bind(R.id.show_btns) Button mShowBtn;

    private Button mShowBtn,mSaveBtn,mInc,mDec;
    private EditText mName, mDesc, mUrl;
    private FirebaseFirestore db;
    private String uName, uDesc, uId, uUrl;
    private int fontSize = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_exercise_list);
        ButterKnife.bind(this);
        db = FirebaseFirestore.getInstance();

        mShowBtn = findViewById(R.id.show_btns);
        mSaveBtn = findViewById(R.id.save_btns);
        mName = findViewById(R.id.edit_names);
        mDesc = findViewById(R.id.edit_descs);
        mInc = findViewById(R.id.increase_btn);
        mDec = findViewById(R.id.decrease_btn);
        mUrl = findViewById(R.id.edit_urls);

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ShowActivity.class));
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mSaveBtn.setText("Update");
            uName = bundle.getString("uName");
            uId = bundle.getString("uId");
            uDesc = bundle.getString("uDesc");
            uUrl = bundle.getString("uUrl");
            mName.setText(uName);
            mDesc.setText(uDesc);
            mUrl.setText(uUrl);
        } else {
            mSaveBtn.setText("Save");
        }

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                String desc = mDesc.getText().toString();
                String url = mUrl.getText().toString();

                Bundle bundleOne = getIntent().getExtras();
                if (bundleOne != null) {
                    String id = uId;
                    updateToFireStore(id, name, desc, url);
                } else {
                    String id = UUID.randomUUID().toString();
                    saveToFireStore(id, name, desc, url);
                }
            }
        });

        mInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fontSize < 20) {
                    fontSize += 1f;
                    mName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                    mDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                    mUrl.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                }
            }
        });

        mDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fontSize > 15) {
                    fontSize -= 1f;
                    mName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                    mDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                    mUrl.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void updateToFireStore(String id, String name, String desc, String url) {
        db.collection("Documents").document(id).update("name", name, "desc", desc, "url", url)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Data Updated!", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }

    private void saveToFireStore(String id, String name, String desc, String url) {
        if (!name.isEmpty() && !desc.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("desc", desc);
            map.put("url", url);

            db.collection("Documents").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed Save", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Empty Fields not Allowed", Toast.LENGTH_SHORT).show();
        }
    }
}