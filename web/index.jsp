<%--
  Created by IntelliJ IDEA.
  User: 李奇
  Date: 2017/12/15
  Time: 20:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>FCN</title>
  <script src="jquery-1.3.1.js"></script>
  <script src="jquery-form.js"></script>
  <script type="text/javascript">
       $(document).ready(function () {
            $("#file_upload").change(function () {
                var fil = this.files[0]
                if(!/image\/\w+/.test(fil.type)){
                    alert("请确保文件为图像类型");
                    return false;
                }
            });
           $("#upbutton").click(function () {
               var option = {
                   type:"post",
                   url:"Servlet",
                   async: false,
                  // data:$("#MyForm").submit(),
                   enctype:"multipart/form-data",
                   dataType:"json",
                   error:function(data){
                       alert(data);
                   },
                   success:function(data){
                       $("#span_img").html(
                           "<p>" + "原图:" + "</p>" + "</br>" +
                           "<img src=\"" + data.file[0].f1path + "\"width='60%'height='240px'/>" +
                           "<p>" + "结果:" + "</p>" + "</br>" +
                           "<img src=\"" + data.file[0].f2path + "\"width='60%'height='240px'/>"
                       )
                   }
               };
               $("#MyForm").ajaxSubmit(option)
           })

       });
  </script>
</head>
<body>
<form action="" method="POST"  Enctype="multipart/form-data"id="MyForm">
  <div align="center"><br/>
    <fieldset style="width:60%">
      <legend>上传图片</legend><br/>
      <div class='line'>
        <div align='left' class="leftDiv">上传图片</div>
        <div align='left' class="rightDiv">
          <input type="file" name="file1" class="text" id="file_upload">
        </div>
      </div>
      <div align='left' class="rightDiv"><br/>
        <input type="button" value=" 提交  " id="upbutton">
      </div></br>
        <span id="span_img">
      </span>
    </fieldset>
  </div>
</form>
</body>
</html>
