package com.orctech.dcdswarm1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orctech.dcdswarm1.Adapters.BlockAdapter;
import com.orctech.dcdswarm1.Helpers.BlockHelper;
import com.orctech.dcdswarm1.Helpers.CacheHelper;
import com.orctech.dcdswarm1.Helpers.DateExtension;
import com.orctech.dcdswarm1.Models.BlockSchedule;
import com.orctech.dcdswarm1.R;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;

import static com.orctech.dcdswarm1.Helpers.DateExtension.getDateExtension;


public class BlockActivity extends AppCompatActivity {
    ListView mListView;
    Date date;
    Toolbar t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        Intent intent = getIntent();
        String dateString = intent.getStringExtra(MainActivity.DATE_KEY);
        try {
            date = getDateExtension().formatSlashed.parse(dateString);
        }
        catch (ParseException e) {
            date = new Date();
        }

        t = (Toolbar) findViewById(R.id.toolbar_block);
        setSupportActionBar(t);
        t.setTitle(DateExtension.getDateExtension().formatWithDay.format(date));

        mListView = (ListView) findViewById(R.id.list_block);
        downloadAndShowSchedule();
    }

    //region Date changing
    public void changeScheduleDate(Date newDate) {
        date = newDate;
        downloadAndShowSchedule();
    }

    public void downloadAndShowSchedule() {
        for (String fileRef : BlockHelper.fileRefsForDate(date)) {

            if (!CacheHelper.getInstance().isFileRefDownloaded(getApplicationContext(), fileRef)) {
                StorageReference mStorageRef;
                mStorageRef = FirebaseStorage.getInstance().getReference();
                final StorageReference islandRef = mStorageRef.child(fileRef);

                final long ONE_MEGABYTE = 1024 * 1024;
                islandRef.getBytes(ONE_MEGABYTE)
                         .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                             @Override
                             public void onSuccess(byte[] bytes) {
                                 String json = new String(bytes, Charset.defaultCharset());
                                 BlockHelper.processBlocks(json, getApplicationContext());
                                 CacheHelper.getInstance()
                                            .downloadedFileRef(getApplicationContext(),
                                                               islandRef.getPath());
                                 updateTable();
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception exception) {
                                 // Handle any errors
                                 String a = "a";
                                 //TODO: something when it fails
                             }
                         });
                //TODO: something while it downloads, if it needs to
            } else {
                updateTable();
            }
        }
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
