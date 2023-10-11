package com.ecm.dashobd_plus;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_PERMISSION_CODE = 0;

    private static final String TAG = MainActivity.class.getSimpleName();

    String[] permissions = new String[]{
            android.Manifest.permission.BLUETOOTH_SCAN,
            //Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INSTALL_PACKAGES,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.INTERNET

    };

    String link = "https://github.com/shmykelsa/AAAD/releases/download/v1.4.4/AAAD-1.4.4-release.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        checkPermissions();



    }


    /**
     * <p>Function to check permission, then request them if they are not granted</p>
     */
    public void checkPermissions() {

        //ask for permissions

        Log.v(TAG, "requesting permissions");
        requestPermissions(permissions, REQUEST_PERMISSION_CODE);

        for (int i = 0; i < permissions.length; i++) {



        }
    }


    /**
     * <p></p>
     */

    private void downloadFile(String url){
        Log.v(TAG, "Downloading file");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(link);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        request.setAllowedOverMetered(true);
        request.setTitle("test");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, "/tt");
        long reference = manager.enqueue(request);
        Log.v(TAG, "Downloaded file");
    }
}