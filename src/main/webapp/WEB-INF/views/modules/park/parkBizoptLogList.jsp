<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>业务操作日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/park/parkBizoptLog/">业务操作日志列表</a></li>
		<shiro:hasPermission name="park:parkBizoptLog:edit"><li><a href="${ctx}/park/parkBizoptLog/form">业务操作日志模拟添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="parkBizoptLog" action="${ctx}/park/parkBizoptLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>操作名称：</label>
				<form:input path="bizoptName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>订单编号：</label>
                <form:input path="orderId" htmlEscape="false" maxlength="100" class="input-medium"/>
            </li>
			<li><label>系统用户id：</label>
				<form:input path="sysUserId" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>系统用户名：</label>
				<form:input path="sysUserLoginName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>操作时间：</label>
				<input name="beginBizoptTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${parkBizoptLog.beginBizoptTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endBizoptTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${parkBizoptLog.endBizoptTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>操作内容：</label>
				<form:input path="bizoptContent" htmlEscape="false" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>操作名称</th>
				<th>订单编号</th>
				<th>用户id</th>
				<th>用户名</th>
				<th>操作时间</th>
				<th>操作内容</th>
				<shiro:hasPermission name="park:parkBizoptLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="parkBizoptLog">
			<tr>
				<td><%-- <a href="${ctx}/park/parkBizoptLog/form?id=${parkBizoptLog.id}"> --%>
					${parkBizoptLog.bizoptName}
				<!-- </a> --></td>
				<td>
                    ${parkBizoptLog.orderId}
                </td>
				<td>
					${parkBizoptLog.sysUserId}
				</td>
				<td>
					${parkBizoptLog.sysUserLoginName}
				</td>
				<td>
					<fmt:formatDate value="${parkBizoptLog.bizoptTime}" pattern="MM-dd HH:mm:ss"/>
				</td>
				<td>
					${parkBizoptLog.bizoptContent}
				</td>
				<shiro:hasPermission name="park:parkBizoptLog:edit"><td>
    				<a href="${ctx}/park/parkBizoptLog/formreadonly?id=${parkBizoptLog.id}">查看</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>