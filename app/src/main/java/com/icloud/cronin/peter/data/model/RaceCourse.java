package com.icloud.cronin.peter.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "RACE_COURSE")
public class RaceCourse {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "courseNumber", index = true)
    private String courseNumber;

    @ColumnInfo(name = "course")
	private String course;

    @ColumnInfo(name = "dateAdded")
    private Date dateAdded;

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
