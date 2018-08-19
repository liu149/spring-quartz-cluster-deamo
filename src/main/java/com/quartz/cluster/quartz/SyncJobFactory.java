package com.quartz.cluster.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * author : liuqi
 * createTime : 2018-08-19
 * description : 同步任务工厂
 * version : 1.0
 */
public class SyncJobFactory extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
