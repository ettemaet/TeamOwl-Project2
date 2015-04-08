package edu.msu.stanospa.teamowl_project2;

import android.util.Log;
import android.util.Xml;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 4/8/15.
 */
public class Cloud {
    private static final String REGISTER_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/register.php";
    private static final String LOGIN_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/login.php";
    private static final String UTF8 = "UTF-8";

    /**
     * Skip the XML parser to the end tag for whatever
     * tag we are currently within.
     * @param xml the parser
     * @throws IOException
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml)
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG &&
                tag != XmlPullParser.END_DOCUMENT);
    }

    /**
     * Open a connection to a hatting in the cloud.
     * @param UserId the user name
     * @param PassWord the password send to the server
     *
     * @return reference to an input stream or null if this fails
     */
    public String LogOnCloud(final String UserId, final String PassWord) {

        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(LOGIN_URL);

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("user", UserId));
        nameValuePair.add(new BasicNameValuePair("pw", PassWord));

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String responseString = " ";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            // write response to log
            responseString = response.toString();
            Log.i("return string",responseString);
            //Log.d("Http Post Response:", response.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

        /**
         * Create an XML parser for the result
         */
        try {
            XmlPullParser xml = Xml.newPullParser();
            xml.setInput(new StringReader(responseString));

            xml.nextTag();      // Advance to first tag
            xml.require(XmlPullParser.START_TAG, null, "login");

            String status = xml.getAttributeValue(null, "status");
            if(status.equals("no")) {
                return null;
            }

            return xml.getAttributeValue(null, "id");


            // We are done
        } catch(XmlPullParserException ex) {
            return null;
        } catch(IOException ex) {
            return null;
        }
    }


}
