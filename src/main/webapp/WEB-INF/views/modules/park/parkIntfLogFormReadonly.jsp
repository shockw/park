<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口日志查看</title>
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
			
			placeholder = '请求消息应包括请求URL、请求Header、请求报文体';
            $("#reqMsg").attr('placeholder', placeholder);
            placeholder = '包括：软件、入门门禁、出门门禁、车架';
            $("#caller").attr('placeholder', placeholder);
            placeholder = '包括：软件、入门门禁、出门门禁、车架';
            $("#callee").attr('placeholder', placeholder);
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/park/parkIntfLog/">接口日志列表</a></li>
		<li class="active"><a href="${ctx}/park/parkIntfLog/form?id=${parkIntfLog.id}">接口日志<shiro:hasPermission name="park:parkIntfLog:edit">${not empty parkIntfLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="park:parkIntfLog:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="parkIntfLog" action="${ctx}/park/parkIntfLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">接口名：</label>
			<div class="controls">
				${parkIntfLog.intfName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">调用方式：</label>
			<div class="controls">
			     ${fns:getDictLabel(parkIntfLog.callMethod, 'park_intf_call_method', '')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">订单编号：</label>
			<div class="controls">
			     ${parkIntfLog.orderId}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">调用方：</label>
			<div class="controls">
			     ${parkIntfLog.caller}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">被调用方：</label>
			<div class="controls">
				${parkIntfLog.callee}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请求时间：</label>
			<div class="controls">
				<fmt:formatDate value="${parkIntfLog.reqTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">响应时间：</label>
			<div class="controls">
				<fmt:formatDate value="${parkIntfLog.rspTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">调用状态：</label>
			<div class="controls">
				${fns:getDictLabel(parkIntfLog.callStatus, 'park_intf_call_status', '')}
			</div>
		</div>
		<div class="control-group">
            <label class="control-label">请求消息：</label>
            <div class="controls">
                ${parkIntfLog.reqMsg}
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">响应消息：</label>
            <div class="controls">
                ${parkIntfLog.rspMsg}
            </div>
        </div>
		<%-- <div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div> --%>
		<%-- <div class="form-actions">
			<shiro:hasPermission name="park:parkIntfLog:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div> --%>
	</form:form>
</body>
</html>