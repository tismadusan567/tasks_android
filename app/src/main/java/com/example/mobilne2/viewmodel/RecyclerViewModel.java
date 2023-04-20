package com.example.mobilne2.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobilne2.model.CalendarDate;
import com.example.mobilne2.model.Database;
import com.example.mobilne2.model.Task;
import com.example.mobilne2.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class RecyclerViewModel extends ViewModel {

    public static final String STORE_KEY = "RecyclerViewModel";

    private final MutableLiveData<SortedSet<Task>> tasks = new MutableLiveData<>();
    private final MutableLiveData<Date> currentDay = new MutableLiveData<>();

    private MutableLiveData<List<Task.Predicate>> predicates = new MutableLiveData<>();

    private final MutableLiveData<List<CalendarDate>> dates = new MutableLiveData<>();
    private final MutableLiveData<Date> currentMonth = new MutableLiveData<>();
    private List<CalendarDate> allDates = new ArrayList<>();

    private final MutableLiveData<User> user = new MutableLiveData<>();

    public RecyclerViewModel() {
        initDates();

        initTasks();

        initDummyData();

        predicates.setValue(new ArrayList<>());
    }

    private void initDummyData() {
        for (int i = 0; i <= 50; i++) {
            Task task = new Task(
                    Database.getInstance().getUniqueId(),
                    Integer.toString(i),
                    new Date(Calendar.getInstance().getTime().getTime() + 1000L*3600*i*24),
                    new Date(Calendar.getInstance().getTime().getTime() + 1000L*3600*(i*24 + 1)),
                    "description",
                    i % 2 == 0 ? Task.Priority.HIGH : Task.Priority.LOW
            );
            Task task2 = new Task(
                    Database.getInstance().getUniqueId(),
                    "-" + i,
                    new Date(Calendar.getInstance().getTime().getTime() - 1000L*3600*i*24),
                    new Date(Calendar.getInstance().getTime().getTime() - 1000L*3600*(i*24 + 1)),
                    "description",
                    i % 2 == 0 ? Task.Priority.HIGH : Task.Priority.LOW
            );

            addTask(task);
            addTask(task2);
        }
    }

    public void loadFromDatabase() {
        allDates.forEach(CalendarDate::clear);

        Database.getInstance().getAllTasks().stream().forEach(task -> {
            CalendarDate calendarDate = findCalendarDateForDate(task.getStartTime());
            calendarDate.addTask(task);
        });

        filterTasksByPredicates();
    }

    private void initTasks() {
        TreeSet<Task> setToSubmit = new TreeSet<>(Database.getInstance().getAllTasks());
        tasks.setValue(setToSubmit);
        currentDay.setValue(Calendar.getInstance().getTime());
    }

    private void initDates() {
        //monday 5th january 2015
        Date firstDate = new GregorianCalendar(2015, Calendar.JANUARY, 5, 12, 0, 0).getTime();
        for (int i = 0; i <= 5000; i++) {
            CalendarDate date = new CalendarDate(new Date(firstDate.getTime() + (long) i *3600*1000*24));
            allDates.add(date);
        }
        allDates.sort(Comparator.comparing(CalendarDate::getDate));

        ArrayList<CalendarDate> listToSubmit = new ArrayList<>(allDates);
        dates.setValue(listToSubmit);
        currentMonth.setValue(Calendar.getInstance().getTime());
    }

    public boolean addTask(Task newTask) {
        for (Task task: Database.getInstance().getAllTasks()) {
             if (
                     newTask.getEndTime().after(task.getStartTime())
                     && newTask.getStartTime().before(task.getEndTime())
             ) {
                 return false;
             }
        }
        CalendarDate calendarDate = findCalendarDateForDate(newTask.getStartTime());
        if (calendarDate == null) return false;

        Database.getInstance().addTask(newTask);
        calendarDate.addTask(newTask);
        tasks.getValue().add(newTask);

        tasks.setValue(new TreeSet<>(tasks.getValue()));
        dates.setValue(new ArrayList<>(dates.getValue()));

        return true;
    }

    public void addPredicate(Task.Predicate predicate) {
        predicates.getValue().add(predicate);
        predicates.setValue(new ArrayList<>(predicates.getValue()));
    }

    public void removePredicate(Task.Predicate predicate) {
        predicates.getValue().remove(predicate);
        predicates.setValue(new ArrayList<>(predicates.getValue()));
    }

    public void filterTasksByPredicates() {
        SortedSet<Task> filteredSet = Database.getInstance().getAllTasks()
                .stream()
                .filter(task -> predicates.getValue()
                        .stream()
                        .allMatch(p -> p.satisfiesCondition(task))
                )
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

    public MutableLiveData<List<Task.Predicate>> getPredicates() {
        return predicates;
    }

    public int getTodayPosition() {
        for (int i=0;i<dates.getValue().size();i++) {
            if (isSameDay(dates.getValue().get(i).getDate(), Calendar.getInstance().getTime())) {
                return i;
            }
        }
        return -1;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }
}
