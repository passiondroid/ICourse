package com.icloud.cronin.peter.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Arif Khan on 21/10/18.
 */

@Entity(tableName = "RACE_LOCATION", indices = {@Index(value = "markerCharacter", unique = true),
        @Index(value = "locationName", unique = true)})
public class RaceLocation {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String locationName;

    @NonNull
    private String latitude;

    @NonNull
    private String longitude;

    @NonNull
    private String markerCharacter;

    public RaceLocation(String locationName, String latitude, String longitude, String markerCharacter) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerCharacter = markerCharacter;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(@NonNull String locationName) {
        this.locationName = locationName;
    }

    @NonNull
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull String latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull String longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public String getMarkerCharacter() {
        return markerCharacter;
    }

    public void setMarkerCharacter(@NonNull String markerCharacter) {
        this.markerCharacter = markerCharacter;
    }

    @Override
    public String toString() {
        return "RaceLocation{" +
                ", markerCharacter='" + markerCharacter + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof RaceLocation)){
            return false;
        }

        RaceLocation raceLocation = (RaceLocation)obj;
        return (raceLocation.markerCharacter.equals(this.markerCharacter));
    }

    @Override
    public int hashCode() {
        return markerCharacter.hashCode() ^ latitude.hashCode();
    }

    public static RaceLocation[] populateData() {
        return new RaceLocation[] {
                new RaceLocation( "APEX","53.44611111", "-6.05444444", "A"),
                new RaceLocation( "BALSCADDEN","53.38833333", "-6.05444444", "B"),
                new RaceLocation( "CUSH","53.41027778", "-6.09083333", "C"),
                new RaceLocation( "DUNBO","53.41222222", "-6.06416667", "D"),
                new RaceLocation( "EAST","53.43333333", "-6.03666667", "E"),
                new RaceLocation( "GARBH","53.41666667", "-6.03861111", "G"),
                new RaceLocation( "HUB","53.42861111", "-6.0738889", "H"),
                new RaceLocation( "ISLAND","53.41166667", "-6.07277778", "I"),
                new RaceLocation( "THULLA","53.395176", "-6.059380", "J"),
                new RaceLocation( "STACK","53.41000000", "-6.05194444", "K"),
                new RaceLocation( "MALAHIDE","53.45388889", "-6.09305556", "M"),
                new RaceLocation( "NORTH","53.466338889", "-6.05666667", "N"),
                new RaceLocation( "OSPREY","53.42361111", "-6.05861111", "O"),
                new RaceLocation( "PORTMARNOCK","53.42722222", "-6.09666667", "P"),
                new RaceLocation( "ROWAN_ROCKS","53.397893", "-6.055039", "Q"),
                new RaceLocation( "SOUTH_ROWAN","53.396225", "-6.065425", "R"),
                new RaceLocation( "SPIT","53.40583333", "-6.07472222", "S"),
                new RaceLocation( "TABLOT","53.45527778", "-6.02166667", "T"),
                new RaceLocation( "ULYSSES","53.43694444", "-6.08277778", "U"),
                new RaceLocation( "VICEROY","53.41861111", "-6.07972222", "V"),
                new RaceLocation( "WEST","53.41611111", "-6.10111111", "W"),
                new RaceLocation( "XEBEC","53.40194444", "-6.07222222", "X")
        };
    }


}
