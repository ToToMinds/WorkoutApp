package tomsnuverink.com.workoutapp.service;


import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tomsnuverink.com.workoutapp.model.Workout;

/**
 * Created by tom on 30-5-2016.
 */
public interface WorkoutService {

    @GET("workouts")
    Call<List<Workout>> all(@Query("api_token") String apiToken);

    @GET("workouts/{id}")
    Call<List<Workout>> get(@Path("id") long id, @Query("api_token") String apiToken);

    @POST("workouts")
    Call<Workout> create(@Body Workout workout, @Query("api_token") String apiToken);

    @PUT("workouts/{id}")
    Call<Workout> update(@Body Workout workout, @Query("api_token") String apiToken);

    @DELETE("workouts/{id}")
    Call<ResponseBody> delete(@Path("id") long id, @Query("api_token") String apiToken);

}
