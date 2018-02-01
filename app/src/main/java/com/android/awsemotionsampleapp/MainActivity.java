package com.android.awsemotionsampleapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amazonaws.regions.Regions;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.Image;


import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {


    AmazonRekognitionClient amazonRekognitionClient;
    Image getAmazonRekognitionImage = new Image();
    DetectFacesRequest detectFaceRequest;
    DetectFacesResult detectFaceResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new AsyncTaskRunner().execute();
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


    public void getFaceDetetcResults() {

        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.happyhead);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            ByteBuffer imageBytes = ByteBuffer.wrap(stream.toByteArray());
            getAmazonRekognitionImage.withBytes(imageBytes);

            // Initialize the Amazon Cognito credentials provider
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "XXXXXXXXXXXXXXXXXXXXXX", // Please update Identity Pool ID before you run the applicationgit p
                    Regions.US_EAST_2 // Region
            );

            //I want "ALL" attributes
            amazonRekognitionClient = new AmazonRekognitionClient(credentialsProvider);
            detectFaceRequest = new DetectFacesRequest()
                    .withAttributes(Attribute.ALL.toString())
                    .withImage(getAmazonRekognitionImage);
            detectFaceResult = amazonRekognitionClient.detectFaces(detectFaceRequest);
            detectFaceResult.getFaceDetails();

        } catch (Exception ex) {
            Log.e("Error on something:", "Message:" + ex.getMessage());
        }
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getFaceDetetcResults();
            return null;
        }
    }
}
