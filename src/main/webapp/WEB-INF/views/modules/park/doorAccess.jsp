<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>停车订单理管</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
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
	<sys:message content="${message}" />
	
	<form id="inputForm" action="${ctx}/park/doorAccess/inAccess" method="post"
		class="form-horizontal">
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-large btn-primary" type="submit"
				value="入口开门" />&nbsp;
		</div>
	</form>
	
	<form id="inputForm" action="${ctx}/park/doorAccess/outAccess" method="post"
		class="form-horizontal">
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-large btn-primary" type="submit"
				value="出口开门" />&nbsp;
		</div>
	</form>
	
	<form id="inputForm" action="${ctx}/park/doorAccess/jiffyStandReset" method="post"
		class="form-horizontal">
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-large btn-primary" type="submit"
				value="车架复位" />&nbsp;
		</div>
	</form>
</body>
</html>