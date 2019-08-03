<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>业务操作日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			placeholder = '修改订单状态操作内容：订单号,修改前状态,修改后状态,';
            $("#bizoptContent").attr('placeholder', placeholder);
            placeholder = '修改订单状态/修改订单金额/修改订单对应停车位...';
            $("#bizoptName").attr('placeholder', placeholder);
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/park/parkBizoptLog/">业务操作日志列表</a></li>
		<li class="active"><a href="${ctx}/park/parkBizoptLog/form?id=${parkBizoptLog.id}">业务操作日志<shiro:hasPermission name="park:parkBizoptLog:edit">${not empty parkBizoptLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="park:parkBizoptLog:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="parkBizoptLog" action="${ctx}/park/parkBizoptLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">操作名称：</label>
			<div class="controls">
				<form:input path="bizoptName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
            <label class="control-label">订单编号：</label>
            <div class="controls">
                <form:input path="orderId" htmlEscape="false" maxlength="100" class="input-xlarge "/>
            </div>
        </div>
		<div class="control-group">
			<label class="control-label">用户id：</label>
			<div class="controls">
				<form:input path="sysUserId" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户名：</label>
			<div class="controls">
				<form:input path="sysUserLoginName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">操作时间：</label>
			<div class="controls">
				<input name="bizoptTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${parkBizoptLog.bizoptTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">操作内容：</label>
			<div class="controls">
			    <form:textarea path="bizoptContent" htmlEscape="false" rows="5" class="input-xxlarge required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div> --%>
		<div class="form-actions">
			<shiro:hasPermission name="park:parkBizoptLog:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>