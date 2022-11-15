package com.example.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitness.Pages.MuscleExerciseList.MuscleExerciseListActivity;
import com.example.fitness.Pages.ViewExercise.ViewExercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    private ShowActivity activity;
    private List<Model> mList;
    private List<Model> mListFull;

    // for delete
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MyAdapter(ShowActivity activity, List<Model> mList){
        this.activity = activity;
        this.mList = mList;
        this.mListFull = new ArrayList<>(mList);
    }

    public void addToList(Model newItem){
        mList.add(newItem);
        mListFull.add(newItem);
        notifyDataSetChanged();
    }

    public void updateInfo(int position) {
        Model item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uId", item.getId());
        bundle.putString("uName", item.getName());
        bundle.putString("uDesc", item.getDesc());
        bundle.putString("uUrl", item.getUrl());
        Intent intent = new Intent(activity, MuscleExerciseListActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    // add go to info to start new activity with the exercise info
    public void viewDetails(int postion){
        Model item = mList.get(postion);
        Bundle bundle = new Bundle();
        bundle.putString("uId", item.getId());
        bundle.putString("uName", item.getName());
        bundle.putString("uDesc", item.getDesc());
        bundle.putString("uUrl", item.getUrl());
        Intent intent = new Intent(activity, ViewExercise.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);

    }

    public void deleteInfo(int postion) {
        Model item = mList.get(postion);
        db.collection("Documents").document(item.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            notifyRemoved(postion);
                            Toast.makeText(activity, "Data Deleted!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    private void notifyRemoved(int pos) {
        mList.remove(pos);
        notifyItemRemoved(pos);
        activity.showInfo();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mList.get(position).getName());
        holder.desc.setText(mList.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, desc, url;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameText);
            desc = itemView.findViewById(R.id.descText);
        }
    }

    @Override
    public Filter getFilter() {
        return ExerciseFilter;
    }

    private Filter ExerciseFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Model> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mListFull);
                Log.d("Filter", "ListShowsAll");
            }
            else {
                Log.d("Filter", "ListBeingFiltered");
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Model exercise : mListFull){
                    if(exercise.getName().toLowerCase().contains(filterPattern) ){
                        filteredList.add(exercise);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.d("Filter", Integer.toString(results.count));

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Log.d("Filter", Integer.toString(mList.size()));
            mList.clear();
            mList.addAll((List) filterResults.values);
            notifyDataSetChanged();
            Log.d("Filter", "PublishHappened");
            Log.d("Filter", Integer.toString(filterResults.count));
        }
    };
}
