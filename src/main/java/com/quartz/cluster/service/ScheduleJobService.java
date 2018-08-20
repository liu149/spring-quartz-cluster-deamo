package com.quartz.cluster.service;

import com.quartz.cluster.vo.ScheduleJobVo;

import java.util.List;

public interface ScheduleJobService {

    /**
     * 初始化定时任务
     */
    public void initScheduleJob();

    /**
     * 新增
     *
     * @param scheduleJobVo
     * @return
     */
    public Long insert(ScheduleJobVo scheduleJobVo);

    /**
     * 直接修改 只能修改运行的时间，参数、同异步等无法修改
     *
     * @param scheduleJobVo
     */
    public void update(ScheduleJobVo scheduleJobVo);

    /**
     * 删除重新创建方式
     *
     * @param scheduleJobVo
     */
    public void delUpdate(ScheduleJobVo scheduleJobVo);

    /**
     * 删除
     *
     * @param scheduleJobId
     */
    public void delete(Long scheduleJobId);

    /**
     * 运行一次任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    public void runOnce(Long scheduleJobId);

    /**
     * 暂停任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    public void pauseJob(Long scheduleJobId);

    /**
     * 恢复任务
     *
     * @param scheduleJobId the schedule job id
     * @return
     */
    public void resumeJob(Long scheduleJobId);

    /**
     * 查询任务列表
     *
     * @return
     */
    public List<ScheduleJobVo> queryList();

    /**
     * 获取运行中的任务列表
     *
     * @return
     */
    public List<ScheduleJobVo> queryExecutingJobList();

    /**
     *  根据scheduleJobId查找
     * @param scheduleJobId
     * @return
     */
    public ScheduleJobVo quaryScheduleJobVoById(Long scheduleJobId);
}
