package org.jasey.unforgetit.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jasey.unforgetit.R;
import org.jasey.unforgetit.entity.Task;
import org.jasey.unforgetit.utils.TaskUtil;

import java.util.Date;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private Task[] mTaskSet;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView date;

        ViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.card_view);
            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.date);
        }
    }

    public MyRecyclerViewAdapter(Task[] mTaskSet) {
        this.mTaskSet = mTaskSet;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.card_view_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task t = mTaskSet[position];

        holder.title.setText(t.getTitle());
        holder.date.setText(TaskUtil.formatDate(t.getDate()));

        switch (t.getPriorityLevel()) {
            case (Task.PRIORITY_LOW):
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorBlue));
                break;
            case (Task.PRIORITY_NORMAL):
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorYellow));
                break;
            case (Task.PRIORITY_HIGH):
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorOrange));
                break;
        }

        if (t.getDate().compareTo(new Date()) <= 0) {
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRed));
        }

        if (t.isDone()) {
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorGreen));
        }
    }

    @Override
    public int getItemCount() {
        return mTaskSet.length;
    }
}
