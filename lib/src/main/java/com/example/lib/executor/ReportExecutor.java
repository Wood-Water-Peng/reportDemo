package com.example.lib.executor;

import com.example.lib.ReportLog;
import com.example.lib.bean.PageEventWrapper;
import com.example.lib.db.PageEventDao;
import com.example.lib.db.PageEventDaoImpl;
import com.example.lib.event.PageEvent;
import com.example.lib.net.ReportRequest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 4:35 PM
 * @Version 1.0
 * <p>
 * 数据库信息上报逻辑
 * 1.根据数据库当前的条数决定上报次数,每次最多上报20条
 * 2.在生成一次上报数据后，将数据库中的数据删除
 * 3.最终表中的数据为0
 * 4.如果发生异常导致删除失败，那么继续走1
 */
public class ReportExecutor {
    private final JobExecutors mExecutors;
    public final int MIN_REPORT_NUM = 20;
    public final int MAX_REPORT_NUM = 50;
    public final int DEFAULT_REPORT_NUM = MIN_REPORT_NUM;
    private final PageEventDao mEventDao;
    //缓存阈值
    private static final int PENDING_THRESHOLD = 20;
    private final ArrayDeque<PageEvent> pendingEvents = new ArrayDeque<>();

    public ReportExecutor() {
        this.mExecutors = new JobExecutors();
        mEventDao = new PageEventDaoImpl();
    }


    public void enqueueEvent(final PageEvent event) {
        if (pendingEvents.size() > PENDING_THRESHOLD) {

        } else {
            pendingEvents.add(event);
            ReportLog.logD("enqueueEvent:" + event.name + "---当前容量:" + pendingEvents.size());
        }
        mExecutors.diskIO().execute(new Runnable() {

            @Override
            public void run() {
                //将记录插入数据库
                mEventDao.insertEvent(event);
            }
        });
        realReport(event);

    }


    public void realReport(final PageEvent event) {


        mExecutors.networkIO().execute(new Runnable() {

            @Override
            public void run() {

                int count = generateReportCount(mEventDao.getEventNum(), DEFAULT_REPORT_NUM);

                //根据count生成request
                for (int i = 0; i < count; i++) {
                    PageEventWrapper pageEventWrapper = mEventDao.generateWrapperFromDB(DEFAULT_REPORT_NUM);
                    ReportLog.logD("上报数据->" + pageEventWrapper.getNum());
                    boolean request = new ReportRequest(pageEventWrapper).performRequest();
                }
            }
        });
    }

    /**
     * @param totalSize   数据库中总条数
     * @param singleCount 单次上报条数
     * @return
     */
    private int generateReportCount(int totalSize, int singleCount) {
        if (totalSize == 0) return 0;
        if (totalSize <= singleCount) return 1;
        return (totalSize / singleCount) + ((singleCount % totalSize) > 0 ? 1 : 0);
    }
}
