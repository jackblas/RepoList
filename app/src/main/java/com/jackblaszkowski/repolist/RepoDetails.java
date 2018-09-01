package com.jackblaszkowski.repolist;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class RepoDetails extends AppCompatActivity {

    private String mLoginId;
    private String mRepoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            mLoginId=getIntent().getStringExtra(RepoDetailsFragment.ARG_LOGIN_ID);
            mRepoName=getIntent().getStringExtra(RepoDetailsFragment.ARG_REPO_NAME);

            arguments.putString(RepoDetailsFragment.ARG_LOGIN_ID, mLoginId);
            arguments.putString(RepoDetailsFragment.ARG_REPO_NAME, mRepoName);

            RepoDetailsFragment fragment = new RepoDetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment, fragment)
                    .commit();
        }
    }

}
