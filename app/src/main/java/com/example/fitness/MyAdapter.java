package com.example.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ShowActivity activity;
    private List<Model> mList;

    // for delete
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MyAdapter(ShowActivity activity, List<Model> mList){
        this.activity = activity;
        this.mList = mList;
    }

    public void updateInfo(int position) {
        Model item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uId", item.getId());
        bundle.putString("uName", item.getName());
        bundle.putString("uDesc", item.getDesc());
        Intent intent = new Intent(activity, TestFragment.class);
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

        TextView name, desc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameText);
            desc = itemView.findViewById(R.id.descText);
        }
    }
}
