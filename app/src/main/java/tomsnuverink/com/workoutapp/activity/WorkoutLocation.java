package tomsnuverink.com.workoutapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import tomsnuverink.com.workoutapp.R;
import tomsnuverink.com.workoutapp.model.Workout;

public class WorkoutLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent thisIntent = getIntent();
        ArrayList<Workout> workouts = (ArrayList<Workout>) thisIntent.getSerializableExtra("Workouts");
        LatLng coords = new LatLng(0, 0);
        for (int i = 0; i < workouts.size(); i++)
        {
            Workout wOut = workouts.get(i);
            coords = new LatLng(wOut.getLatitude(), wOut.getLongitude());
            mMap.addMarker(new MarkerOptions().position(coords));
        }

        int position = thisIntent.getIntExtra("Position", -1);
        coords = new LatLng(workouts.get(position).getLatitude(), workouts.get(position).getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
    }
}
