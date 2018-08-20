package com.quartz.cluster.utils;

import com.quartz.cluster.model.ScheduleJob;
import com.quartz.cluster.quartz.AsyncJobFactory;
import com.quartz.cluster.quartz.SyncJobFactory;
import com.quartz.cluster.vo.ScheduleJobVo;
import com.quartz.exceptions.ScheduleException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author : liuqi
 * createTime : 2018-08-18
 * description : 任务调度工具类
 * version : 1.0
 */
public class ScheduleUtils {

    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleUtils.class);

    /**
     *  获取触发器key
     * @param jobName
     * @param jobGroup
     * @return
     */
    public static TriggerKey getTriggerKey(String jobName,String jobGroup){
        return TriggerKey.triggerKey(jobName,jobGroup);
    }

    /**
     *  获取表达式触发器
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @return
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler,String jobName,String jobGroup){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName,jobGroup);
            return (CronTrigger)scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            LOG.error("获取定时任务CronTrigger出现异常", e);
            throw new ScheduleException("获取定时任务CronTrigger出现异常");
        }
    }

    /**
     *  创建任务
     * @param scheduler
     * @param scheduleJobVo
     */
    public static void createScheduleJob(Scheduler scheduler,ScheduleJobVo scheduleJobVo){
        createScheduleJob(scheduler,scheduleJobVo.getJobName(),scheduleJobVo.getJobGroup()
                ,scheduleJobVo.getCronExpression(),scheduleJobVo.getIsSync(),scheduleJobVo);
    }

    /**
     *  创建定时任务
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @param cronExpression
     * @param isSync
     * @param param
     */
    public static void createScheduleJob(Scheduler scheduler,String jobName,String jobGroup,
                                         String cronExpression,boolean isSync,Object param){

        //同步或异步


        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob().withIdentity(jobName,jobGroup).build();

        //表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName,jobGroup)
                .withSchedule(cronScheduleBuilder).build();

        String jobTrigger = cronTrigger.getKey().getName();

        ScheduleJobVo ScheduleJobVo = (ScheduleJobVo) param;
        ScheduleJobVo.setJobTrigger(jobTrigger);

        //放入参数，运行时的方法可获取
        jobDetail.getJobDataMap().put(ScheduleJobVo.JOB_PARAM_KEY,ScheduleJobVo);

        try {
            scheduler.scheduleJob(jobDetail,cronTrigger);
        } catch (SchedulerException e) {
            LOG.error("创建定时任务失败",e);
            throw new ScheduleException("创建定时任务失败");
        }
    }


    /**
     *  运行一次任务
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void runOnce(Scheduler scheduler,String jobName,String jobGroup){
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("运行一次任务失败",e);
            throw new ScheduleException("运行一次任务失败");
        }
    }

    /**
     *  暂停任务
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void pauseJob(Scheduler scheduler,String jobName,String jobGroup){
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("暂停任务失败",e);
            throw new ScheduleException("暂停任务失败");
        }
    }

    /**
     *  恢复任务
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void resumeJob(Scheduler scheduler,String jobName,String jobGroup){
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("恢复任务失败",e);
            throw new ScheduleException("恢复任务失败");
        }
    }

    /**
     *  获取JobKey
     * @param jobName
     * @param jobGroup
     * @return
     */
    public static JobKey getJobKey(String jobName,String jobGroup){
        return JobKey.jobKey(jobName,jobGroup);
    }

    /**
     *  更新定时任务
     * @param scheduler
     * @param scheduleJobVo
     */
    public static void updateScheduleJob(Scheduler scheduler,ScheduleJobVo scheduleJobVo){
        updateScheduleJob(scheduler,scheduleJobVo.getJobName(),scheduleJobVo.getJobGroup(),
                scheduleJobVo.getCronExpression(),scheduleJobVo.getIsSync(),scheduleJobVo);
    }

    /**
     *  更新定时任务
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @param cronExpression
     * @param isSync
     * @param param
     */
    public static void updateScheduleJob(Scheduler scheduler,String jobName,String jobGroup,
                                         String cronExpression,boolean isSync,Object param){
        try {
            // 同步或异步
            Class<? extends Job> jobClass = isSync ? AsyncJobFactory.class : SyncJobFactory.class;

            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName,jobGroup));
            jobDetail = jobDetail.getJobBuilder().ofType(jobClass).build();

            // 更新参数
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            jobDataMap.put(ScheduleJobVo.JOB_PARAM_KEY,param);
            jobDetail.getJobBuilder().usingJobData(jobDataMap);

            TriggerKey triggerKey = ScheduleUtils.getTriggerKey(jobName,jobGroup);

            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            //按新的cronExpression重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

            // 排除暂停状态的，其他的按新的trigger重新设置job执行
            if(!triggerState.name().equalsIgnoreCase("PAUSE")){
                scheduler.rescheduleJob(triggerKey,trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     *  删除定时任务
     * @param scheduler
     * @param jobName
     * @param jobGroup
     */
    public static void deleteScheduleJob(Scheduler scheduler,String jobName,String jobGroup){
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("删除定时任务失败",e);
            throw new ScheduleException("删除定时任务失败");
        }
    }

}
