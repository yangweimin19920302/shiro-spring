<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <script src="<%=basePath%>static/jquery-1.12.3.min.js"></script>
</head>
<body>
<h1>${message}</h1>
<shiro:hasPermission name="user:edit">
    <button onclick="get()">有权限</button>
</shiro:hasPermission>
<button onclick="add()">无权限</button>
<a href="/test">测试页面</a>
<shiro:hasPermission name="user:add">
    12345
</shiro:hasPermission>
<a href="/loginout" style="float: right">退出</a>
<script>
    $(function() {
        /**
         * $.ajaxSetup相当于$.ajax的一个切面，如果对$.ajaxSetup进行了定义（用法跟$.ajax一样），则可以直接$.ajax();这样调用ajax
         * 但$.ajax()使用的配置都是$.ajaxSetup的，如果对$.ajax()进行自定义，则已$.ajax()的为准，没有定义就默认使用$.ajaxSetup的配置
         */
        $.ajaxSetup({
            //complete()函数是ajax请求完成后运行的函数http://www.runoob.com/jquery/jquery-ref-ajax.html
            complete:function(data){
                var dat = data.responseJSON;
                switch (dat.code) {
                    case 300 :
                        window.location.href = "/login";
                        break;
                    case 500 :
                        alert(dat.data);
                }
            },
            error: function(){ // 出错时默认的处理函数
                alert("系统异常");
            }
        } );

    })
    function get() {
        $.ajax({
            url: "get",
            type: "GET",
            dataType: "json",
            cache: false,
            async: false,// async: false先执行完ajax，在执行ajax后面的语句，(async:true，分两个线程走，执行ajax的同时，回调去执行后面的语句)
            success: function (data) {
                if (data.status) {
                    alert(data.data);
                }//这里已经把错误的信息交由$.ajaxSetup()的complete()方法处理
            }
        });
    }

    function add() {
        $.ajax({
            url: "add",
            type: "GET",
            dataType: "json",
            cache: false,
            async: true,
            success: function (data) {
                if (data.status) {
                    alert(data.data);
                }
            }
        });
    }
</script>
</body>
</html>