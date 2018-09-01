package com.jackblaszkowski.repolist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jackblaszkowski.repolist.data.RepoContract;

/**
 * List fragment.
 */
public class RepoListActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = "RepoListFragment";
    public static final String ARG_LOGIN_ID = "login_id";

    private static final int LOADER_ID = 1;

    public static final String[] REPOS_PROJECTION = {
            RepoContract.RepoEntry._ID,
            RepoContract.RepoEntry.COLUMN_NAME,
            RepoContract.RepoEntry.COLUMN_LOGIN,
            RepoContract.RepoEntry.COLUMN_LANGUAGE,
            RepoContract.RepoEntry.COLUMN_STARGAZERS


    };

    // Indices are tied to REPOS_PROJECTION
    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_LOGIN = 2;
    public static final int COL_LANGUAGE = 3;
    public static final int COL_STARGAZERS = 4;


    private ListView mListView;
    private View mRootView;
    private RepoCursorAdapter mCrusorAdapter;

    private String mLoginId;

    public RepoListActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null && getArguments().containsKey(ARG_LOGIN_ID)) {

            mLoginId = getArguments().getString(ARG_LOGIN_ID);


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG, "In onCreateView()");

        mRootView =  inflater.inflate(R.layout.fragment_repo_list, container, false);


        mCrusorAdapter = new RepoCursorAdapter(getActivity(), null, 0);

        mListView = mRootView.findViewById(R.id.repo_list_view);

        mListView.setAdapter(mCrusorAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                         Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                         String loginId = cursor.getString(COL_LOGIN);
                         String repoName = cursor.getString(COL_NAME);

                         Intent intent = new Intent(getActivity(), RepoDetails.class);
                         intent.putExtra(RepoDetailsFragment.ARG_LOGIN_ID, loginId);
                         intent.putExtra(RepoDetailsFragment.ARG_REPO_NAME, repoName);

                         startActivity(intent);

                     }
                 }
        );

        return  mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       getLoaderManager().initLoader(LOADER_ID, null, this);
       super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String sortOrder=null;
        Uri reposUri = RepoContract.RepoEntry.buildReposUri(mLoginId);

        return new CursorLoader(getActivity(),
                reposUri,
                REPOS_PROJECTION,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCrusorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCrusorAdapter.swapCursor(null);

    }
}
