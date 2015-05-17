package com.example.android.googlejamfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AlertListActivity extends ActionBarActivity implements AlertListFragment.OnAlertSelectedListener {

    boolean mDualPane = false;

    public void onAlertSelected(long id) {
        if (mDualPane) {
            // do two pane stuff
            DetailMapFragment newFragment = new DetailMapFragment();
            Bundle args = new Bundle();
            args.putLong("id", id);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_right, newFragment);
//            transaction.addToBackStack(null);
            transaction.commit();

        } else {
            //do one pane stuff
            Intent intent = new Intent();
            intent.setClass(this, MapActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if(findViewById(R.id.fragment_left) != null) {
            mDualPane = true;
        }

        if (savedInstanceState == null) {
            if (mDualPane) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_left, new AlertListFragment())
                        .commit();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_right, new DetailMapFragment())
                        .commit();

            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_list, new AlertListFragment())
                        .commit();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportFragmentManager().popBackStackImmediate();
        return super.onSupportNavigateUp();

    }

}
