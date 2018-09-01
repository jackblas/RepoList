package com.jackblaszkowski.repolist.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.jackblaszkowski.repolist.remote.GitHubApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class RepoProviderHelper {

    private static final String LOG_TAG = "RepoProviderHelper";
    private Context mContext;

    public RepoProviderHelper(Context context){

        mContext=context;

    }

    // Call API
    // If success, save to db
    void getRepositoryInfoFromServer(String loginId) throws IOException, JSONException {
        Log.d(LOG_TAG, "queryForRepos()");

            JSONArray jsonArray = getArrayListFromApi(loginId);

            if(jsonArray != null) {
                // Delete old data
                int rowsDeleted = mContext.getContentResolver().delete(
                        RepoContract.CONTENT_URI,
                        RepoContract.RepoEntry.COLUMN_LOGIN + "=?",
                        new String[]{loginId});

                // Insert fresh data
                for (int i = 0; i < jsonArray.length(); i++) {

                    ContentValues contentValues = new ContentValues();
                    JSONObject object = jsonArray.getJSONObject(i);

                    contentValues.put(RepoContract.RepoEntry.COLUMN_LOGIN, loginId);
                    contentValues.put(RepoContract.RepoEntry.COLUMN_NAME, object.getString(GitHubApi.JSON_REPO_NAME));
                    contentValues.put(RepoContract.RepoEntry.COLUMN_HTML_URL, object.getString(GitHubApi.JSON_HTML_URL));
                    contentValues.put(RepoContract.RepoEntry.COLUMN_DESCRIPTION, object.getString(GitHubApi.JSON_DESCRIPTION));
                    contentValues.put(RepoContract.RepoEntry.COLUMN_LANGUAGE, object.getString(GitHubApi.JSON_LANGUAGE));
                    contentValues.put(RepoContract.RepoEntry.COLUMN_STARGAZERS, object.getString(GitHubApi.JSON_STARGAZERS));

                    Uri insertedImageUri = mContext.getContentResolver().insert(
                            RepoContract.CONTENT_URI,
                            contentValues);

                }
            }

    }

    private JSONArray getArrayListFromApi(String id) throws IOException {

        JSONArray list = null;
        URL url = new URL(GitHubApi.BASE_URL + id + GitHubApi.REPOS_PATH);

        list = GitHubApi.fetchArrayList(url);

        return list;
    }


}
