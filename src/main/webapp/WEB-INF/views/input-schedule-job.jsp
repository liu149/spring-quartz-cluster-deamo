<%--
  Created by IntelliJ IDEA.
  User: liuqi
  Date: 2018/8/20
  Time: 下午2:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>分布式定时任务</title>

    <%--bootstrap--%>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
            crossorigin="anonymous"></script>
    <script src="https://cdn.bootcss.com/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
            crossorigin="anonymous"></script>
    <script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
</head>
<body>
<div class="container" role="main">
    <nav class="navbar navbar-light" style="background-color: #e3f2fd;">
        <a href="list-schedule-job">基于Quartz的分布式定时任务</a>
    </nav>
    <div class="jumbotron">
        <h1>Spring Quartz分布式定时任务</h1>
        <p>Spring整合Quartz基于数据库的分布式定时任务，可动态添加、删除、修改定时任务。</p>
    </div>
</div>

<form action="save-schedule-job" method="post" class="form-horizontal" id="scheduleJobForm">
    <input type="hidden" name="scheduleJobId" :value="scheduleJobVo.scheduleJobId">
    <input type="hidden" name="keywords" :value="scheduleJobVo.keywords">

    <div class="form-group">
        <label for="jobName" class="col-sm-2 control-label">任务名称</label>
        <div class="col-sm-8">
            <input type="text" id="jobName" name="jobName" :value="scheduleJobVo.jobName" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <label for="jobGroup" class="col-sm-2 control-label">任务分组</label>
        <div class="col-sm-8">
            <input type="text" id="jobGroup" name="jobGroup" :value="scheduleJobVo.jobGroup" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <label for="aliasName" class="col-sm-2 control-label">任务别名</label>
        <div class="col-sm-8">
            <input type="text" id="aliasName" name="aliasName" :value="scheduleJobVo.aliasName" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <label for="cronExpression" class="col-sm-2 control-label">时间表达式</label>
        <div class="col-sm-8">
            <input type="text" id="cronExpression" name="cronExpression" :value="scheduleJobVo.cronExpression" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <label for="isSync" class="col-sm-2 control-label">是否异步</label>
        <div class="col-sm-8">
            <input type="radio" name="isSync" id="isSync" value="false"/>同步
            <%--<input type="radio" name="isSync" value="true" #if ($!{scheduleJobVo.isSync}=="true") checked #end/>异步--%>
        </div>
    </div>

    <div class="form-group">
        <label for="url" class="col-sm-2 control-label">任务执行url</label>
        <div class="col-sm-8">
            <input type="text" id="url" name="url" :value="scheduleJobVo.url" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <label for="description" class="col-sm-2 control-label">任务描述</label>
        <div class="col-sm-8">
            <input type="text" id="description" name="description" :value="scheduleJobVo.description" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary">保存</button>
            <a href="list-schedule-job" class="btn btn-default" role="button">返回</a>
        </div>
    </div>
</form>
<footer class="footer">
    <div class="container">
        <p class="text-muted">Quartz</p>
    </div>
</footer>
</body>

<script>
   $(function(){
       var scheduleJobVo = $.parseJSON('${scheduleJob}');
       console.log(scheduleJobVo);
       var app = new Vue({
           el: '#scheduleJobForm',
           data: {
               message: 'Hello Vue!',
               scheduleJobVo : scheduleJobVo
           }
       })
   });
</script>
</html>
