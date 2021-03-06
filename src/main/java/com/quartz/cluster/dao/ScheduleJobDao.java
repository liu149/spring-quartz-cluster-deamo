package com.quartz.cluster.dao;

import com.quartz.cluster.model.ScheduleJob;
import com.quartz.cluster.vo.ScheduleJobVo;

import java.util.List;

public interface ScheduleJobDao {

    /** 查询ScheduleJob列表 */
    public List<ScheduleJobVo> quaryList();

    /** 添加定时任务 */
    public Long insert(ScheduleJobVo scheduleJobVo);

    /** 更新定时任务 */
    public void update(ScheduleJobVo scheduleJobVo);

    /** 根据scheduleJobId查询 */
    public ScheduleJobVo quaryScheduleJobById(Long scheduleJobId);

    /** 根据scheduleJobId删除 */
    public void deleteScheduleJobVoById(Long scheduleJobId);
}
