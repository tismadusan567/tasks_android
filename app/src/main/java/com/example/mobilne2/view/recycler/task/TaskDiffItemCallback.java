package com.example.mobilne2.view.recycler.task;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.mobilne2.model.Task;

public class TaskDiffItemCallback extends DiffUtil.ItemCallback<Task> {

    @Override
    public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        //TODO: promeni ovo
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        if (newItem.isDirty()) {
            newItem.setDirty(false);
            return false;
        }
        return oldItem.getId() == newItem.getId()
                && oldItem.getTitle().equals(newItem.getTitle())
                && oldItem.getStartTime().equals(newItem.getStartTime())
                && oldItem.getEndTime().equals(newItem.getEndTime())
                && oldItem.getDescription().equals(newItem.getDescription())
                && oldItem.getPriority().equals(newItem.getPriority());
    }
}
