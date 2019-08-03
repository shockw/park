<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>业务操作日志查看</title>
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
				${parkBizoptLog.bizoptName}
			</div>
		</div>
		<div class="control-group">
            <label class="control-label">订单编号：</label>
            <div class="controls">
                 ${parkBizoptLog.orderId}
            </div>
        </div>
		<div class="control-group">
			<label class="control-label">用户id：</label>
			<div class="controls">
				${parkBizoptLog.sysUserId}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户名：</label>
			<div class="controls">
				${parkBizoptLog.sysUserLoginName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">操作时间：</label>
			<div class="controls">
				<fmt:formatDate value="${parkBizoptLog.bizoptTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">操作内容：</label>
			<div class="controls">
				${parkBizoptLog.bizoptContent}
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div> --%>
	</form:form>
</body>
</html>