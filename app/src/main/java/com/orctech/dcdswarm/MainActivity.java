package com.orctech.dcdswarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import static com.orctech.dcdswarm.DateExtension.getInstance;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    ListView mListView;
    PortalDay portalDay = new PortalDay();
    AsyncTask request;
    Toolbar t;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        intent.getExtras();
        String username = intent.getExtras().get(LoginActivity.USERNAME_PARAMETER).toString();
        String pass = intent.getExtras().get(LoginActivity.PASSWORD_PARAMETER).toString();
        
        mListView = (ListView) findViewById(R.id.listView);
        t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("Date");
        setSupportActionBar(t);
    
        changePortalDate(new Date());

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

//    public void downloadAssignments(String username, String password) {
////        request.cancel(true);
//        try {
//            String[] params = {"https://www.dcds.edu/userlogin.cfm?pp=8256&userrequest=YES&keyrequest=false&userpage=8253", username, password};
//            request = new PortalLoginTask(this);
//            request.execute(params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    @Override
    public void processFinish(String output) {
        if (output == null) {
            return;
        }
        try {
            this.portalDay = HtmlStringHelper.processCalendarString(output);
        } catch (IOException e) {
            e.printStackTrace();
            portalDay = new PortalDay(portalDay.date);
            //TODO: add error alerts
        }
        CacheHelper.getInstance().storePortalDay(this, portalDay);
        updateTable();
    }
    
    public void updateTable() {
        t.setTitle(getInstance().formatWithDay.format(this.portalDay.date));
        AssignmentAdapter adapter = new AssignmentAdapter(this, portalDay.assignments);
        mListView.setAdapter(adapter);
    }
    //region Date changing
    public void tomorrow(View view) {
        changePortalDate(getInstance().tomorrow(portalDay.date));
    }
    
    public void yesterday(View view) {
        changePortalDate(getInstance().yesterday(portalDay.date));
    }
    
    public void changePortalDate(Date newDate) {
        if(request != null)
            request.cancel(true);
        portalDay = CacheHelper.getInstance().getPortalDay(this, newDate);
        updateTable();
        //TODO: add loading signal
        request = new PortalDayTask(this).execute(newDate);
    }
    //endregion
    
    private class PortalDayTask extends AsyncTask<Date, Void, String> {
        HttpURLConnection portal;
        Date requestDate;
        private AsyncResponse delegate = null;
        
        public PortalDayTask(AsyncResponse activity) {
            this.delegate = activity;
        }
        
        @Override
        protected String doInBackground(Date... params) {
            try {
                //region Request
                requestDate = params[0];
                
                URL url = new URL(String.format(getString(R.string.URL_schedule_day) + "&start=%s&period=day", getInstance().formatSlashed.format(requestDate)));
                portal = (HttpURLConnection) url.openConnection();
                portal.setRequestMethod("GET");
                
                portal.setConnectTimeout(3000);
                CookieManager.getInstance().setCookies(portal);
                portal.setConnectTimeout(3000);
                portal.setReadTimeout(3000);
                portal.setDoInput(true);
                portal.setDoOutput(true);
                //endregion
                //region Response
                int responseCode = portal.getResponseCode();
                CookieManager.getInstance().storeCookies(portal);
                
                BufferedReader in = new BufferedReader(new InputStreamReader(portal.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    return responseCode + "";
                }
                //endregion
                
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (portal != null) {
                    portal.disconnect();
                }
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(String s) {
            delegate.processFinish(s);
        }
    }

//    private class PortalLoginTask extends AsyncTask<String, Integer, String> {
//        HttpURLConnection portal;
//
//        private AsyncResponse delegate = null;
//
//
//        public PortalLoginTask(AsyncResponse activity) {
//            delegate = activity;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {//params for string: url, username, password, date
//            portal = null;
//            StringBuffer response;
//
//            try {
//                URL url = new URL(params[0]);
//                portal = (HttpURLConnection) url.openConnection();
//                portal.setUseCaches(true);
//                portal.setRequestMethod("POST");
//                portal.setConnectTimeout(30000);
//                portal.setInstanceFollowRedirects(false);
//
////                portal.setRequestProperty("Host", "www.dcds.edu");
////                portal.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
////                portal.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
////                portal.setRequestProperty("cache-Control", "max-age=0");
//
//                String urlParameters = String.format("do=login&p=413&username=%s&password=%s&submit=login", params[1], params[2]);
//
//                portal.setRequestProperty("Content-Length", Integer.toString(urlParameters.length()));
//
//                portal.setDoInput(true);
//                portal.setDoOutput(true);
//
//                // Send post request
//                DataOutputStream wr = new DataOutputStream(portal.getOutputStream());
//                wr.writeBytes(urlParameters);
//                wr.flush();
//                wr.close();
//
//                int responseCode = portal.getResponseCode();
//                System.out.println("\nSending 'POST' request to URL : " + url);
//                System.out.println("Post parameters : " + urlParameters);
//                System.out.println("Response Code : " + responseCode);
//
//                CookieManager.getInstance().storeCookies(portal);
//
//                if (responseCode == 302) {
//
//                    try {
//                        url = new URL(portal.getHeaderField(getString(R.string.responseHeader_Location)));
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
////                        url = new URL(portal.getURL().getHost() + "/" + portal.getHeaderField(getString(R.string.responseHeader_Location)));
//                    } finally {
//                        if (portal != null)
//                            portal.disconnect();
//                    }
//                    try {
//                        portal = (HttpURLConnection) url.openConnection();
//                        portal.setRequestMethod("GET");
//
//                        portal.setConnectTimeout(3000);
//                        CookieManager.getInstance().setCookies(portal);
////                        portal.setRequestProperty(getString(R.string.requestHeader_Cookie), cookieStringBuffer.toString());
//                        portal.setConnectTimeout(3000);
//                        portal.setReadTimeout(3000);
//
////                        urlParameters = String.format("p=8256&start=05/21/2017&period=day");
//
////                        portal.setRequestProperty("Content-Length", Integer.toString(urlParameters.length()));
////
////                        portal.setDoInput(true);
////                        portal.setDoOutput(true);
//
//                        // Send post request
////                        wr = new DataOutputStream(portal.getOutputStream());
//////                        wr.writeBytes(urlParameters);
////                        wr.flush();
////                        wr.close();
//
//                        int NresponseCode = portal.getResponseCode();
//
//                        BufferedReader in = null;
//                        in = new BufferedReader(new InputStreamReader(portal.getInputStream()));
//                        String inputLine;
//                        response = new StringBuffer();
//
//                        while ((inputLine = in.readLine()) != null) {
//                            response.append(inputLine);
//                        }
//                        in.close();
//
//                        if (NresponseCode != HttpsURLConnection.HTTP_OK) {
//                            return NresponseCode + "";
//                        }
//
//
//                        return response.toString();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        if (portal != null) {
//                            portal.disconnect();
//                        }
//
//                    }
//                } else {
//                    BufferedReader in = new BufferedReader(new InputStreamReader(portal.getInputStream()));
//                    String inputLine;
//                    response = new StringBuffer();
//
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//                    return response.toString();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (portal != null) {
//                    portal.disconnect();
//                }
//            }
//            return null;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            delegate.processFinish(s);
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onCancelled(String s) {
//            super.onCancelled(s);
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//    }
}

interface AsyncResponse {
    void processFinish(String output);
}