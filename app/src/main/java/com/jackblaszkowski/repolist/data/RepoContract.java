package com.jackblaszkowski.repolist.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class RepoContract {

    //Content paths
    public static final String PATH_USERS = "users";

    //Content Provider constants:
    public static final String CONTENT_AUTHORITY = "com.jackblaszkowski.repolist";
    private static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final Uri CONTENT_URI =
            BASE_URI.buildUpon().appendPath(PATH_USERS).build();


    public static class RepoEntry implements BaseColumns {

        public static final String TABLE_NAME = "repo_table";

        public static final String COLUMN_LOGIN = "login";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_HTML_URL = "html_url";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_STARGAZERS = "stargazers_count";

        public static Uri buildRecordUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //Matches: /users/
        public static Uri buildUsersUri() {
            return CONTENT_URI;
        }

        // Matches: /users/*
        public static Uri buildReposUri(String login) {
            return CONTENT_URI.buildUpon().appendPath(login).build();
        }

        // Matches: /users/*/*
        public static Uri buildDetailsUri(String login, String repoName) {

            return CONTENT_URI.buildUpon().appendPath(login).appendPath(repoName).build();
        }

        public static String getLoginId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getRepoName(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }
}
