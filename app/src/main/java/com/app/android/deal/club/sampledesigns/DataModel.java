package com.app.android.deal.club.sampledesigns;

public class DataModel {
   String _title, _time_date;

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_time_date() {
        return _time_date;
    }

    public void set_time_date(String _time_date) {
        this._time_date = _time_date;
    }

    public DataModel(String _title, String _time_date) {
        this._title = _title;
        this._time_date = _time_date;
    }
}
