package tomsnuverink.com.workoutapp.service;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tomsnuverink.com.workoutapp.model.Workout;

/**
 * Created by tom on 30-5-2016.
 */
public interface WorkoutService {

    @GET("workouts")
    Call<List<Workout>> getWorkouts(@Query("api_token") String apiToken);

}
