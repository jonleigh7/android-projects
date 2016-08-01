package com.vectorexa.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;

public class MainActivity extends AppCompatActivity {

    private TextView xmlData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        xmlData = (TextView) findViewById(R.id.downloadedData);

        DownloadData download = new DownloadData();
        download.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //CODE THAT I ADDED

    private class DownloadData extends AsyncTask<String, Void, String> {

        private String mFileContents;

        @Override
        protected String doInBackground(String... strings) { //the "..." represents multiple or variable String parameters.
            mFileContents=downloadXMLFile(strings[0]); //call our downloadXMLFile method.
            if(mFileContents==null){
                Log.d("DownloadData", "Error Downloading"); //print to the console that there is a download error.
            }
            return mFileContents;
        }

        @Override
        protected void onPostExecute(String s) { // THIS METHOD IS DESIGNED TO UPDATE OR SHOW DATA ON THE SCREEN IN ASYNCTASK.
            super.onPostExecute(s);
            xmlData.setText(mFileContents);
            Log.d("DownloadData", "Result was "+ s); //say this to the console .
        }

        private String downloadXMLFile (String urlPath){ //method that actually downloads text file form the internet.
            StringBuilder buffer = new StringBuilder();
            try{
                URL xmlUrl = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) xmlUrl.openConnection(); //open a HTTPURLconnection by casting our URLConnection, we made from our URL path.
                int response = connection.getResponseCode();
                Log.d("DownloadData", "Response Code : " + response); //print response code to console

                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is); //appropriate code to download the data and put it into the buffer to read from the URL.
                //read one character at a time.
                int charRead;
                char[] r = new char[500];
                while(true){
                    charRead=reader.read(r); //inputStreamReader reads data into r[] which is a char array. returns the amount of characters read as an integer.
                    if(charRead<=0){ //if it hits the end, break the while loop and end the method.
                        break;
                    }
                    buffer.append(String.copyValueOf(r,0,charRead)); //convert char[] into a String before appending it to the buffer. the args after "r" are the indexes. starts from 0 and ends at charRead.
                }
                return buffer.toString();

            }catch(IOException e){
                Log.d("DownloadData", "IO Exception: " + e.getMessage());
                e.printStackTrace(); //prints to the console the line numbers where the exception messed up.
            }catch(SecurityException e){
                Log.d("DownloadData", "Security Exception,  Something wrong with permissions? : " + e.getMessage());
            }
            return null;
        }
    }
}
