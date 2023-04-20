package com.example.mobilne2.view.recycler.task;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilne2.R;
import com.example.mobilne2.model.Task;
import com.example.mobilne2.view.EditTaskActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.Consumer;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.ViewHolder> {

    private final Consumer<Task> onTaskClicked;

    public TaskAdapter(@NonNull DiffUtil.ItemCallback<Task> diffCallback, Consumer<Task> onTaskClicked) {
        super(diffCallback);
        this.onTaskClicked = onTaskClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        return new ViewHolder(view, parent.getContext(), position -> {
            Task task = getItem(position);
            onTaskClicked.accept(task);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = getItem(position);
        holder.bind(task);
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

        public void bind(Task task) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
            String text = sdf.format(task.getStartTime()) + " - " + sdf.format(task.getEndTime());
            ((TextView) itemView.findViewById(R.id.task_time_interval)).setText(text);
            ((TextView) itemView.findViewById(R.id.task_name)).setText(task.getTitle());

            View icon = itemView.findViewById(R.id.task_icon);

            int colorId;
            switch (task.getPriority()) {
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
            icon.setBackgroundColor(ContextCompat.getColor(context, colorId));

            ImageButton button = itemView.findViewById(R.id.edit_button);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("edit_task", task);
                context.startActivity(intent);
            });
        }

    }
}

