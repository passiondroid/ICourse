package com.icloud.cronin.peter.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.icloud.cronin.peter.data.model.RaceCourse;
import java.util.List;

/**
 * Created by Arif Khan on 21/10/18.
 */
@Dao
public interface RaceCourseDao {

    @Query("SELECT * FROM RACE_COURSE WHERE courseNumber =:courseNumber")
    RaceCourse findCourseByNumber(String courseNumber);

    @Insert
    long addRaceCourse(RaceCourse raceCourse);

    @Query("SELECT * FROM RACE_COURSE")
    List<RaceCourse> getAllCourses();

    @Query("DELETE FROM RACE_COURSE WHERE courseNumber =:courseNumber")
    int deleteCourseByNumber(String courseNumber);

    @Update
    int updateCourse(RaceCourse raceCourse);

}
