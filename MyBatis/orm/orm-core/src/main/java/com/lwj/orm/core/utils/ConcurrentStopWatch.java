package com.lwj.orm.core.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Date: 2021/7/28
 * <p>
 * Description: 多线程StopWatch 记录观测多个线程的运行时间
 *
 * @author liuWangjie
 */
public class ConcurrentStopWatch {

    private final boolean openStopWatchFlag;

    private final String id;

    private final TimeUnit timeUnit;

    private boolean keepTaskList = true;

    private final long timeConversion;

    private final String timeUnitStr;

    private final List<TaskInfo> taskList = new LinkedList<>();

    private final Map<String, TaskInfo> taskMap = new ConcurrentHashMap<>();

    private long startTimeNanos;

    private long endTimeNanos;

    private int taskCount;

    private final AtomicLong totalTimeNanos;

    public ConcurrentStopWatch() {
        this("");
    }

    public ConcurrentStopWatch(String id) {
        this(id, TimeUnit.MILLISECONDS, true);
    }

    public ConcurrentStopWatch(String id, boolean openStopWatchFlag) {
        this(id, TimeUnit.MILLISECONDS, openStopWatchFlag);
    }

    public ConcurrentStopWatch(String id, TimeUnit timeUnit, boolean openStopWatchFlag) {
        this.id = id;
        this.totalTimeNanos = new AtomicLong();
        long chooseTimeConversion;
        String timeUnitStr;
        switch (timeUnit) {
            case SECONDS:
                chooseTimeConversion = 1000000000;
                timeUnitStr = "s";
                break;
            case MILLISECONDS:
                chooseTimeConversion = 1000000;
                timeUnitStr = "ms";
                break;
            case MICROSECONDS:
                chooseTimeConversion = 1000;
                timeUnitStr = "us";
                break;
            default:
                chooseTimeConversion = 1;
                timeUnitStr = "ns";
        }
        this.timeUnitStr = timeUnitStr;
        this.timeConversion = chooseTimeConversion;
        this.timeUnit = timeUnit;
        this.openStopWatchFlag = openStopWatchFlag;
    }

    public void setKeepTaskList(boolean keepTaskList) {
        this.keepTaskList = keepTaskList;
    }

    public void start(String taskName) throws IllegalStateException {
        if (!openStopWatchFlag) {
            return;
        }
        Long id = Thread.currentThread().getId();
        String key = id + taskName;
        TaskInfo taskInfo = taskMap.get(key);
        long curNanoTime = System.nanoTime();
        if (this.startTimeNanos == 0) {
            startTimeNanos = curNanoTime;
        }
        if (taskInfo == null) {
            TaskInfo newTask = new TaskInfo(taskName, curNanoTime);
            taskMap.put(key, newTask);
            return;
        }
        throw new IllegalStateException("Can't start StopWatch: it's already running");
    }

    public void stop(String taskName) throws IllegalStateException {
        if (!openStopWatchFlag) {
            return;
        }
        Long id = Thread.currentThread().getId();
        String key = id + taskName;
        TaskInfo taskInfo = taskMap.get(key);
        if (taskInfo != null) {
            long curNanoTime = System.nanoTime();
            this.endTimeNanos = curNanoTime;
            long useTimeNanos = curNanoTime - taskInfo.startTimeNanos;
            this.totalTimeNanos.addAndGet(useTimeNanos);
            taskInfo.setTimeNanos(useTimeNanos);
            if (this.keepTaskList) {
                this.taskList.add(taskInfo);
                this.taskCount++;
            }
            taskMap.remove(key);
            return;
        }
        throw new IllegalStateException("Can't stop StopWatch: it's not running");
    }

    public TaskInfo[] getTaskInfo() {
        if (!this.keepTaskList) {
            return new ArrayList<TaskInfo>().toArray(new TaskInfo[0]);
        }
        return this.taskList.toArray(new TaskInfo[0]);
    }

