package tomsnuverink.com.workoutapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import tomsnuverink.com.workoutapp.R;
import tomsnuverink.com.workoutapp.model.Exercise;
import tomsnuverink.com.workoutapp.model.Workout;

/**
 * Created by tom on 1-6-2016.
 */
public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    public ExerciseAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ExerciseAdapter(Context context, int resource, List<Exercise> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.exercise_list_item, null);
        }

        Exercise exercise = getItem(position);

        if (exercise != null) {
            TextView name = (TextView) v.findViewById(R.id.exercise_name);
            TextView description = (TextView) v.findViewById(R.id.exercise_description);


            if (name != null) {
                name.setText(exercise.getName());
            }

            if (description != null) {
                description.setText(exercise.getDescription());
            }

            if (!exercise.getImagePath().equals("")) {
                getImage(v, exercise.getImagePath());
            }

        }

        return v;
    }

    private void getImage(View v, String url) {
        ImageView exerciseImage = (ImageView) v.findViewById(R.id.exercise_image);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, exerciseImage);
    }


}
