package com.example.fitness;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class Touch extends ItemTouchHelper.SimpleCallback{

    private MyAdapter adapter;

    public Touch(MyAdapter adapter) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int pos = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            adapter.updateInfo(pos);
            adapter.notifyDataSetChanged();
        } else if (direction == ItemTouchHelper.RIGHT) {
            adapter.deleteInfo(pos);
        } else if (direction == ItemTouchHelper.UP){
            adapter.notifyItemChanged(pos);
            adapter.viewDetails(pos);
        }
    }
}
