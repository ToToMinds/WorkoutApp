package tomsnuverink.com.workoutapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tomsnuverink.com.workoutapp.adapter.WorkoutAdapter;
import tomsnuverink.com.workoutapp.model.Exercise;
import tomsnuverink.com.workoutapp.model.Workout;
import tomsnuverink.com.workoutapp.service.ExerciseService;
import tomsnuverink.com.workoutapp.service.WorkoutService;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 101;
    private ListView workoutListView;
    private ImageView imageView;

    private CoordinatorLayout coordinatorLayout;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        workoutListView = (ListView) findViewById(R.id.workoutList);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_activity_layout);
        imageView = (ImageView) findViewById(R.id.imageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://workouts.tomsnuverink.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WorkoutService workoutService = retrofit.create(WorkoutService.class);
        ExerciseService exerciseService = retrofit.create(ExerciseService.class);
        Call<List<Workout>> workouts = workoutService.getWorkouts("5Mu8FxGPjeW0bzCPooR87rolWckt7tl6ZQxjE2NAh5F9lq1X0YcM8uRxKsEO");
        workouts.enqueue(new Callback<List<Workout>>() {
            @Override
            public void onResponse(Call<List<Workout>> call, Response<List<Workout>> response) {
                Snackbar.make(coordinatorLayout, "Success", Snackbar.LENGTH_LONG).show();
                setAdapter(response.body());
            }

            @Override
            public void onFailure(Call<List<Workout>> call, Throwable t) {
                Log.v("MAIN_ACTIVITY", t.getLocalizedMessage());
            }
        });

        Call<List<Exercise>> exercises = exerciseService.getExercises("5Mu8FxGPjeW0bzCPooR87rolWckt7tl6ZQxjE2NAh5F9lq1X0YcM8uRxKsEO");
        exercises.enqueue(new Callback<List<Exercise>>() {
            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                Snackbar.make(coordinatorLayout, "Success", Snackbar.LENGTH_LONG).show();
                Log.v("MAIN_ACTIVITY", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable t) {
                Log.v("MAIN_ACTIVITY", t.getLocalizedMessage());
            }
        });

    }

    /**
     * @param workouts
     */
    private void setAdapter(List<Workout> workouts) {
        Log.v("MAIN_ACTIVITY_WORKOUTS", workouts.toString());
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(this, R.id.workoutList, workouts);
        workoutListView.setAdapter(workoutAdapter);
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = imageView;
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
            }
        }
    }

}
