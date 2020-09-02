<%--
  Created by IntelliJ IDEA.
  User: LightseaBlue
  Date: 2020/2/7
  Time: 12:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>网盘</title>
</head>
<script src="js/jquery-3.1.0.min.js"></script>
<script>
    function show(path) {
        $('#backList').attr("onclick", "goUp('" + path + "')");
        $('#thisPath').html(path);
        $('#hid').attr("value", path);
        $.post("main.action", {
            op: "showAll",
            path: path
        }, function (data) {
            setTable(data, path);
        })
    }

    $(function () {
        show("/");
    });

    function goUp(path) {
        $.post("main.action", {
            op: "goUp",
            path: path
        }, function (data) {
            $('#backList').attr("onclick", "goUp('" + data.parentPath + "')");
            $('#thisPath').html(data.parentPath);
            $('#hid').attr("value", data.parentPath);
            setTable(data.list, path);
        })
    }

    function rename(FilePath, path) {
        var newName = prompt("请输入您要修改的名字:");
        if (newName.trim() == "") {
            alert("文件名不能为空...");
        } else {
            var newFilePath = path + newName;
            $.post("main.action", {
                op: "reName",
                newFilePath: newFilePath,
                filePath: FilePath
            }, function (data) {
                if (data) {
                    show(path);
                } else {
                    alert("失败请重试...")
                }
            })
        }
    }

    function deleteFile(Filepath, path) {
        $.post("main.action", {
            op: "deleteFile",
            filePath: Filepath
        }, function (data) {
            if (data) {
                show(path);
            } else {
                alert("请重试...");
            }
        })
    }

    function creatFile() {
        var FileName = prompt("请输入新建目录名：");
        if (FileName.trim() == "") {
            alert("目录名不能为空...");
            return;
        }
        var path = $('#thisPath').text();
        var newFileName = path + FileName;
        $.post("main.action", {
            op: "creatFile",
            newFileName: newFileName
        }, function (data) {
            if (data) {
                show(path);
            } else {
                alert("请重试...");
            }
        })
    }

    function moveFile(filepath, path) {
        var FileName = prompt("请输入移动的位置：");
        if (FileName.trim() == "") {
            alert("不能为空...");
            return;
        }
        $.post("main.action", {
            op: "moveFile",
            filePath: filepath,
            newFilePath: FileName
        }, function (data) {
            if (data) {
                show(path);
            } else {
                alert("请重试...");
            }
        })
    }

    function copyFile(filePath, path) {
        var FileName1 = prompt("请输入复制到的位置：");
        if (FileName1.trim() == "") {
            alert("不能为空...");
            return;
        }
        $.post("main.action", {
            op: "copyFile",
            filePath: filePath,
            newFilePath: FileName1
        }, function (data) {
            if (data) {
                show(path);
            } else {
                alert("请重试...");
            }
        })
    }
    
    function selectFile(i) {
        var path = $('#thisPath').text();
        $.post("main.action",{
            op:"selectFile",
            i:i
        },function (data) {
            setTable(data,path);
        })
    }

    function setTable(data, path) {
        $('#num').html(data.length);
        var x = '';
        for (var i = 0; i < data.length; i++) {
            var z = data[i];
            x += "<tr>" +
                "<td><a href='javascript:show(\"" + z.FilePath + "\")'>" + z.FileName + "</a></td>" +
                "<td>" + z.FileLen + "</td>" +
                "<td>" + z.FileTime + "</td>" +
                "<td>" +
                "<a href='javascript:rename(\"" + z.FilePath + "\",\"" + path + "\")'>重命名</a>  " +
                "<a href='javascript:deleteFile(\"" + z.FilePath + "\",\"" + path + "\")'>删除</a>  " +
                "<a href='javascript:moveFile(\"" + z.FilePath + "\",\"" + path + "\")'>移动</a> " +
                "<a href='javascript:copyFile(\"" + z.FilePath + "\",\"" + path + "\")'>复制到</a> " +
                "</td>" +
                "</tr>";
        }
        $('#tb').html(x);
    }
</script>
<body>
<form method="post" action="main.action?op=fileUpload" enctype="multipart/form-data">
    <input type="file" name="file">
    <input id="hid" name="hid" type="hidden" value="">
    <input type="submit" value="上传">
</form>
<button id="creatFile" onclick="creatFile()">新建目录</button>
<hr>
<button id="backList">返回上一级</button>
当前路径：<span id="thisPath"></span>
共<span id="num"></span>个
<hr>
<button onclick="selectFile(1)">图片</button>
<button onclick="selectFile(2)">文档</button>
<table border="1">
    <thead>
    <tr>
        <td>文件名</td>
        <td>文件大小</td>
        <td>创建时间</td>
        <td>操作</td>
    </tr>
    </thead>
    <tbody id="tb">

    </tbody>
</table>

</body>
</html>
