package com.orctech.dcdswarm.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.orctech.dcdswarm.Adapters.AssignmentAdapter;
import com.orctech.dcdswarm.Helpers.CacheHelper;
import com.orctech.dcdswarm.Helpers.CookieHelper;
import com.orctech.dcdswarm.Helpers.HtmlStringHelper;
import com.orctech.dcdswarm.Models.PortalDay;
import com.orctech.dcdswarm.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import static com.orctech.dcdswarm.Helpers.DateExtension.getDateExtension;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    ListView mListView;
    PortalDay portalDay = new PortalDay();
    AsyncTask request;
    Toolbar   t;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the Intent that started this activity and extract the string
        
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

    //region Menu bar
    public void goToBlockSchedule(View view) {
        startActivity(new Intent(this, BlockActivity.class));
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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_blocks:
                goToBlockSchedule(item.getActionView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion
    
    @Override
    public void processFinish(String output) {
        if (output == null) {
            return;
        }
        try {
            this.portalDay = HtmlStringHelper.processCalendarString(output);
        } catch (IOException e) {
            e.printStackTrace();
            portalDay = new PortalDay(portalDay.getDate());
            //TODO: add error alerts
        }
        CacheHelper.getInstance().storePortalDay(this, portalDay);
        updateTable();
    }
    
    public void updateTable() {
        t.setTitle(getDateExtension().formatWithDay.format(this.portalDay.getDate()));
        AssignmentAdapter adapter = new AssignmentAdapter(this, portalDay.getAssignments());
        mListView.setAdapter(adapter);
    }
    
    //region Date changing
    public void changePortalDate(Date newDate) {
        if (request != null) {
            request.cancel(true);
        }
        portalDay = CacheHelper.getInstance().getPortalDay(this, newDate);
        updateTable();
        //TODO: add loading signal
        request = new PortalDayTask(this).execute(newDate);
    }
    
    public void tomorrow(View view) {
        changePortalDate(getDateExtension().tomorrow(portalDay.getDate()));
    }
    
    public void yesterday(View view) {
        changePortalDate(getDateExtension().yesterday(portalDay.getDate()));
    }
    
    public void weekNext(View view) {
        changePortalDate(getDateExtension().weekNext(portalDay.getDate()));
    }
    
    public void weekPrev(View view) {
        changePortalDate(getDateExtension().weekPrev(portalDay.getDate()));
    }
    
    public void today(View view) {
        changePortalDate(new Date());
    }
    //endregion
    
    private class PortalDayTask extends AsyncTask<Date, Void, String> {
        HttpURLConnection portal;
        Date              requestDate;
        private AsyncResponse delegate = null;
        
        PortalDayTask(AsyncResponse activity) {
            this.delegate = activity;
        }
        
        @Override
        protected String doInBackground(Date... params) {
            try {
                //region Request
                requestDate = params[0];
                
                URL url = new URL(String.format(getString(R.string.URL_schedule_day) +
                        "&start=%s&period=day", getDateExtension().formatSlashed.format(requestDate)));
                portal = (HttpURLConnection) url.openConnection();
                portal.setRequestMethod("GET");
                
                portal.setConnectTimeout(3000);
                CookieHelper.getCookieHelper().setCookies(portal);
                portal.setConnectTimeout(3000);
                portal.setReadTimeout(3000);
                portal.setDoInput(true);
                portal.setDoOutput(true);
                //endregion
                //region Response
                int responseCode = portal.getResponseCode();
                CookieHelper.getCookieHelper().storeCookies(portal);
                
                BufferedReader in = new BufferedReader(new InputStreamReader(portal
                        .getInputStream()));
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
    //        protected String doInBackground(String... params) {//params for string: url,
    // username, password, date
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
    ////                portal.setRequestProperty("Content-Type",
    /// "application/x-www-form-urlencoded");
    ////                portal.setRequestProperty("Accept", "text/html,application/xhtml+xml,
    /// application/xml;q=0.9,image/webp,*/*;q=0.8");
    ////                portal.setRequestProperty("cache-Control", "max-age=0");
    //
    //                String urlParameters = String.format
    // ("do=login&p=413&username=%s&password=%s&submit=login", params[1], params[2]);
    //
    //                portal.setRequestProperty("Content-Length", Integer.toString(urlParameters
    // .length()));
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
    //                CookieHelper.getInstance().storeCookies(portal);
    //
    //                if (responseCode == 302) {
    //
    //                    try {
    //                        url = new URL(portal.getHeaderField(getString(R.string
    // .responseHeader_Location)));
    //                    } catch (MalformedURLException e) {
    //                        e.printStackTrace();
    ////                        url = new URL(portal.getURL().getHost() + "/" + portal
    /// .getHeaderField(getString(R.string.responseHeader_Location)));
    //                    } finally {
    //                        if (portal != null)
    //                            portal.disconnect();
    //                    }
    //                    try {
    //                        portal = (HttpURLConnection) url.openConnection();
    //                        portal.setRequestMethod("GET");
    //
    //                        portal.setConnectTimeout(3000);
    //                        CookieHelper.getInstance().setCookies(portal);
    ////                        portal.setRequestProperty(getString(R.string
    /// .requestHeader_Cookie), cookieStringBuffer.toString());
    //                        portal.setConnectTimeout(3000);
    //                        portal.setReadTimeout(3000);
    //
    ////                        urlParameters = String.format("p=8256&start=05/21/2017&period=day");
    //
    ////                        portal.setRequestProperty("Content-Length", Integer.toString
    /// (urlParameters.length()));
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
    //                        in = new BufferedReader(new InputStreamReader(portal.getInputStream
    // ()));
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
    //                    BufferedReader in = new BufferedReader(new InputStreamReader(portal
    // .getInputStream()));
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