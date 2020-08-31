package io.square1.richtextlib;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebRequestHelper extends AsyncTask<String, Void, String> {
    private EmbedUtils.ThumbnailUrlCallback callback;

    public WebRequestHelper(EmbedUtils.ThumbnailUrlCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        try {
            URL getUrl = new URL(strings[0]);
            urlConnection = (HttpURLConnection) getUrl.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:74.0) Gecko/20100101 Firefox/74.0");
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder sbStreamMap = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sbStreamMap.append(line);
            }
            return sbStreamMap.toString();
        } catch (IOException ignored) { } finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException ignored) { }
            urlConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.callback.onReceived(s);
    }
}
