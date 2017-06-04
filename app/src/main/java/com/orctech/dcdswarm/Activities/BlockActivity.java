package com.orctech.dcdswarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.orctech.dcdswarm.Adapters.BlockAdapter;
import com.orctech.dcdswarm.Helpers.BlockHelper;
import com.orctech.dcdswarm.Helpers.CacheHelper;
import com.orctech.dcdswarm.Helpers.DateExtension;
import com.orctech.dcdswarm.Models.BlockSchedule;
import com.orctech.dcdswarm.R;

import java.text.ParseException;
import java.util.Date;

import static com.orctech.dcdswarm.Helpers.DateExtension.getDateExtension;

public class BlockActivity extends AppCompatActivity {
    ListView mListView;
    Date     date;
    Toolbar t;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        Intent intent = getIntent();
        String dateString = intent.getStringExtra(MainActivity.DATE_KEY);
        try {
            date = getDateExtension().formatSlashed.parse(dateString);
        } catch (ParseException e) {
            date = new Date();
        }
        
        t = (Toolbar) findViewById(R.id.toolbar_block);
        setSupportActionBar(t);
        t.setTitle(DateExtension.getDateExtension().formatWithDay.format(date));
        
        mListView = (ListView) findViewById(R.id.list_block);
        BlockHelper.processBlocks(this);
        updateTable();
    }
    
    //region Date changing
    public void changeScheduleDate(Date newDate) {
        date = newDate;
        updateTable();
    }
    
    private void updateTable() {
        t.setTitle(getDateExtension().formatWithDay.format(date));
        BlockSchedule schedule = CacheHelper.getInstance().getBlockSchedule(this, date);
        BlockAdapter blockAdapter = new BlockAdapter(this, schedule.getBlocks());
        mListView.setAdapter(blockAdapter);
    }
    
    public void tomorrow(View view) {
        changeScheduleDate(getDateExtension().tomorrow(date));
    }
    
    public void yesterday(View view) {
        changeScheduleDate(getDateExtension().yesterday(date));
    }
    
    public void weekNext(View view) {
        changeScheduleDate(getDateExtension().weekNext(date));
    }
    
    public void weekPrev(View view) {
        changeScheduleDate(getDateExtension().weekPrev(date));
    }
    
    public void today(View view) {
        changeScheduleDate(new Date());
    }
    //endregion
}
