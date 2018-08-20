package com.quartz.cluster.quartz;

import com.quartz.cluster.vo.ScheduleJobVo;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * author : liuqi
 * createTime : 2018-08-19
 * description : 同步任务工厂
 * version : 1.0
 */
public class SyncJobFactory extends QuartzJobBean {
    private  static final Logger LOG = LoggerFactory.getLogger(SyncJobFactory.class);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.info("AsyncJobFactory execute");
        ScheduleJobVo scheduleJobVo = (ScheduleJobVo) context.getMergedJobDataMap().get(ScheduleJobVo.JOB_PARAM_KEY);
        System.out.println("jobName:" + scheduleJobVo.getJobName() + "  " + scheduleJobVo);
    }
}
