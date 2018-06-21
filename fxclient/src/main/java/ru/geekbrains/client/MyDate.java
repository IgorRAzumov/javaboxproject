package ru.geekbrains.client;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MyDate extends Date {

    public MyDate(long date) {
        super(date);
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("dd/MM/yyyy").format(this);
    }
}

