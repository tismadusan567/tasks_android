package com.example.mobilne2.view.recycler.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilne2.R;
import com.example.mobilne2.model.CalendarDate;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.Consumer;

public class CalendarDateAdapter extends ListAdapter<CalendarDate, CalendarDateAdapter.ViewHolder> {
    private final Consumer<CalendarDate> onDateClicked;

    public CalendarDateAdapter(@NonNull DiffUtil.ItemCallback<CalendarDate> diffCallback, Consumer<CalendarDate> onDateClicked) {
        super(diffCallback);
        this.onDateClicked = onDateClicked;
    }

    @NonNull
    @Override
    public CalendarDateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        return new CalendarDateAdapter.ViewHolder(view, parent.getContext(), position -> {
            CalendarDate date = getItem(position);
            onDateClicked.accept(date);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarDateAdapter.ViewHolder holder, int position) {
        CalendarDate date = getItem(position);
        holder.bind(date);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
        }

        public void bind(CalendarDate date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.US);
            String text = sdf.format(date.getDate());
            ((TextView) itemView.findViewById(R.id.calendar_item_textview)).setText(text);
            View l = itemView.findViewById(R.id.calendar_item_layout);
            int colorId;
            switch (date.getPriority()) {
                case HIGH:
                    colorId = R.color.red;
                    break;
                case MID:
                    colorId = R.color.yellow;
                    break;
                case LOW:
                    colorId = R.color.green;
                    break;
                default:
                    colorId = R.color.white;
                    break;
            }
            l.setBackgroundColor(ContextCompat.getColor(context, colorId));
        }

    }
}
