<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>停车订单管理</title>
<meta name="decorator" content="default" />
<meta http-equiv="Content-Type"
	content="multipart/form-data;charset=utf-8" />
<script src="${ctxStatic}/websocket/sockjs.min.js"></script>
<script src="${ctxStatic}/websocket/stomp.min.js"></script>
<script type="text/javascript">
	$(function() {
		var socket = new SockJS('/park/socket');
		var stompClient = Stomp.over(socket);
		console.log("test")
		stompClient.connect({}, function(frame) {
			stompClient.subscribe('/topic/bill', function(message) {
				var str = message.body;
				if('closed'==$.trim(str)){
					$("#billInfo").html('');
				}else{
					var obj = eval('(' + str + ')');
					var personId = obj.personId;
					var floor = obj.floor;
					var startTime = obj.startTime;
					var endTime = obj.endTime;
					var cost = obj.cost;
					var  HTMLbank='<div align="center" style="font-size:32px">用户id：' +personId+
					   '</div><p></p><div align="center" style="font-size:32px">停车楼层：'+floor+
					   '</div><p></p><div align="center" style="font-size:32px">开始时间：'+startTime+
					   '</div><p></p><div align="center" style="font-size:32px">结束时间：'+endTime+
					   '</div><p></p><div align="center" style="font-size:32px">花费：'+cost+
					   '</div><p></p>';
					$("#billInfo").html(HTMLbank);
				}
				
			});
		});
	})
	$(document).ready(
			function() {
				$("#payCode").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/park/parkOrder/pay">停车收费</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="parkOrder"
		action="${ctx}/park/parkOrder/bill" method="post"
		class="form-horizontal" enctype="multipart/form-data">
		<div class="control-group">
			<label class="control-label">请出示微信付款码：</label>
			<div class="controls">
				<input id="payCode" name="payCode" maxlength="18"
					class="input-xlarge required"
					oninput="value=value.replace(/[^\d]/g,'')" /> <span
					class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="park:parkOrder:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
	
	<p></p>
	<font size="6" color="red">待付款订单信息：</font>
	<div id = "billInfo">
	</div>
</body>
</html>