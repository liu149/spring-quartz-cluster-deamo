<%--
  Created by IntelliJ IDEA.
  User: liuqi
  Date: 2018/8/18
  Time: 下午7:02
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

<a href="input-schedule-job.shtml" class="btn btn-success" role="button">添加任务</a>
<table class="table table-bordered table-margin-top" id="scheduleJobList">
    <tr>
        <td>任务名称</td>
        <td>任务别名</td>
        <td>任务分组</td>
        <td>触发器</td>
        <td>任务状态</td>
        <td>时间表达式</td>
        <td>是否异步</td>
        <td>任务执行url</td>
        <td>任务描述</td>
        <td>操作</td>
    </tr>
    <tr>
        <td>$!{item.jobName}</td>
        <td>$!{item.aliasName}</td>
        <td>$!{item.jobGroup}</td>
        <td>$!{item.jobTrigger}</td>
        #if ($!{item.status}=="PAUSED")
        <td class="status-color">$!{item.status}</td>
        #else
        <td>$!{item.status}</td>
        #end
        <td>$!{item.cronExpression}</td>
        <td>
            #if ($!{item.isSync})
            异步
            #else
            同步
            #end
        </td>
        <td>$!{item.url}</td>
        <td>$!{item.description}</td>
        <td>
            <!--<a href="input-schedule-job.shtml?scheduleJobId=$!{item.scheduleJobId}">修改</a>-->
            <a class="btn btn-danger" role="button"
               href="pause-schedule-job.shtml?scheduleJobId=$!{item.scheduleJobId}"><i class="fa fa-pause-circle-o" aria-hidden="true"></i>暂停</a>
            <a class="btn btn-danger" role="button"
               href="resume-schedule-job.shtml?scheduleJobId=$!{item.scheduleJobId}"><i class="fa fa-reply" aria-hidden="true"></i>恢复</a>
            <a class="btn btn-danger" role="button"
               href="run-once-schedule-job.shtml?scheduleJobId=$!{item.scheduleJobId}"><i class="fa fa-play" aria-hidden="true"></i>运行一次</a>
            <a class="btn btn-danger" role="button"
               href="input-schedule-job.shtml?scheduleJobId=$!{item.scheduleJobId}&keywords=delUpdate"><i class="fa fa-pencil" aria-hidden="true"></i>修改</a>
            <a class="btn btn-danger" role="button"
               href="delete-schedule-job.shtml?scheduleJobId=$!{item.scheduleJobId}"><i class="fa fa-trash" aria-hidden="true"></i>删除</a>
        </td>
    </tr>
</table>
<p class="lead">运行中的任务</p>
<table class="table table-bordered">
    <tr>
        <td>任务名称</td>
        <td>任务别名</td>
        <td>任务分组</td>
        <td>触发器</td>
        <td>任务状态</td>
        <td>时间表达式</td>
        <td>是否异步</td>
        <td>任务执行url</td>
        <td>任务描述</td>
    </tr>
    <tr>
        <td>$!{item.jobName}</td>
        <td>$!{item.aliasName}</td>
        <td>$!{item.jobGroup}</td>
        <td>$!{item.jobTrigger}</td>
        <td>$!{item.status}</td>
        <td>$!{item.cronExpression}</td>
        <td>$!{item.isSync}</td>
        <td>$!{item.url}</td>
        <td>$!{item.description}</td>
    </tr>
</table>

<footer class="footer">
    <div class="container">
        <p class="text-muted">Quartz</p>
    </div>
</footer>
</body>
<script>
    $(function(){
        var scheduleJobList = '${scheduleJobList}';
        console.log(scheduleJobList.get(0));
        var app = new Vue({
            el: '#scheduleJobList',
            data: {
                message: 'Hello Vue!'
            }
        })
    });
</script>
</html>
