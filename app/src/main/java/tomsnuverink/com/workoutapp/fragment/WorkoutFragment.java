package tomsnuverink.com.workoutapp.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tomsnuverink.com.workoutapp.R;
import tomsnuverink.com.workoutapp.adapter.WorkoutAdapter;
import tomsnuverink.com.workoutapp.helper.RetrofitHelper;
import tomsnuverink.com.workoutapp.model.Workout;
import tomsnuverink.com.workoutapp.service.WorkoutService;


public class WorkoutFragment extends Fragment {

    private ListView workoutListView;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void refreshWorkouts() {
        RetrofitHelper retrofitHelper = new RetrofitHelper();
        WorkoutService workoutService = (WorkoutService) retrofitHelper.build(WorkoutService.class);
        Call<List<Workout>> workouts = workoutService.getWorkouts("5Mu8FxGPjeW0bzCPooR87rolWckt7tl6ZQxjE2NAh5F9lq1X0YcM8uRxKsEO");
        workouts.enqueue(new Callback<List<Workout>>() {
            @Override
            public void onResponse(Call<List<Workout>> call, Response<List<Workout>> response) {
                setAdapter(response.body());
            }

            @Override
            public void onFailure(Call<List<Workout>> call, Throwable t) {
                Log.v("MAIN_ACTIVITY", t.getLocalizedMessage());
            }
        });
    }

    private void setAdapter(List<Workout> workouts) {
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), R.id.workoutListView, workouts);
        workoutListView.setAdapter(workoutAdapter);
        workoutAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        workoutListView = (ListView) view.findViewById(R.id.workoutListView);


        return view;
    }

}
