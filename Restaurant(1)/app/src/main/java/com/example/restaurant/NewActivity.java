package com.example.restaurant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class NewActivity extends AppCompatActivity
{
	private EditText name;
	private EditText location;
	private Restaurant restaurant;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new);

		if (!forcedPermissionGrant())
		{
			System.exit(-1);
		}

		name = findViewById(R.id.name);
		location = findViewById(R.id.location);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		Places.initialize(getApplicationContext(), getResources().getString(R.string.Auth));

		AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete);
		assert autocompleteFragment != null;
		autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
		{
			@Override
			public void onPlaceSelected(@NonNull Place place)
			{
				restaurant = new Restaurant(place);
				name.setText(restaurant.name);
				location.setText(restaurant.address);
			}

			@Override
			public void onError(@NonNull Status status)
			{

			}
		});
	}

	public void onCurrent(View view)
	{
		Places.initialize(getApplicationContext(), getResources().getString(R.string.Auth));
		PlacesClient placesClient = Places.createClient(this);

		FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
		@SuppressLint("MissingPermission") Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
		placeResponse.addOnCompleteListener(task ->
		{
			if (task.isSuccessful())
			{
				FindCurrentPlaceResponse response = task.getResult();
				if (response.getPlaceLikelihoods().size() > 0)
				{
					restaurant = new Restaurant(response.getPlaceLikelihoods().get(0).getPlace());
					name.setText(response.getPlaceLikelihoods().get(0).getPlace().getName());
					location.setText(response.getPlaceLikelihoods().get(0).getPlace().getAddress());
				}
			}
		});
	}

	public void onShow(View view)
	{
		if (restaurant != null)
		{
			Intent intent = new Intent(this, MapsActivity.class);
			intent.putExtra("Restaurant", restaurant);
			startActivity(intent);
		}
	}

	public void onSave(View view)
	{
		if (restaurant != null && !name.getText().toString().isEmpty())
		{
			restaurant.name = name.getText().toString();
			restaurant.address = location.getText().toString();

			Preferences preferences = new Preferences(this);
			preferences.addRestaurant(restaurant);

			startActivity(new Intent(this, MainActivity.class));
		}
	}

	public boolean forcedPermissionGrant()
	{
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
		}

		return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}
}