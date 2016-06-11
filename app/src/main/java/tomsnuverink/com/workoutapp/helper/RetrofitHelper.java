package tomsnuverink.com.workoutapp.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tom on 11-6-2016.
 */
public class RetrofitHelper {

    private Retrofit retrofit;
    private final String BASE_URL = "http://workouts.tomsnuverink.com/api/";

    /**
     * Constructor
     */
    public RetrofitHelper() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**
     * @param service
     * @return Object
     */
    public Object build(Class service) {
        return retrofit.create(service);
    }

}
