package tomsnuverink.com.workoutapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tomsnuverink.com.workoutapp.R;
import tomsnuverink.com.workoutapp.fragment.ExerciseFragment;
import tomsnuverink.com.workoutapp.helper.RetrofitHelper;
import tomsnuverink.com.workoutapp.model.Exercise;
import tomsnuverink.com.workoutapp.service.ExerciseService;

public class ExerciseActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_REQUEST_CODE = 300;

    private EditText exerciseNameEditText;
    private EditText exerciseDescriptionEditText;
    private ImageView imageView;

    private Button takePictureButton;
    private Button saveExerciseButton;
    private File file;

    private Exercise savedExercise;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        exerciseNameEditText = (EditText) findViewById(R.id.add_exercise_name);
        exerciseDescriptionEditText = (EditText) findViewById(R.id.add_exercise_description);
        takePictureButton = (Button) findViewById(R.id.take_picture_button);
        saveExerciseButton = (Button) findViewById(R.id.save_exercise_button);
        imageView = (ImageView) findViewById(R.id.captured_image);

        requestCode = getIntent().getExtras().getInt("requestCode", ExerciseFragment.ADD_EXERCISE);
        if (requestCode == ExerciseFragment.UPDATE_EXERCISE) {
            savedExercise = (Exercise) getIntent().getExtras().getSerializable("exercise");
            if (savedExercise != null) {
                fillFields(savedExercise);
            }
        }
    }

    private void fillFields(Exercise exercise) {
        exerciseNameEditText.setText(exercise.getName());
        exerciseDescriptionEditText.setText(exercise.getDescription());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(exercise.getImagePath(), imageView);
    }

    /**
     * Send request to create exercise
     *
     * @param view
     */
    public void sendRequest(View view) {
        if (requestCode == ExerciseFragment.UPDATE_EXERCISE) {
            doUpdate();
        } else {
            doInsert();
        }
    }

    private void doInsert() {
        String name = exerciseNameEditText.getText().toString();
        String description = exerciseDescriptionEditText.getText().toString();

        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setDescription(description);

        RetrofitHelper retrofitHelper = new RetrofitHelper();
        ExerciseService exerciseService = (ExerciseService) retrofitHelper.build(ExerciseService.class);

        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // MultipartBody.Part is used to send also the actual file name
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        Call<Exercise> exerciseCall = exerciseService.create(exercise.getName(), exercise.getDescription(), image, "5Mu8FxGPjeW0bzCPooR87rolWckt7tl6ZQxjE2NAh5F9lq1X0YcM8uRxKsEO");
        exerciseCall.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ExerciseActivity.this, "Exercise saved", Toast.LENGTH_SHORT).show();
                    closeActivity(true);
                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                Log.v("SAVE_EXERCISE", t.getLocalizedMessage() + "");
                Toast.makeText(ExerciseActivity.this, "Exercise not saved", Toast.LENGTH_SHORT).show();
                closeActivity(false);
            }
        });
    }

    private void doUpdate() {
        String name = exerciseNameEditText.getText().toString();
        String description = exerciseDescriptionEditText.getText().toString();

        Exercise exercise = savedExercise;
        exercise.setName(name);
        exercise.setDescription(description);

        RetrofitHelper retrofitHelper = new RetrofitHelper();
        ExerciseService exerciseService = (ExerciseService) retrofitHelper.build(ExerciseService.class);

        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // MultipartBody.Part is used to send also the actual file name
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        Call<Exercise> exerciseCall = exerciseService.update(exercise.getId(), exercise.getName(), exercise.getDescription(), image, "5Mu8FxGPjeW0bzCPooR87rolWckt7tl6ZQxjE2NAh5F9lq1X0YcM8uRxKsEO");
        exerciseCall.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ExerciseActivity.this, "Exercise updated", Toast.LENGTH_SHORT).show();
                    closeActivity(true);
                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                Log.v("SAVE_EXERCISE", t.getLocalizedMessage() + "");
                Toast.makeText(ExerciseActivity.this, "Exercise not updated", Toast.LENGTH_SHORT).show();
                closeActivity(false);
            }
        });
    }

    /**
     * Start the camera
     */
    public void startCamera(View view) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
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
                case CAPTURE_IMAGE_REQUEST_CODE:
                    handleImage(data);
                    break;
            }
        }
    }

    private void handleImage(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        // Get the cursor
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);

        file = new File(imgDecodableString);

        cursor.close();
        takePictureButton.setText("Change picture");

        // Set the Image in ImageView after decoding the String
        imageView.setImageBitmap(BitmapFactory
                .decodeFile(imgDecodableString));
    }

    /**
     * @param successful
     */
    private void closeActivity(boolean successful) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("successful", successful);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
