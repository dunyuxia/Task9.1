package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{
	private ArrayList<Restaurant> restaurants;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		restaurants = new ArrayList<>();

		Intent intent = getIntent();
		Restaurant item = (Restaurant) intent.getSerializableExtra("Restaurant");

		if (item == null)
		{
			Preferences preferences = new Preferences(this);
			restaurants = preferences.readRestaurants();
		}
		else
		{
			restaurants.add(item);
		}

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		assert mapFragment != null;
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(@NonNull GoogleMap googleMap)
	{
		for (int i = 0; i < restaurants.size(); i++)
		{
			googleMap.addMarker(new MarkerOptions().position(restaurants.get(i).getLatLng()).title(restaurants.get(i).name));
		}

		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-37.8503317, 145.1128872), 10));
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		startActivity(new Intent(this, MainActivity.class));
	}
}