package edu.kvcc.cis298.inclass3.inclass3;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jmartin5229 on 12/7/2015.
 */
public class CrimeFetcher {//like page 410

    //Sting constant for logging.
    private static final String TAG = "CrimeFragment";

    //Method to get the raw bytes from the web source.  Conversion from bytes
    //to something more meaningful will happen in a different method.
    //The method has one parameter which is the url that we want to connect to.
    private byte[] getUrlBytes(String urlSpec)throws IOException {
        //Create a new URL object from the url setring that was passed in.
        URL url = new URL(urlSpec);
        //Create a new HTTp connecction to the specified url.
        //If we were to laod data from a secure site, it would need
        //to use HttpsUrlConnection.
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            //Create an output stream to hold that dat that is read from
            //the url source.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //create a input stream from the http connectioin.
            InputStream in = connection.getInputStream();

            //Check to see that the response code from the http request is
            //200, which is the same as http_ok.  Every web request will return
            //some sort of response code. You can google them.  Typically
            //200s = good, 300s = cache, 400s = error, 500s = sever error
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        ": with" +
                urlSpec);
            }

            //Create an int to hold how many bytes were read in
            int bytesRead = 0;
            //create a byte aray to act as a buffer that will read
            //in up to 1024 bytes at a time.
            byte[] buffer = new byte[1024];

            //While we can read bytes from the input stream
            while ((bytesRead = in.read(buffer))>0){
                //write the bytes out to the output stream
                out.write(buffer, 0, bytesRead);
            }
            //Once everything has been read and written, close the
            //output stream
            out.close();

            //convert the output stream to a byte array
            return out.toByteArray();
        }finally {
            //Make sure the connectioni to the web is closed.
            connection.disconnect();
        }
    }
    //Method to get the string result from the http web address.
    //The url bytes representing the data get returned from the
    //getUrlBytes method, and are then transformed into a string.
    public String getUrlString (String  urlSpec) throws IOException {
        return new String (getUrlBytes(urlSpec));
    }

    public void fetchCrimes() {
        try {

            //This method will take the original URL and allow us to add any parameters that might be required to it.
            //For the URL's on my server there are no additional parameters needed.
            //However many API's require extra paramterser.  The API
            //Thjat the book uses requries extra parameters and this is where they add them.

            String url = Uri.parse("http:/barnesbrothers.homeserver.com/crimeapi")
                    .buildUpon()
                    //Add extra pramaeters here with the method
                    //.appendQueryParameter("param", "Value")
                    .build().toString();

            String jsonString = getUrlString(url);
            Log.i(TAG, "Recieved JSON: " + jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch itms", ioe);

        }
    }
}
