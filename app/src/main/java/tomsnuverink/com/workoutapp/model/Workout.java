package tomsnuverink.com.workoutapp.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by tom on 30-5-2016.
 */
public class Workout implements Serializable {

    private long id;
    private User user;
    private String date;
    @SerializedName("start_date")
    private Date startDate;
    @SerializedName("end_date")
    private Date endDate;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    private List<Exercise> exercises;
    private float latitude;
    private float longitude;

    public Workout(long id, User user, String date, Date startDate, Date endDate, Date createdAt, Date updatedAt, List<Exercise> exercises, float latitude, float longitude) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.exercises = exercises;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", user=" + user +
                ", date='" + date + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", exercises=" + exercises +
                '}';
    }
}
