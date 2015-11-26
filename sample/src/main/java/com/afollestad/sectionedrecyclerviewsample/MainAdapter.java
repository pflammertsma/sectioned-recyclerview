package com.afollestad.sectionedrecyclerviewsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

/**
 * @author Aidan Follestad (afollestad)
 */
public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MainVH> {

    @Override
    public int getSectionCount() {
        return 20;
    }

    @Override
    public int getItemCount(int section) {
        return 6;
    }

    @Override
    public void onBindHeaderViewHolder(MainVH holder, int section) {
        holder.title.setText(String.format("Section %d", section + 1));
    }

    @Override
    public void onBindViewHolder(MainVH holder, int section, int position) {
        holder.title.setText(String.format("S:%d, P:%d", section + 1, position + 1));
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, boolean header) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(header ? R.layout.list_item_header : R.layout.list_item_main, parent, false);
        return new MainVH(v);
    }

    public static class MainVH extends RecyclerView.ViewHolder {

        final TextView title;

        public MainVH(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
