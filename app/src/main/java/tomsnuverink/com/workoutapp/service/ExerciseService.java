package tomsnuverink.com.workoutapp.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tomsnuverink.com.workoutapp.model.Exercise;

/**
 * Created by tom on 1-6-2016.
 */
public interface ExerciseService {

    @GET("exercises")
    Call<List<Exercise>> getExercises(@Query("api_token") String apiToken);


}
