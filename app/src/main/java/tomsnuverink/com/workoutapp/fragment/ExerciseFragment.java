package tomsnuverink.com.workoutapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tomsnuverink.com.workoutapp.R;
import tomsnuverink.com.workoutapp.activity.AddExerciseActivity;
import tomsnuverink.com.workoutapp.adapter.ExerciseAdapter;
import tomsnuverink.com.workoutapp.helper.RetrofitHelper;
import tomsnuverink.com.workoutapp.model.Exercise;
import tomsnuverink.com.workoutapp.service.ExerciseService;


public class ExerciseFragment extends Fragment {

    private ListView exerciseListView;
    private ImageView exerciseImage;
    private FloatingActionButton floatingActionButton;

    private EditText exerciseName;
    private EditText exerciseDescription;

    private ExerciseService exerciseService;
    private RetrofitHelper retrofitHelper;

    public static final int ADD_EXERCISE = 101;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitHelper = new RetrofitHelper();
        exerciseService = (ExerciseService) retrofitHelper.build(ExerciseService.class);
        refreshExercises();
    }

    /**
     * Refresh the exercises list
     */
    private void refreshExercises() {
        Call<List<Exercise>> exercises = exerciseService.all(RetrofitHelper.TEST_TOKEN);
        exercises.enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                Log.v("MAIN_ACTIVITY", response.body().toString());
                setAdapter(response.body());
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) {
                Log.v("MAIN_ACTIVITY", t.getLocalizedMessage());
            }
        });
    }

    private void setAdapter(List<Exercise> exercises) {
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(getContext(), R.id.exerciseListView, exercises);
        exerciseListView.setAdapter(exerciseAdapter);
        exerciseAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        exerciseListView = (ListView) view.findViewById(R.id.exerciseListView);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_exercise);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newExercise(v);
            }
        });

        exerciseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Exercise exercise = (Exercise) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to delete exercise: " + exercise.getName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeExercise(exercise);
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

    private void removeExercise(final Exercise exercise) {
        Call<ResponseBody> call = exerciseService.delete(exercise.getId(), RetrofitHelper.TEST_TOKEN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getContext(), exercise.getName() + " deleted!", Toast.LENGTH_SHORT).show();
                refreshExercises();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("EXERCISE_DELETED_FAILED", t.getLocalizedMessage() + "");
                Toast.makeText(getContext(), exercise.getName() + " isn't deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Start new add exercise activity
     *
     * @param view
     */
    public void newExercise(View view) {
        Intent intent = new Intent(getContext(), AddExerciseActivity.class);
        startActivityForResult(intent, ADD_EXERCISE);
    }


    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ExerciseFragment.ADD_EXERCISE:
                    if (data.getBooleanExtra("successful", false)) {
                        refreshExercises();
                    }
                    break;
            }
        }
    }

}
