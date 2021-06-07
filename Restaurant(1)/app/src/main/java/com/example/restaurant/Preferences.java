package com.example.restaurant;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Preferences
{
	private static final String PREF = "Restaurants";
	private static final String INDEX = "Index";

	private final SharedPreferences sharedPreferences;

	Preferences(Context context)
	{
		sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
	}

	public synchronized void addRestaurant(Restaurant restaurant)
	{
		int index = sharedPreferences.getInt(INDEX, 0);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(String.valueOf(index), restaurant.toString());
		editor.putInt(INDEX, ++index);
		editor.apply();
	}

	public synchronized ArrayList<Restaurant> readRestaurants()
	{
		ArrayList<Restaurant> notes = new ArrayList<>();

		Map<String, ?> map = sharedPreferences.getAll();

		for (String key : map.keySet())
		{
			if (!key.equals(INDEX))
			{
				notes.add(new Restaurant(Objects.requireNonNull(map.get(key)).toString()));
			}
		}

		return notes;
	}
}