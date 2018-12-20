package com.basava.finalweatherandroid.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Weather")
public class Weather extends Model {
    // Make sure to have a default constructor for every ActiveAndroid model
    public Weather() {
        super();
    }

    @Column(name = "city", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String city;

    @Column(name = "data")
    public String data;

    public static List<Weather> getWeathers(String city) {
        return new Select().from(Weather.class).where("city = ?", city).execute();
    }
}
