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

    public RaceCourse() {
    }

    public RaceCourse(@NonNull String courseNumber, String course) {
        this.courseNumber = courseNumber;
        this.course = course;
        this.dateAdded = new Date();
    }

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

    public static RaceCourse[] populateData() {
        return new RaceCourse[] {
                new RaceCourse( "001","&nbsp;<font color='#000000'>Z</font>&nbsp;<font color='#000000'>W</font>&nbsp;<font color='#ff0000'>H</font>"),
                new RaceCourse( "002","&nbsp;<font color='#000000'>A</font>&nbsp;<font color='#000000'>B</font>&nbsp;<font color='#ff0000'>C</font>"),
                new RaceCourse( "003","&nbsp;<font color='#000000'>J</font>&nbsp;<font color='#000000'>H</font>&nbsp;<font color='#ff0000'>K</font>"),
        };
    }
}