    public String shortSummary() {
        if (!openStopWatchFlag) {
            return "concurrentStopWatch is close";
        }
        return "ConcurrentStopWatch '" + getId() + "': threads running total Time = " + getTotalTimeNanos() / getTimeConversion() + " " + timeUnitStr;
    }

    public String prettyPrint() {
        if (!openStopWatchFlag) {
            return "concurrentStopWatch is close";
        }
        StringBuilder sb = new StringBuilder(shortSummary());
        sb.append('\n');
        if (!this.keepTaskList) {
            sb.append("No task info kept");
        } else {
            sb.append("---------------------------------------------\n");
            sb.append(getTimeUnitStr()).append("         %     Task name\n");
            sb.append("---------------------------------------------\n");
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(3);
            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);
            for (TaskInfo task : getTaskInfo()) {
                sb.append(nf.format(task.getTimeNanos() / getTimeConversion())).append("  ");
                sb.append(pf.format((double) task.getTimeNanos() / getTotalTimeNanos())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
            sb.append("---------------------------------------------\n");
            sb.append("really use time = ").append((this.getEndTimeNanos() - this.getStartTimeNanos()) / getTimeConversion()).append(" ").append(timeUnitStr);
        }
        return sb.toString();
    }

    /**
     * Generate an informative string describing all tasks performed
     * <p>For custom reporting, call {@link #getTaskInfo()} and use the task info
     * directly.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(shortSummary());
        if (this.keepTaskList) {
            for (TaskInfo task : getTaskInfo()) {
                sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeNanos()).append(" ").append(timeUnitStr);
                long percent = Math.round(100.0 * task.getTimeNanos() / getTotalTimeNanos());
                sb.append(" = ").append(percent).append("%");
            }
        } else {
            sb.append("; no task info kept");
        }
        return sb.toString();
    }


    private long getStartTimeNanos() {
        return startTimeNanos;
    }

    private long getEndTimeNanos() {
        return endTimeNanos;
    }


    private long getTimeConversion() {
        return timeConversion;
    }

    private String getTimeUnitStr() {
        return timeUnitStr;
    }

    public long getTotalTimeNanos() {
        return this.totalTimeNanos.get();
    }

    public long getTotalTimeMillis() {
        return nanosToMillis(this.totalTimeNanos.get());
    }

    public double getTotalTimeSeconds() {
        return nanosToSeconds(this.totalTimeNanos.get());
    }

    public int getTaskCount() {
        return this.taskCount;
    }

    public String getId() {
        return this.id;
    }

    private static long nanosToMillis(long duration) {
        return TimeUnit.NANOSECONDS.toMillis(duration);
    }

    private static double nanosToSeconds(long duration) {
        return duration / 1_000_000_000.0;
    }

    public TimeUnit getTimeUnit() {
        return this.timeUnit;
    }


    /**
     * Nested class to hold data about one task executed within the {@code StopWatch}.
     */
    public static final class TaskInfo {

        private final String taskName;

        private long timeNanos;

        private final long startTimeNanos;

        TaskInfo(String taskName, long startTimeNanos) {
            this.taskName = taskName;
            this.startTimeNanos = startTimeNanos;
        }

        public void setTimeNanos(long timeNanos) {
            this.timeNanos = timeNanos;
        }

        /**
         * Get the name of this task.
         */
        public String getTaskName() {
            return this.taskName;
        }

        /**
         * Get the time in nanoseconds this task took.
         *
         * @see #getTimeMillis()
         * @see #getTimeSeconds()
         * @since 5.2
         */
        public long getTimeNanos() {
            return this.timeNanos;
        }

        /**
         * Get the time in milliseconds this task took.
         *
         * @see #getTimeNanos()
         * @see #getTimeSeconds()
         */
        public long getTimeMillis() {
            return nanosToMillis(this.timeNanos);
        }

        /**
         * Get the time in seconds this task took.
         *
         * @see #getTimeMillis()
         * @see #getTimeNanos()
         */
        public double getTimeSeconds() {
            return nanosToSeconds(this.timeNanos);
        }
    }

}

