package com.example.lib;

import android.os.AsyncTask;

import com.example.lib.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @Author jacky.peng
 * @Date 2021/4/13 11:29 AM
 * @Version 1.0
 */
public class Write2DBTask extends AsyncTask<List<Event>, Void, List<Event>> {

    @Override
    protected void onPostExecute(List<Event> events) {
        super.onPostExecute(events);
        ReportLog.logD("Write2DBTask onPostExecute");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ReportLog.logD("Write2DBTask onPreExecute");
    }

    @Override
    protected List<Event> doInBackground(List<Event>... lists) {
        List<Event> list = lists[0];
        List<Event> failed = new ArrayList<>();
        for (Event e :
                list) {
            boolean flag = FakeDB.getsInstance().write(e);
            if (!flag) {
                failed.add(e);
            }
        }
        if (failed.isEmpty()) {
            ReportLog.logD("doInBackground all write to db");
            return null;
        } else {
            ReportLog.logD("doInBackground failed->" + failed.size());
            return failed;
        }
    }
}
