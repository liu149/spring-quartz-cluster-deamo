package com.quartz.cluster.service.impl;

import com.quartz.cluster.dao.ScheduleJobDao;
import com.quartz.cluster.model.ScheduleJob;
import com.quartz.cluster.service.ScheduleJobService;
import com.quartz.cluster.utils.ScheduleUtils;
import com.quartz.cluster.vo.ScheduleJobVo;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liuqi
 * createTime : 2018-08-18
 * description : 定时任务调度
 * version : 1.0
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {

    /** 调度工程Bean */
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobDao scheduleJobDao;

    @Override
    public void initScheduleJob() {
        List<ScheduleJobVo> scheduleJobVoList = scheduleJobDao.quaryList();
        if(CollectionUtils.isEmpty(scheduleJobVoList)){
            return;
        }
        for(ScheduleJobVo scheduleJobVo : scheduleJobVoList){
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler,scheduleJobVo.getJobName(),
                    scheduleJobVo.getJobGroup());

            if(cronTrigger == null){
                ScheduleUtils.createScheduleJob(scheduler,scheduleJobVo);
            }else{
                ScheduleUtils.updateScheduleJob(scheduler,scheduleJobVo);
            }
        }
    }

    @Override
    public Long insert(ScheduleJobVo scheduleJobVo) {
        ScheduleUtils.createScheduleJob(scheduler,scheduleJobVo);
        return scheduleJobDao.insert(scheduleJobVo);
    }

    @Override
    public void update(ScheduleJobVo scheduleJobVo) {
        ScheduleUtils.updateScheduleJob(scheduler,scheduleJobVo);
        scheduleJobDao.update(scheduleJobVo);
    }

    @Override
    public void delUpdate(ScheduleJobVo scheduleJobVo) {

    }

    /**
     *  删除任务
     * @param scheduleJobId
     */
    @Override
    public void delete(Long scheduleJobId) {
        ScheduleJobVo scheduleJobVo = scheduleJobDao.quaryScheduleJobById(scheduleJobId);
        // 删除运行的任务
        ScheduleUtils.deleteScheduleJob(scheduler,scheduleJobVo.getJobName(),scheduleJobVo.getJobGroup());
        // 删除运行的数据
        scheduleJobDao.deleteScheduleJobVoById(scheduleJobId);
    }

    @Override
    public void runOnce(Long scheduleJobId) {

    }

    @Override
    public void pauseJob(Long scheduleJobId) {

    }

    @Override
    public void resumeJob(Long scheduleJobId) {

    }

    @Override
    public List<ScheduleJobVo> queryList() {
        List<ScheduleJobVo> scheduleJobVoList = scheduleJobDao.quaryList();

        for(ScheduleJobVo vo : scheduleJobVoList){
            JobKey jobKey = ScheduleUtils.getJobKey(vo.getJobName(),vo.getJobGroup());
            try {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if(CollectionUtils.isEmpty(triggers)){
                    continue;
                }
                //一个任务可以有多个触发器，这里默认选择第一个触发器
                Trigger trigger = triggers.iterator().next();
                vo.setJobTrigger(trigger.getKey().getName());

                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                vo.setStatus(triggerState.name());

                if(trigger instanceof CronTrigger){
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    vo.setCronExpression(cronExpression);
                }

            } catch (SchedulerException e) {
                //暂不做处理
            }
        }
        return scheduleJobVoList;
    }

    /**
     *  获取运行中的job列表
     * @return
     */
    @Override
    public List<ScheduleJobVo> queryExecutingJobList() {
        try{
            List<ScheduleJobVo> excutingJobList = new ArrayList<ScheduleJobVo>();

            // 获取scheduler中的jobGroupName
            for(String jobGroupName : scheduler.getJobGroupNames()){
                // 获取jobkey，遍历jobkey
                for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(jobGroupName))){
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    JobDataMap jobDataMap = jobDetail.getJobDataMap();
                    ScheduleJobVo scheduleJobVo = (ScheduleJobVo) jobDataMap.get(ScheduleJobVo.JOB_PARAM_KEY);

                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    Trigger trigger = triggers.iterator().next();
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    scheduleJobVo.setJobTrigger(trigger.getKey().getName());
                    scheduleJobVo.setStatus(triggerState.name());

                    // 获取正常运行的任务列表
                    if(triggerState.name().equals("NORMAL")){
                        excutingJobList.add(scheduleJobVo);
                    }
                }
            }
            return excutingJobList;
        }catch(SchedulerException e){
            // 暂不做处理
            return null;
        }
    }

    /**
     *  根据
     * @param scheduleJobId
     * @return
     */
    @Override
    public ScheduleJobVo quaryScheduleJobVoById(Long scheduleJobId){
        return scheduleJobDao.quaryScheduleJobById(scheduleJobId);
    }
}
