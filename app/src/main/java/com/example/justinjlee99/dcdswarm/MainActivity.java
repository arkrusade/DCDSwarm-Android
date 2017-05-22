package com.example.justinjlee99.dcdswarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    ListView mListView;
    TextView mTestTextView;
    HttpURLConnection portal;
    ArrayList<Assignment> assignments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String username = intent.getStringExtra(LoginActivity.USERNAME_PARAMETER);
        String pass = intent.getStringExtra(LoginActivity.PASSWORD_PARAMETER);

        mListView = (ListView) findViewById(R.id.listView);
        downloadAssignments(username, pass);
        AssignmentAdapter adapter = new AssignmentAdapter(this, assignments);
        mListView.setAdapter(adapter);


        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(username);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void downloadAssignments(String username, String password) {
        ArrayList<String[]> names = new ArrayList();

//        for (int i = 0; i < 20; i++) {
//            names.add(new String[3]);
//            for (int j = 0; j < 3; j++) {
//                names.get(i)[j] = String.format(Locale.getDefault(), "%d%d", i, j);
//            }
//            assignments.add(new Assignment(names.get(i)));
//        }


        //TODO:
        try {
            String[] params = {"https://www.dcds.edu/userlogin.cfm?pp=8256&userrequest=YES&keyrequest=false&userpage=8253", username, password};
            PortalLoginTask portalTask = new PortalLoginTask(this);
            portalTask.execute(params);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String output) {
        System.out.println(output);
        //TODO: process assignment string into objects
    }

    public boolean loginToPortal(String username, String password) {
        portal = null;
        try {

            PortalLoginTask task = new PortalLoginTask(this);
            String[] asdf = {"https://www.dcds.edu/userlogin.cfm?pp=8256&userrequest=YES&keyrequest=false&userpage=8253", username, password};
            String result = task.execute(asdf).get();


            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    private class PortalLoginTask extends AsyncTask<String, Integer, String> {
        //TODO: better input

        private AsyncResponse delegate = null;

        private static final String DATE_FORMAT = "EEE, dd-MMM-yyyy hh:mm:ss z";
        private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        public PortalLoginTask(AsyncResponse activity) {
            delegate = activity;
        }

        private boolean isNotExpired(String cookieExpires) {
            if (cookieExpires == null) {
                return true;
            }
            Date now = new Date();
            try {
                return (now.compareTo(dateFormat.parse(cookieExpires))) <= 0;
            }
            catch (java.text.ParseException pe) {
                pe.printStackTrace();
                return false;
            }
        }

        private boolean comparePaths(String cookiePath, String targetPath) {
            if (cookiePath == null) {
                return true;
            } else if (cookiePath.equals("/")) {
                return true;
            } else if (targetPath.regionMatches(0, cookiePath, 0, cookiePath.length())) {
                return true;
            } else {
                return false;
            }

        }

        @Override
        protected String doInBackground(String... params) {//params for string: url, username, password
            portal = null;
            StringBuffer response;
            try {
                URL url = new URL(params[0]);
                portal = (HttpURLConnection) url.openConnection();
                portal.setUseCaches(true);
                portal.setRequestMethod("POST");
                portal.setConnectTimeout(30000);
                portal.setInstanceFollowRedirects(false);

                String urlParameters = String.format("do=login&p=413&username=%s&password=%s&submit=login", params[1], params[2]);

                portal.setRequestProperty("Content-Length", Integer.toString(urlParameters.length()));

                portal.setDoInput(true);
                portal.setDoOutput(true);

                // Send post request
                DataOutputStream wr = new DataOutputStream(portal.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = portal.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                if (responseCode == 302) {
                    Map cookiesMap = storeCookies(portal);


                    if (cookiesMap == null) {
                        return "Error - no cookies";//TODO:asdf
                    }
                    StringBuffer cookieStringBuffer = new StringBuffer();

                    Iterator cookieNames = cookiesMap.keySet().iterator();
                    while (cookieNames.hasNext()) {
                        String cookieName = (String) cookieNames.next();
                        Map cookie = (Map) cookiesMap.get(cookieName);
                        // check cookie to ensure path matches  and cookie is not expired
                        // if all is cool, add cookie to header string
                        if (comparePaths((String) cookie.get("path"), portal.getURL().getPath()) && isNotExpired((String) cookie.get("expires"))) {
                            cookieStringBuffer.append(cookieName + "=" + (String) cookie.get(cookieName));
                            if (cookieNames.hasNext()) {
                                cookieStringBuffer.append("; ");
                            }
                        }
                    }
                    try {
                        url = new URL(portal.getHeaderField("Location"));
                    }
                    catch (MalformedURLException e) {
                        url = new URL(portal.getURL().getHost() + "/" + portal.getHeaderField("Location"));
                    }
                    finally {
                        if (portal != null)
                            portal.disconnect();
                    }
                    try {
                        portal.disconnect();
                        portal = (HttpURLConnection) url.openConnection();
                        portal.setRequestMethod("GET");

                        portal.setConnectTimeout(3000);

                        portal.setRequestProperty("Cookie", cookieStringBuffer.toString());
                        portal.setConnectTimeout(3000);
                        portal.setReadTimeout(3000);

                        int NresponseCode = portal.getResponseCode();

                        BufferedReader in = null;
                        in = new BufferedReader(new InputStreamReader(portal.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        if (NresponseCode != HttpsURLConnection.HTTP_OK) {
                            return NresponseCode + "";
                        }


                        return response.toString();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (portal != null) {
                            portal.disconnect();
                        }

                    }
                } else {
                    BufferedReader in = null;
                    in = new BufferedReader(new InputStreamReader(portal.getInputStream()));
                    String inputLine;
                    response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (portal != null) {
                    portal.disconnect();
                }
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            delegate.processFinish(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class HTMLGetTextTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            int count = urls.length;
            String total = "";
            for (int i = 0; i < count; i++) {
                total += getHTMLdata(urls[i]);
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) {
                    break;
                }
            }
            return total;
        }

        protected void onProgressUpdate(Integer... progress) {
            mTestTextView = (TextView) findViewById(R.id.testTextView);
            mTestTextView.setText("Downloaded " + progress[0] + " bytes.");
        }

        protected void onPostExecute(String result) {
            mTestTextView = (TextView) findViewById(R.id.textView3);
            mTestTextView.setText("Downloaded " + result + " bytes");
        }
    }

    private String getHTMLdata(URL url) {
        String data = "";
        HttpURLConnection urlC = null;
        try {
            urlC = (HttpURLConnection) url.openConnection();
            InputStream in = urlC.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int d = isw.read();
            while (d != -1) {
                char current = (char) d;
                d = isw.read();
                data += current;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (urlC != null) {
                urlC.disconnect();
            }
        }
        return data;
    }

    public Map storeCookies(URLConnection conn) throws IOException {

        Map domainStore; // this is where we will store cookies for this domain

        domainStore = new HashMap();

        String headerName;
        for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
            if (headerName.equalsIgnoreCase("Set-Cookie")) {
                Map cookie = new HashMap();
                StringTokenizer st = new StringTokenizer(conn.getHeaderField(i), ";");

                if (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    String name = token.substring(0, token.indexOf("="));
                    String value = token.substring(token.indexOf("=") + 1, token.length());
                    domainStore.put(name, cookie);
                    cookie.put(name, value);
                }

                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (token.indexOf("=") != -1)
                        cookie.put(token.substring(0, token.indexOf("=")).toLowerCase(), token.substring(token.indexOf("=") + 1, token.length()));

                }
            }
        }
        return domainStore;
    }
}

interface AsyncResponse {
    void processFinish(String output);
}