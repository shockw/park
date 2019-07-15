<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借款流程管理</title>
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
		<li><a href="${ctx}/oa/oaLoan/">借款流程列表</a></li>
		<li class="active"><a href="${ctx}/oa/oaLoan/form?id=${oaLoan.id}">借款流程<shiro:hasPermission name="oa:oaLoan:edit">${not empty oaLoan.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="oa:oaLoan:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaLoan" action="${ctx}/oa/oaLoan/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">流程实例编号：</label>
			<div class="controls">
				<form:input path="procInsId" htmlEscape="false" maxlength="64" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户：</label>
			<div class="controls">
				<sys:treeselect id="user" name="user.id" value="${oaLoan.user.id}" labelName="user.name" labelValue="${oaLoan.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属部门：</label>
			<div class="controls">
				<sys:treeselect id="office" name="office.id" value="${oaLoan.office.id}" labelName="office.name" labelValue="${oaLoan.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">借款事由：</label>
			<div class="controls">
				<form:input path="summary" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">借款金额：</label>
			<div class="controls">
				<form:input path="fee" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">借款原因：</label>
			<div class="controls">
			    <form:textarea path="reason" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开户行：</label>
			<div class="controls">
				<form:input path="actbank" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账号：</label>
			<div class="controls">
				<form:input path="actno" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账号名：</label>
			<div class="controls">
				<form:input path="actname" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">财务预审：</label>
			<div class="controls"> ${financialText }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">部门领导意见：</label>
			<div class="controls"> ${leadText }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">总经理意见：</label>
			<div class="controls"> ${mainLeadText }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出纳支付：</label>
			<div class="controls"> ${teller }
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="oa:oaLoan:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>