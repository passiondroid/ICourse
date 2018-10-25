package com.icloud.cronin.peter.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.icloud.cronin.peter.data.model.RaceLocation;

import java.util.List;

/**
 * Created by Arif Khan on 21/10/18.
 */
@Dao
public interface LocationDao {

    @Query("SELECT * FROM RACE_LOCATION WHERE markerCharacter = :marker")
    RaceLocation findLocationByMarker(String marker);

    @Insert
    void insertAll(RaceLocation... raceLocations);

    @Insert
    long insert(RaceLocation raceLocations);

    @Query("SELECT * FROM RACE_LOCATION")
    List<RaceLocation> getAllLocations();

    @Query("SELECT markerCharacter FROM RACE_LOCATION")
    String[] getAllMarkers();

    @Delete
    int deleteLocation(RaceLocation raceLocation);

    @Update
    int updateLocation(RaceLocation raceLocation);

}
