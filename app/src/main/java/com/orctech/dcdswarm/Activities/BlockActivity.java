package com.orctech.dcdswarm.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.orctech.dcdswarm.Adapters.BlockAdapter;
import com.orctech.dcdswarm.Helpers.BlockHelper;
import com.orctech.dcdswarm.R;

public class BlockActivity extends AppCompatActivity {
    ListView mListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.list_block);
        BlockAdapter blockAdapter = new BlockAdapter(this, BlockHelper.processBlocks(this));
        mListView.setAdapter(blockAdapter);
    }

}
