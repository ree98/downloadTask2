package com.example.resan.downloadtask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog mProgDialog;
    private Bitmap mBitmap;
    private Button mButton;
    private ImageView imageView;
    private HttpURLConnection mHttpConn;
    private ContactAdapter contactAdapter;
    ArrayList<Contact> contacts;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.myRV);

        mButton = findViewById(R.id.btnDL);
        imageView = findViewById(R.id.ivThumbnail);
        contacts = new ArrayList<Contact>();
        // contactAdapter = new ContactAdapter(contacts, MainActivity.this);
        //recyclerView.setAdapter(contactAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkAvailability("http://192.168.43.46/JSONexampleBrg/index.php");
            }
        });
    }

    private void checkNetworkAvailability(String urlStr){

        ConnectivityManager connMngr = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connMngr.getActiveNetworkInfo();

        if (mNetworkInfo!=null && mNetworkInfo.isConnected()){
            new DownloadImageTask().execute(urlStr);
            Toast.makeText(getApplicationContext(),"Konek",Toast.LENGTH_LONG).show();
        }else if (mNetworkInfo==null) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No internet connection available",
                    Toast.LENGTH_SHORT);

            toast.show();
        }
    }

    public class DownloadImageTask extends AsyncTask<String ,Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgDialog = new ProgressDialog(MainActivity.this);
            mProgDialog.setMessage("Please Wait");
            // mProgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            InputStream inputStream = null;
            String konten = null;

            try {
                StringBuilder stringBuilder =new StringBuilder();
                inputStream = openHttpConnection(url);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String nextline ;

                while ((nextline= bufferedReader.readLine())!= null)
                {
                    stringBuilder.append(nextline);
                }

                konten = stringBuilder.toString();

                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return konten;
        }

        private InputStream openHttpConnection(String str){
            InputStream inputStream = null;
            int resCode = -1;

            try {
                URL mUrl = new URL(str);
                mHttpConn = (HttpURLConnection) mUrl.openConnection();
                mHttpConn.setAllowUserInteraction(false);
                mHttpConn.setInstanceFollowRedirects(true);
                mHttpConn.setReadTimeout(10000);
                mHttpConn.setConnectTimeout(15000);
                mHttpConn.setRequestMethod("GET");
                mHttpConn.connect();
                resCode = mHttpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK){
                    inputStream = mHttpConn.getInputStream();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Contact contact = new Contact(jsonObject.getString("name"),jsonObject.getString("stock"),
                            jsonObject.getString("price"), jsonObject.getString("image"));
                    contacts.add(contact);
                }
                recyclerView = findViewById(R.id.myRV);
                contactAdapter = new ContactAdapter(contacts, MainActivity.this);
                recyclerView.setAdapter(contactAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                contactAdapter.notifyDataSetChanged();

            }catch (JSONException e) {
                e.printStackTrace();
            }

            mHttpConn.disconnect();
            mProgDialog.dismiss();
        }
    }
}
