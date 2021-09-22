package android.ztech.com.prescription;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

class InternetCheck extends AsyncTask<Void,Void,Boolean> {


    public boolean internetStatus = false;


    public InternetCheck ()
    {

    }

    @Override protected Boolean doInBackground(Void... voids) { try {
        Socket sock = new Socket();
        sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
        sock.close();
        return true;
    } catch (IOException e) { return false; } }


    @Override protected void onPostExecute(Boolean internet)
    {

        internetStatus = internet;
    }




}

