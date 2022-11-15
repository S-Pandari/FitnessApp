package com.example.fitness;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity implements Filterable {

    private RecyclerView showRecylerView;
    private FirebaseFirestore db;
    private MyAdapter adapter;
    private List<Model> list;
    private List<Model> listFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        showRecylerView = findViewById(R.id.showRecycle);
        showRecylerView.setHasFixedSize(true);
        showRecylerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter = new MyAdapter(this, list);
        showRecylerView.setAdapter(adapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new Touch(adapter));
        touchHelper.attachToRecyclerView(showRecylerView);

        listFull = new ArrayList<>(list);

        showInfo();

    }

    public void showInfo() {
        db.collection("Documents").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        for (DocumentSnapshot snapshot : task.getResult()) {
                            Model model = new Model(snapshot.getString("id"), snapshot.getString("name"), snapshot.getString("desc"), snapshot.getString("url"));
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowActivity.this, "Failure!!", Toast.LENGTH_SHORT);
                    }
                });
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private Filter ExerciseFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Model> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(listFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Model exercise : listFull){
                    if(exercise.getName().toLowerCase().contains(filterPattern) ){
                        filteredList.add(exercise);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List) filterResults.values);
        }
    };
}