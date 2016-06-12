package tomsnuverink.com.workoutapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tomsnuverink.com.workoutapp.R;
import tomsnuverink.com.workoutapp.adapter.WorkoutAdapter;
import tomsnuverink.com.workoutapp.helper.RetrofitHelper;
import tomsnuverink.com.workoutapp.model.Exercise;
import tomsnuverink.com.workoutapp.model.Workout;
import tomsnuverink.com.workoutapp.service.WorkoutService;


public class WorkoutFragment extends Fragment {

    private ListView workoutListView;
    private RetrofitHelper retrofitHelper;
    private WorkoutService workoutService;

    /**
     * Constructor
     */
    public WorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitHelper = new RetrofitHelper();
        workoutService = (WorkoutService) retrofitHelper.build(WorkoutService.class);
        refreshWorkouts();
    }

    private void removeWorkout(Workout workout) {
        Call<ResponseBody> call = workoutService.delete(workout.getId(), RetrofitHelper.TEST_TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getContext(), "Workout deleted!", Toast.LENGTH_SHORT).show();
                refreshWorkouts();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("EXERCISE_DELETED_FAILED", t.getLocalizedMessage() + "");
                Toast.makeText(getContext(), "Workout isn't deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     */
    private void refreshWorkouts() {
        Call<List<Workout>> workouts = workoutService.all(RetrofitHelper.TEST_TOKEN);
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

    /**
     * @param workouts
     */
    private void setAdapter(List<Workout> workouts) {
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), R.id.workoutListView, workouts);
        workoutListView.setAdapter(workoutAdapter);
        workoutAdapter.notifyDataSetChanged();
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        workoutListView = (ListView) view.findViewById(R.id.workoutListView);


        workoutListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Workout workout = (Workout) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to delete this workout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeWorkout(workout);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return false;
            }
        });

        return view;
    }

}
