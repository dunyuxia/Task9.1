package com.example.restaurant;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;

public class Restaurant implements Serializable
{
	String name;
	String address;
	final double latitude;
	final double longitude;

	Restaurant(String descriptor)
	{
		String[] split = descriptor.split("\\|");
		name = split[0];
		address = split[1];
		latitude = Double.parseDouble(split[2]);
		longitude = Double.parseDouble(split[3]);
	}

	Restaurant(Place place)
	{
		assert place.getLatLng() != null;
		name = place.getName();
		address = place.getAddress();
		latitude = place.getLatLng().latitude;
		longitude = place.getLatLng().longitude;
	}

	LatLng getLatLng()
	{
		return new LatLng(latitude, longitude);
	}

	@Override
	public String toString()
	{
		return name + "|" + address + "|" + latitude + "|" + longitude;
	}
}