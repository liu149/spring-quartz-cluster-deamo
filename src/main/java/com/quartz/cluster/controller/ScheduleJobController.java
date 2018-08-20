package com.quartz.cluster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quartz.cluster.dao.ScheduleJobDao;
import com.quartz.cluster.service.ScheduleJobService;
import com.quartz.cluster.vo.ScheduleJobVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ScheduleJobController {

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ScheduleJobService scheduleJobService;

    @Autowired
    ScheduleJobDao scheduleJobDao;

    /**
     *  任务页面
     * @return
     */
    @RequestMapping(value = "input-schedule-job",method = RequestMethod.GET)
    public ModelAndView inputScheduleJob(ScheduleJobVo scheduleJobVo) throws JsonProcessingException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("input-schedule-job");

        ScheduleJobVo vo = new ScheduleJobVo();
        if(scheduleJobVo.getScheduleJobId() != null){
            vo = scheduleJobService.quaryScheduleJobVoById(scheduleJobVo.getScheduleJobId());
        }
        mv.addObject("scheduleJob",mapper.writeValueAsString(vo));
        return mv;
    }

    /**
     *  任务列表页
     * @return
     */
    @RequestMapping(value = "list-schedule-job",method = RequestMethod.GET)
    public ModelAndView listScheduleJob(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("list-schedule-job");
        // 所有任务
        List<ScheduleJobVo> scheduleJobVoList = scheduleJobService.queryList();

        // 运行中的任务
        List<ScheduleJobVo> excutingJobList = scheduleJobService.queryExecutingJobList();

//        ObjectMapper mapper = new ObjectMapper();
        String scheduleJobJson = "";
        String excutingJobJson = "";
        try {
            scheduleJobJson = mapper.writeValueAsString(scheduleJobVoList);
            excutingJobJson = mapper.writeValueAsString(excutingJobList);
        } catch (JsonProcessingException e) {
            //
        }

        mv.addObject("scheduleJobList",scheduleJobJson);
        mv.addObject("excutingJobList",excutingJobJson);
        return mv;
    }

    /**
     *  删除任务
     * @param scheduleJobId
     * @return
     */
    @RequestMapping(value = "delete-schedule-job",method = RequestMethod.GET)
    public String deleteScheduleJob(Long scheduleJobId){
        scheduleJobService.delete(scheduleJobId);
        return "redirect:list-schedule-job";
    }

    /**
     *  运行一次任务
     * @param scheduleJobId
     * @return
     */
    @RequestMapping(value = "run-once-schedule-job",method = RequestMethod.GET)
    public String runOnceScheduleJob(Long scheduleJobId){
        scheduleJobService.runOnce(scheduleJobId);
        return "redirect:list-schedule-job";
    }

    /**
     *  暂停任务
     * @return
     */
    @RequestMapping(value = "pause-schedule-job",method = RequestMethod.GET)
    public String pauseScheduleJob(Long scheduleJobId){
        scheduleJobService.pauseJob(scheduleJobId);
        return "redirect:list-schedule-job";
    }

    /**
     *  恢复(un-pause)任务
     * @return
     */
    @RequestMapping(value = "resumt-schedule-job",method = RequestMethod.GET)
    public String resumeScheduleJob(Long scheduleJobId){
        scheduleJobService.resumeJob(scheduleJobId);
        return "redirect:list-schedule-job";
    }

    /**
     *  保存任务
     * @param scheduleJobVo
     * @return
     */
    @RequestMapping(value = "save-schedule-job",method = RequestMethod.POST)
    public String saveScheduleJob(ScheduleJobVo scheduleJobVo){
        // 测试随便设置一个状态
        scheduleJobVo.setStatus("1");

        if(scheduleJobVo.getScheduleJobId() == null){
            scheduleJobService.insert(scheduleJobVo);
        }else{
            scheduleJobService.update(scheduleJobVo);
        }
        return "redirect:list-schedule-job";
    }


}
