package com.example.mobilne2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobilne2.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecyclerViewModel extends ViewModel {

    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    private final MutableLiveData<Date> currentDay = new MutableLiveData<>();
    private List<Task> allTasks = new ArrayList<>();

    public RecyclerViewModel() {
        for (int i = 0; i <= 100; i++) {
            Task car = new Task(Integer.toString(i), Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), "description", Task.Priority.HIGH);
            allTasks.add(car);
        }
        // We are doing this because cars.setValue in the background is first checking if the reference on the object is same
        // and if it is it will not do notifyAll. By creating a new list, we get the new reference everytime
        ArrayList<Task> listToSubmit = new ArrayList<>(allTasks);
        tasks.setValue(listToSubmit);
        currentDay.setValue(Calendar.getInstance().getTime());
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public MutableLiveData<Date> getCurrentDay() {
        return currentDay;
    }
}
