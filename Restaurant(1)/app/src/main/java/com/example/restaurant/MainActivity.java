package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onNew(View view)
	{
		startActivity(new Intent(this, NewActivity.class));
	}

	public void onShow(View view)
	{
		startActivity(new Intent(this, MapsActivity.class));
	}
}