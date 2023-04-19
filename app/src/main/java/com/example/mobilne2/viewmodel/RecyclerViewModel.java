package com.example.mobilne2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobilne2.model.CalendarDate;
import com.example.mobilne2.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RecyclerViewModel extends ViewModel {

    private final MutableLiveData<SortedSet<Task>> tasks = new MutableLiveData<>();
    private final MutableLiveData<Date> currentDay = new MutableLiveData<>();
    private SortedSet<Task> allTasks = new TreeSet<>();

    private final MutableLiveData<List<CalendarDate>> dates = new MutableLiveData<>();
    private final MutableLiveData<Date> currentMonth = new MutableLiveData<>();
    private List<CalendarDate> allDates = new ArrayList<>();

    public RecyclerViewModel() {
        //monday 5th january 2015
        Date firstDate = new GregorianCalendar(2015, Calendar.JANUARY, 5, 12, 0, 0).getTime();
        for (int i = 0; i <= 5000; i++) {
            CalendarDate date = new CalendarDate(new Date(firstDate.getTime() + (long) i *3600*1000*24));
            allDates.add(date);
        }
        allDates.sort(Comparator.comparing(CalendarDate::getDate));
        // We are doing this because cars.setValue in the background is first checking if the reference on the object is same
        // and if it is it will not do notifyAll. By creating a new list, we get the new reference everytime
        ArrayList<CalendarDate> listToSubmit = new ArrayList<>(allDates);
        dates.setValue(listToSubmit);
        currentMonth.setValue(Calendar.getInstance().getTime());

        TreeSet<Task> setToSubmit = new TreeSet<>(allTasks);
        tasks.setValue(setToSubmit);
        currentDay.setValue(Calendar.getInstance().getTime());
    }

    public boolean addTask(Task newTask) {
        for (Task task: allTasks) {
             if (
                     newTask.getEndTime().after(task.getStartTime())
                     && newTask.getStartTime().before(task.getEndTime())
             ) {
                 return false;
             }
        }
        CalendarDate calendarDate = findCalendarDateForDate(newTask.getStartTime());
        if (calendarDate == null) return false;

        allTasks.add(newTask);
        calendarDate.addTask(newTask);
        tasks.getValue().add(newTask);

        tasks.setValue(new TreeSet<>(tasks.getValue()));
        dates.setValue(new ArrayList<>(dates.getValue()));

        return true;
    }

    public void filterTasksByPriority(List<Task.Priority> priorities) {
        SortedSet<Task> filteredSet = allTasks
                .stream()
                .filter(task -> priorities.contains(task.getPriority()))
                .collect(Collectors.toCollection(TreeSet::new));
        tasks.setValue(filteredSet);
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private CalendarDate findCalendarDateForDate(Date date) {
        for (CalendarDate calendarDate: allDates) {
            if (isSameDay(date, calendarDate.getDate())) {
                return calendarDate;
            }
        }
        return null;
    }

    public MutableLiveData<SortedSet<Task>> getTasks() {
        return tasks;
    }

    public MutableLiveData<Date> getCurrentDay() {
        return currentDay;
    }

    public MutableLiveData<List<CalendarDate>> getDates() {
        return dates;
    }

    public MutableLiveData<Date> getCurrentMonth() {
        return currentMonth;
    }

    public int getTodayPosition() {
        for (int i=0;i<dates.getValue().size();i++) {
            if (isSameDay(dates.getValue().get(i).getDate(), Calendar.getInstance().getTime())) {
                return i;
            }
        }
        return -1;
    }
}
