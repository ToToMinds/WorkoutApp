package tomsnuverink.com.workoutapp.service;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tomsnuverink.com.workoutapp.model.Exercise;

/**
 * Created by tom on 1-6-2016.
 */
public interface ExerciseService {

    @GET("exercises")
    Call<List<Exercise>> all(@Query("api_token") String apiToken);

    @GET("exercises/{id}")
    Call<Exercise> get(@Path("id") long id, @Query("api_token") String apiToken);

    @Multipart
    @POST("exercises")
    Call<Exercise> create(@Part("name") String name, @Part("description") String description, @Part MultipartBody.Part file, @Query("api_token") String apiToken);

    @Multipart
    @PUT("/exercises/{id}")
    Call<Exercise> update(@Path("id") long id, @Part("name") String name, @Part("description") String description, @Part MultipartBody.Part file, @Query("api_token") String apiToken);

    @DELETE("exercises/{id}")
    Call<ResponseBody> delete(@Path("id") long id, @Query("api_token") String apiToken);

}
