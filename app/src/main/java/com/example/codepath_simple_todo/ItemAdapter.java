package com.example.codepath_simple_todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Responsible for taking the data from a position and putting it into the viewholder
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    OnLongClickListener longClickListener;

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    OnClickListener clickListener;

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    List<String> items;

    public ItemAdapter(List<String> items,
                       OnLongClickListener longClickListener,
                       OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // use layout inflator to inflate view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        // wrap it inside a view holder and return it
        return new ViewHolder(todoView);
    }

    // bind data to particular ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // grab item at the text position
        String item = items.get(position);
        // bind item to ViewHolder
        holder.bind(item);
    }

    // tells recyclerview how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // container to provide access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(android.R.id.text1);
        }

        // update the view inside view holder with data
        public void bind(String item) {
            textViewItem.setText(item);

            textViewItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });

            textViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
