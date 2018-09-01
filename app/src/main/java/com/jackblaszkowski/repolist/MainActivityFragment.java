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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.jackblaszkowski.repolist.data.RepoContract;

/**
 * Main fragment.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;

    public static final String[] LOGINS_PROJECTION = {
            RepoContract.RepoEntry._ID,
            RepoContract.RepoEntry.COLUMN_LOGIN,

    };

    // Indices are tied toPROJECTION
    public static final int COL_ID = 0;
    public static final int COL_LOGIN = 1;

    private ListView mListView;
    private CursorAdapter mCrusorAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mCrusorAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[] {RepoContract.RepoEntry.COLUMN_LOGIN},
                new int[] { android.R.id.text1 }, 0);

        mListView = rootView.findViewById(R.id.login_list_view);

        mListView.setAdapter(mCrusorAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                     Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                     String loginId = cursor.getString(COL_LOGIN);

                     Intent intent = new Intent(getActivity(), RepoListActivity.class);
                     intent.putExtra(RepoListActivityFragment.ARG_LOGIN_ID, loginId);

                     startActivity(intent);

                 }
             }
        );


        return rootView;
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
        Uri usersUri = RepoContract.RepoEntry.buildUsersUri();

        return new CursorLoader(getActivity(),
                usersUri,
                LOGINS_PROJECTION,
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
