package com.quartz.exceptions;

/**
 * author : liuqi
 * createTime : 2018-08-19
 * description : 任务调度异常
 * version : 1.0
 */
public class ScheduleException extends RuntimeException{
    /**
     * Instantiates a new ScheduleException.
     *
     * @param e the e
     */
    public ScheduleException(Throwable e) {
        super(e);
    }

    /**
     * Constructor
     *
     * @param message the message
     */
    public ScheduleException(String message) {
        super(message);
    }
}
