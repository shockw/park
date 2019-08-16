<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>停车订单理管</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
function sumbit_sure(){
	var gnl=confirm("确定要操作?");
	if (gnl==true){
	return true;
	}else{
	return false;
	}
	}

</script>
</head>
<body>
	<p></p>
	<font size="6">当前令牌状态：</font>
	<font size="6" color="red">${statusDesc }</font>
	<sys:message content="${message}"/>
	<form id="inputForm" action="${ctx}/park/parkOrder/tokenOccupy" method="get"
		class="form-horizontal" onsubmit="return sumbit_sure();">
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-large btn-primary" type="submit"
				value="令牌占用" />&nbsp;
		</div>
	</form>
	
	<form id="inputForm" action="${ctx}/park/parkOrder/tokenRelease" method="get"
		class="form-horizontal" onsubmit="return sumbit_sure();">
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-large btn-primary" type="submit"
				value="令牌释放" />&nbsp;
		</div>
	</form>
	
	
</body>
</html>