package com.jackblaszkowski.repolist.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jackblaszkowski.repolist.utils.Utils;

import org.json.JSONException;

import java.io.IOException;

public class RepoProvider extends ContentProvider {


    private static final String LOG_TAG = "RepoProvider";

    // URI Matcher used by this content provider.
    private static final int USERS = 100;
    private static final int REPOS = 200;
    private static final int REPOS_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RepoContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RepoContract.PATH_USERS, USERS);
        matcher.addURI(authority, RepoContract.PATH_USERS + "/*", REPOS);
        matcher.addURI(authority, RepoContract.PATH_USERS + "/*/*", REPOS_WITH_ID);

        return matcher;
    }

    // INSTANCE VARIABLES:
    private GitHubRepoDatabase mGitHubRepoDatabase;

    @Override
    public boolean onCreate() {
        mGitHubRepoDatabase = new GitHubRepoDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;


        switch (sUriMatcher.match(uri)) {
            // "users/"
            case USERS:
            {

                // groupBy: RepoContract.RepoEntry.COLUMN_LOGIN
                // Returns one row per user/company
                cursor = mGitHubRepoDatabase.getReadableDatabase().query(
                        RepoContract.RepoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        RepoContract.RepoEntry.COLUMN_LOGIN,
                        //null,
                        null,
                        sortOrder
                );


                break;
            }
            // "users/*"
            case REPOS: {

                String loginId = RepoContract.RepoEntry.getLoginId(uri);

                if (Utils.isOnline(getContext())) {
                    // 1. Get Repos from API, save in db
                    try {
                        new RepoProviderHelper(getContext()).getRepositoryInfoFromServer(loginId);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }


                } else {
                    Log.w(LOG_TAG, "Device Not Online!");
                }

                // 2. Get all repositories for the selected user from the database

                /*
                selection = RepoContract.RepoEntry.COLUMN_LOGIN + "=?";
                selectionArgs=new String[]{loginId};
                sortOrder = RepoContract.RepoEntry.COLUMN_LANGUAGE + "," + RepoContract.RepoEntry.COLUMN_STARGAZERS + " DESC";

                cursor = mGitHubRepoDatabase.getReadableDatabase().query(
                        RepoContract.RepoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                */

                // Repositories in the List Activity are grouped by language. Languages with the most repositories are listed first.
                // Within each language group, repositories are sorted in descending order by the stargazers count.

                selectionArgs=new String[]{loginId, loginId};
                String query="SELECT rt._ID, rt.NAME, rt.LOGIN, rt.LANGUAGE, rt.stargazers_count, rc.count FROM REPO_TABLE rt  " +
                                "left JOIN (Select language, COUNT(*) as count From repo_table " +
                                    "WHERE LOGIN = ? GROUP BY language) rc " +
                                "on rc.language = rt.language " +
                                "WHERE rt.LOGIN = ? " +
                                "order by count desc, rt.language, stargazers_count desc";

                cursor=mGitHubRepoDatabase.getReadableDatabase().rawQuery(query, selectionArgs);

                //Log.d("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

                break;
            }
            // "users/*/*"
            case REPOS_WITH_ID: {

                Log.d(LOG_TAG, "In query()- REPOS_WITH_ID");

                String loginId = RepoContract.RepoEntry.getLoginId(uri);
                String repoName = RepoContract.RepoEntry.getRepoName(uri);

                selection = RepoContract.RepoEntry.COLUMN_LOGIN + "=?" + " AND " + RepoContract.RepoEntry.COLUMN_NAME + "=?";
                selectionArgs=new String[]{loginId, repoName};

                // Returns repository details
                cursor = mGitHubRepoDatabase.getReadableDatabase().query(
                        RepoContract.RepoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //Register to watch a content URI for changes:
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
        //return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = mGitHubRepoDatabase.getWritableDatabase();
        Uri returnUri;

        long _id = db.insert(RepoContract.RepoEntry.TABLE_NAME, null, contentValues);
        if ( _id > 0 )
            returnUri = RepoContract.RepoEntry.buildRecordUri(_id);
        else
            //throw new android.database.SQLException("Failed to insert row into " + uri);
            returnUri=null;

        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowsDeleted = mGitHubRepoDatabase.getWritableDatabase().delete(
                RepoContract.RepoEntry.TABLE_NAME,
                selection,
                selectionArgs

        );

        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
