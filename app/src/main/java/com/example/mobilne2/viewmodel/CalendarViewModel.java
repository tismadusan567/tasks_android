package com.example.mobilne2.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobilne2.model.CalendarDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarViewModel extends ViewModel {

    private final MutableLiveData<List<CalendarDate>> dates = new MutableLiveData<>();
    private final MutableLiveData<Date> currentMonth = new MutableLiveData<>();
    private List<CalendarDate> allDates = new ArrayList<>();

    public CalendarViewModel() {
        for (int i = 0; i <= 100; i++) {
            CalendarDate date = new CalendarDate(new Date(Calendar.getInstance().getTime().getTime() + (long) i *3600*1000*24));
            allDates.add(date);
        }
        // We are doing this because cars.setValue in the background is first checking if the reference on the object is same
        // and if it is it will not do notifyAll. By creating a new list, we get the new reference everytime
        ArrayList<CalendarDate> listToSubmit = new ArrayList<>(allDates);
        dates.setValue(listToSubmit);
        currentMonth.setValue(Calendar.getInstance().getTime());
    }

    public MutableLiveData<List<CalendarDate>> getDates() {
        return dates;
    }

    public MutableLiveData<Date> getCurrentMonth() {
        return currentMonth;
    }
}

