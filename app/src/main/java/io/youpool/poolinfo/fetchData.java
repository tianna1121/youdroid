package io.youpool.poolinfo;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import android.text.format.DateFormat;
import android.util.Log;

public class fetchData extends AsyncTask<Void, Void, Void> {

    public static final String TAG = "tianna1121";
    String data = "";
    String pool = "";
    String nblk = "";
    String pblk = "";


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.data.setText(this.pool);
        MainActivity.nblk.setText(this.nblk);
        MainActivity.pblk.setText(this.pblk);
    }


    @Override
    protected Void doInBackground(Void... voids) {

        try {
            URL url = new URL("http://tyche.youpool.io:8111/stats");
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                inputStream = httpURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while(line != null){
                try {
                    line = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                data = data + line;
            }

            try {
//                JSONArray JA = new JSONArray(data);

//                Log.d(TAG, "JA.length = " + JA.length());
//                for(int i = 0; i < JA.length(); i++){
//                    JSONObject JO = (JSONObject) JA.get(i);
//                    singleParsed = "poolHost:" +  JO.get("config") + "\n";
//                }
//                dataParsed = dataParsed + singleParsed;
                JSONObject JO = new JSONObject(data);
                // get the pool name from config-poolHost
                pool = (JO.getJSONObject("config").getString("poolHost")).split("\\.")[0];
                // get the last block timestamp
                nblk = Long.toString((System.currentTimeMillis() - Long.parseLong(JO.getJSONObject("lastblock").getString("timestamp"))*1000)/1000);
                Log.d(TAG, "doInBackground:");
                pblk = Long.toString(System.currentTimeMillis() - Long.parseLong(JO.getJSONObject("pool").getJSONObject("stats").getString("lastBlockFound"))/1000);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
