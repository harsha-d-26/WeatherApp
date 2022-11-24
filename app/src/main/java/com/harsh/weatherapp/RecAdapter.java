package com.harsh.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {
    Context context;
    List<DataModel> data;
    public RecAdapter(Context context, List<DataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.display_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel elem = data.get(position);
        holder.bind(elem);
    }

    @Override
    public int getItemCount() {
        int limit = 5;
        return Math.min(data.size(),limit);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView prop = itemView.findViewById(R.id.property);
        TextView val = itemView.findViewById(R.id.value);
        public void bind(DataModel data){
            prop.setText(data.property);
            val.setText(data.value);
        }
    }
}
