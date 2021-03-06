package edu.msu.stanospa.teamowl_project2;

import android.util.Log;
import android.util.Xml;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Cloud {
    private static final String REGISTER_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/register.php";
    private static final String LOGIN_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/login.php";
    private static final String FINDGAME_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/findgame.php";
    private static final String PLACEBIRD_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/placebird.php";
    private static final String GETTURNID_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/getturnid.php";
    private static final String EXITDC_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/endgame.php";
    private static final String ISMYTURN_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/ismyturn.php";
    private static final String GETMOVE_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/getturninfo.php";
    private static final String GETPLAYERINFO_URL = "http://webdev.cse.msu.edu/~gaojun1/cse476/project2/getplayername.php";
    private static final String UTF8 = "UTF-8";

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

        List<NameValuePair> nameValuePair = new ArrayList<>(2);
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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
                return "no," + xml.getAttributeValue(null, "msg");
            }

            return "yes,"+xml.getAttributeValue(null, "id");


            // We are done
        } catch(XmlPullParserException ex) {
            return "no,XmlPullParserException";
        } catch(IOException ex) {
            return "no,IOException";
        }
    }

    public String CreateOnCloud(final String UserId, final String PassWord, final String PassWord1) {

        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(REGISTER_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(3);
        nameValuePair.add(new BasicNameValuePair("user", UserId));
        nameValuePair.add(new BasicNameValuePair("pw", PassWord));
        nameValuePair.add(new BasicNameValuePair("pw1", PassWord1));

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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
            xml.require(XmlPullParser.START_TAG, null, "register");

            String status = xml.getAttributeValue(null, "status");
            if(status.equals("no")) {
                return "no,"+xml.getAttributeValue(null, "msg");
            } else if ( status.equals("yes")){
                return "yes,register successful";
            }
            return null;


            // We are done
        } catch(XmlPullParserException ex) {
            return "no,XmlPullParserException";
        } catch(IOException ex) {
            return "no,IOException";
        }

    }

    public String isPlayerWaiting(String userid){
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(FINDGAME_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(1);
        nameValuePair.add(new BasicNameValuePair("userid", userid));

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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
            xml.require(XmlPullParser.START_TAG, null, "findgame");

            String status = xml.getAttributeValue(null, "status");
            if(status.equals("create")) {
                return "yes,create," + xml.getAttributeValue(null, "gameid") + "," + xml.getAttributeValue(null, "token");
            } else if ( status.equals("found")){
                // Player 2 joins and finds a game
                return "yes,found," + xml.getAttributeValue(null, "gameid") +
                        "," + xml.getAttributeValue(null, "opponentname") + "," + xml.getAttributeValue(null, "token");
            } else if (status.equals("ready")) {
                // Player 1 waiting has finally found a game
                return "yes,ready," + xml.getAttributeValue(null, "gameid") +
                        "," + xml.getAttributeValue(null, "player2name");
            } else if (status.equals("waiting")) {
                return "yes,waiting";
            } else if (status.equals("error")) {
                return "no," +xml.getAttributeValue(null, "msg");
            }
            return null;


            // We are done
        } catch(XmlPullParserException ex) {
            return "no,XmlPullParserException";
        } catch(IOException ex) {
            return "no,IOException";
        }

    }

    public String GetCurTurn(String gameid, String token)
    {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(GETTURNID_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(2);
        nameValuePair.add(new BasicNameValuePair("gameid", gameid));
        nameValuePair.add(new BasicNameValuePair("token", token));
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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
            xml.require(XmlPullParser.START_TAG, null, "turn");

            String status = xml.getAttributeValue(null, "status");
            if(status.equals("ok")) {
                return "yes," + xml.getAttributeValue(null, "turnnum") +"," + xml.getAttributeValue(null, "player");
            } else if (status.equals("dc")) {
                return "gameover,";
            } else if (status.equals("error")) {
                return "error," +xml.getAttributeValue(null, "msg");
            }
            return null;


            // We are done
        } catch(XmlPullParserException ex) {
            return "no,XmlPullParserException";
        } catch(IOException ex) {
            return "no,IOException";
        }
    }


    //player: expect 1 or 2
    public String GetPlayerInfo(String gameid, int player)
    {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(GETPLAYERINFO_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(2);
        nameValuePair.add(new BasicNameValuePair("gameid", gameid));
        nameValuePair.add(new BasicNameValuePair("player", Integer.toString(player)));

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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
            xml.require(XmlPullParser.START_TAG, null, "player");

            String status = xml.getAttributeValue(null, "status");
            if(status.equals("ok")) {
                return "yes," + xml.getAttributeValue(null, "playerid") +"," + xml.getAttributeValue(null, "playername");
            }  else if (status.equals("error")) {
                return "error," +xml.getAttributeValue(null, "msg");
            }
            return null;


            // We are done
        } catch(XmlPullParserException ex) {
            return "no,XmlPullParserException";
        } catch(IOException ex) {
            return "no,IOException";
        }

    }

    public boolean PlaceBirdCloud(String gameid,String turnnum,int birdid,float x, float y, boolean gameover,String token)
    {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(PLACEBIRD_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(1);
        nameValuePair.add(new BasicNameValuePair("gameid", gameid));
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("turnnum", turnnum));
        nameValuePair.add(new BasicNameValuePair("birdid", Integer.toString(birdid)));
        nameValuePair.add(new BasicNameValuePair("x", Float.toString(x)));
        nameValuePair.add(new BasicNameValuePair("y", Float.toString(y)));
        int gameoverI;
        if(gameover)
            gameoverI = 1;
        else
            gameoverI = 0;
        nameValuePair.add(new BasicNameValuePair("gameover", Integer.toString(gameoverI)));

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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
            xml.require(XmlPullParser.START_TAG, null, "place");

            String status = xml.getAttributeValue(null, "status");
            return status.equals("ok");
            // We are done
        } catch(XmlPullParserException ex) {
            return false;
        } catch(IOException ex) {
            return false;
        }
    }

    public void ExitGame(String gameid, String token) {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(EXITDC_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(2);
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("gameid", gameid));


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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
            Log.i("return string",responseString);
            //Log.d("Http Post Response:", response.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
    }

    public boolean isMyTurn(String gameid, String player, String token) {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(ISMYTURN_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(3);
        nameValuePair.add(new BasicNameValuePair("gameid", gameid));
        nameValuePair.add(new BasicNameValuePair("player", player));
        nameValuePair.add(new BasicNameValuePair("token", token));


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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
            xml.require(XmlPullParser.START_TAG, null, "turn");

            String status = xml.getAttributeValue(null, "status");
            Log.i("count",xml.getAttributeValue(null, "count"));
            if(status.equals("yes"))
            {
                Log.i("isMyTurn():", "yes");
                return true;
            }
            else
            {
                Log.i("isMyTurn():", "no");
                return false;
            }
            // We are done
        } catch(XmlPullParserException ex) {
            return false;
        } catch(IOException ex) {
            return false;
        }
    }

    public String GetMovement(String gameid, int turnnum, String token) {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(GETMOVE_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(3);
        nameValuePair.add(new BasicNameValuePair("gameid", gameid));
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("turnnum", Integer.toString(turnnum)));


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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
            xml.require(XmlPullParser.START_TAG, null, "turn");

            String status = xml.getAttributeValue(null, "status");
            if(status.equals("ok"))
            {
                return "yes," + xml.getAttributeValue(null, "birdid") + "," +
                        xml.getAttributeValue(null, "x") + "," +
                        xml.getAttributeValue(null, "y") + "," +
                        xml.getAttributeValue(null, "gameover");
            }
            else
            {
                return "error";
            }
            // We are done
        } catch(XmlPullParserException ex) {
            return "error";
        } catch(IOException ex) {
            return "error";
        }

    }

    public boolean IsGameOver(String gameid, String token) {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(GETTURNID_URL);

        List<NameValuePair> nameValuePair = new ArrayList<>(3);
        nameValuePair.add(new BasicNameValuePair("gameid", gameid));
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("player", "1"));


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
            HttpEntity responseEntity = response.getEntity();
            if(responseEntity != null) {
                responseString = EntityUtils.toString(responseEntity);
            }
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
            xml.require(XmlPullParser.START_TAG, null, "turn");

            String status = xml.getAttributeValue(null, "status");
            return status.equals("dc");
            // We are done
        } catch(XmlPullParserException ex) {
            return false;
        } catch(IOException ex) {
            return false;
        }
    }


}
