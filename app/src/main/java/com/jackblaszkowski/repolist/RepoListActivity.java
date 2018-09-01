package com.jackblaszkowski.repolist;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class RepoListActivity extends AppCompatActivity {

    private String mLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Add a fragment:
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            mLoginId=getIntent().getStringExtra(RepoListActivityFragment.ARG_LOGIN_ID);
            arguments.putString(RepoListActivityFragment.ARG_LOGIN_ID, mLoginId);

            RepoListActivityFragment fragment = new RepoListActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment, fragment)
                    .commit();
        }




    }


}
