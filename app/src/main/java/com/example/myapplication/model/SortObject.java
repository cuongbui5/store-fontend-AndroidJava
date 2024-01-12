package com.example.myapplication.model;

import androidx.annotation.NonNull;

public class SortObject {
    private String value;
    private String field;
    private String show;

    public SortObject(String value, String field, String show) {
        this.value = value;
        this.field = field;
        this.show = show;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    @NonNull
    @Override
    public String toString() {
        return show;
    }
}
