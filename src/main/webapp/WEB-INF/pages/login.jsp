<%--
  Created by IntelliJ IDEA.
  Date: 2016/11/14
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title></title>
</head>
<body>
登录名：<input type="text" style="width: 100px" id="name"><br>
密码：<input type="password" style="width: 100px" id="password"><br>
<button onclick="login()">登录</button>
<br>
<script src="static/jquery-1.12.3.min.js"></script>
<script>
    function login() {
        $.ajax({
            url: "initlogin",
            type: "GET",
            data: {
                name: $("#name").val(),
                password: $("#password").val()
            },
            dataType: "json",
            cache: false,
            async: true,
            success: function (data) {
                if (data.status && data.data == null) {
                    window.location.href = "welcome";
                } else if (data.status && data.data != null){
                    window.location.href = data.data;
                } else {
                    alert(data.data);
                }
            }
        });
    }
</script>
</body>
</html>
