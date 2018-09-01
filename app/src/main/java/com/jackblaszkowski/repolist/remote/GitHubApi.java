package com.jackblaszkowski.repolist.remote;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubApi {

    private static final String LOG_TAG = "GitHubApi";
    public static final String BASE_URL = "https://api.github.com/users/";
    public static final String REPOS_PATH = "/repos";

    //JSON keys:
    public static final String JSON_LOGIN_ID = "login";
    public static final String JSON_REPO_NAME = "name";
    public static final String JSON_HTML_URL = "html_url";
    public static final String JSON_DESCRIPTION = "description";
    public static final String JSON_LANGUAGE = "language";
    public static final String JSON_STARGAZERS = "stargazers_count";

    public static final String JSON_OWNER = "owner";

    public static String fetchPlainText(URL url) {

        String jsonString=null;


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            // The raw JSON response as a string.
            jsonString = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream.", e);
                }
            }
        }
        //If error  getting or parsing data:
        //return null;

        Log.d(LOG_TAG, "Json String: " + jsonString);

        return jsonString;
    }

    public static JSONArray fetchArrayList(URL url) throws IOException {

        JSONArray jsonArray=null;

        String jsonString = fetchPlainText(url);

        if(jsonString != null) {
            // Parse
            JSONTokener tokener = new JSONTokener(jsonString);
            try {
                Object obj = tokener.nextValue();
                if (obj instanceof JSONArray) {

                    jsonArray = new JSONArray(jsonString);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;
    }
}
