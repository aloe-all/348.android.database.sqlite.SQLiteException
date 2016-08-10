package com.crg.a348androiddatabasesqlitesqliteexception;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mUpgradeButton;
    private SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabaseRead;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUpgradeButton = (Button) findViewById(R.id.upgrade_db);
        mUpgradeButton.setOnClickListener(this);
        mThreadPoolExecutor = new ThreadPoolExecutor(2, 10, 20,TimeUnit.SECONDS, new ArrayBlockingQueue(10),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        mSQLiteOpenHelper = new MyDatabaseHelper(this, "aloe.db", null, 1);
        db = mSQLiteOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", "The Da Vinci Code");
        values.put("author", "Dan Brown");
        values.put("pages", 454);
        values.put("price", 16.96);
        db.insert("Book", null, values);
        db.close();
    }

    @Override
    public void onClick(View v) {
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

                mSQLiteOpenHelper = new MyDatabaseHelper(MainActivity.this, "aloe.db", null, 1);
                mSQLiteDatabaseRead = mSQLiteOpenHelper.getReadableDatabase();
                mSQLiteDatabaseRead.setVersion(2);
                ContentValues values = new ContentValues();
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                mSQLiteDatabaseRead.insert("Book", null, values);
                mSQLiteDatabaseRead.close();
            }
        });

    }
}
