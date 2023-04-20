package com.example.mobilne2.view.recycler.calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.mobilne2.model.CalendarDate;

public class CalendarDateDiffItemCallback extends DiffUtil.ItemCallback<CalendarDate> {

    @Override
    public boolean areItemsTheSame(@NonNull CalendarDate oldItem, @NonNull CalendarDate newItem) {
        //TODO: promeni ovo
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull CalendarDate oldItem, @NonNull CalendarDate newItem) {
        return oldItem.getDate().equals(newItem.getDate())
                && oldItem.getPriority().equals(newItem.getPriority());
    }
}
