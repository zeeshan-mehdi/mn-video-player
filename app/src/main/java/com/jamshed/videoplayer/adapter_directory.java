package com.jamshed.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class adapter_directory extends Adapter<adapter_directory.ViewHolder> {
    Activity activity;
    List<Directory> al_directories;
    Context context;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        LinearLayout llSelect;
        TextView tvCount;
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
            this.tvTitle = (TextView) view.findViewById(R.id.tv_dir);
            this.tvCount = (TextView) view.findViewById(R.id.tv_count);
            this.llSelect = (LinearLayout) view.findViewById(R.id.ll_select);
        }
    }

    public adapter_directory(Context context, List<Directory> list, Activity activity) {
        this.al_directories = list;
        this.context = context;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_directories, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {


        viewHolder.tvTitle.setText(( this.al_directories.get(i)).getPath().getName());
        TextView textView = viewHolder.tvCount;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf((this.al_directories.get(i)).getCount()));
        stringBuilder.append(" Videos");
        textView.setText(stringBuilder.toString());
        viewHolder.llSelect.setOnClickListener(new OnClickListener() {
            public void onClick(View view1) {

                Intent view = new Intent(adapter_directory.this.context, DetailActivity.class);
                view.putExtra("file", ( adapter_directory.this.al_directories.get(i)).getPath());
                adapter_directory.this.activity.startActivity(view);
            }
        });
    }

    public int getItemCount() {
        return this.al_directories.size();
    }
}

