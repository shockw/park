<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>指定层数车架落架构</title>
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

	<form:form id="inputForm" modelAttribute="parkJiffyStand"
		action="${ctx}/park/doorAccess/jiffyStandFallDown" method="post"
		class="form-horizontal">
		<div class="control-group">
			<label class="control-label">楼层：</label>
			<div class="controls">
				<form:select path="floor" class="input-xlarge required">
					<form:option value="" label="" />
					<form:options items="${fns:getDictList('park_floor')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="操作车架" />&nbsp;
		</div>
	</form:form>


</body>
</html>