package tomsnuverink.com.workoutapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tomsnuverink.com.workoutapp.R;
import tomsnuverink.com.workoutapp.model.Workout;

/**
 * Created by tom on 1-6-2016.
 */
public class WorkoutAdapter extends ArrayAdapter<Workout> {

    public WorkoutAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public WorkoutAdapter(Context context, int resource, List<Workout> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.workout_list_item, null);
        }

        Workout workout = getItem(position);

        if (workout != null) {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView dates = (TextView) v.findViewById(R.id.dates);

            if (title != null) {
                title.setText(workout.getId() + "");
            }

            if (dates != null) {
                dates.setText(workout.getStartDate().toString() + " - " + workout.getEndDate().toString());
            }
        }

        return v;
    }

}
