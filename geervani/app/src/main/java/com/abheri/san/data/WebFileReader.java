package com.abheri.san.data;

import com.abheri.san.view.GeervaniViews;
import com.abheri.san.view.GeervaniViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.abheri.san.view.HandleServiceResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WebFileReader extends AsyncTask<String, String, Boolean> {

    android.support.v4.app.FragmentManager fragmgr;

    ProgressDialog progressDialog;
    HandleServiceResponse serviceResponseInterface;
    GeervaniViews currentView;
    Context context;

    public WebFileReader(HandleServiceResponse handleServiceResponse,
                       GeervaniViews cview, Context ctx)
    {
        serviceResponseInterface = handleServiceResponse;
        currentView = cview;
        context = ctx;
    }

    @Override
    protected Boolean doInBackground(String... uri) {

        DataFileCopier dfc = new DataFileCopier(context);

        //TODO have copy files methods return boolean to differentiate
        //  with an error case
        dfc.copyTopicFiles();
        dfc.copyWordFiles();

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if(result)
            serviceResponseInterface.onSuccess(result);
        else
            serviceResponseInterface.onError(result);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate();
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

        /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }

}