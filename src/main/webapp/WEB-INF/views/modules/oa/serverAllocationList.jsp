<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>服务器分配流程管理</title>
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
		<li class="active"><a href="${ctx}/oa/serverAllocation/">服务器分配流程列表</a></li>
		<shiro:hasPermission name="oa:serverAllocation:edit"><li><a href="${ctx}/oa/serverAllocation/form">服务器分配流程添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="serverAllocation" action="${ctx}/oa/serverAllocation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>流程实例编号：</label>
				<form:input path="processInstanceId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>归属部门：</label>
				<sys:treeselect id="office" name="office.id" value="${serverAllocation.office.id}" labelName="office.name" labelValue="${serverAllocation.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>流程实例编号</th>
				<th>归属部门</th>
				<th>名称</th>
				<th>说明</th>
				<th>申请时间</th>
				<th>期望时间</th>
				<shiro:hasPermission name="oa:serverAllocation:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="serverAllocation">
			<tr>
				<td><a href="${ctx}/oa/serverAllocation/form?id=${serverAllocation.id}">
					${serverAllocation.processInstanceId}
				</a></td>
				<td>
					${serverAllocation.office.name}
				</td>
				<td>
					${serverAllocation.name}
				</td>
				<td>
					${serverAllocation.explain}
				</td>
				<td>
					<fmt:formatDate value="${serverAllocation.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${serverAllocation.expectedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="oa:serverAllocation:edit"><td>
    				<a href="${ctx}/oa/serverAllocation/form?id=${serverAllocation.id}">修改</a>
					<a href="${ctx}/oa/serverAllocation/delete?id=${serverAllocation.id}" onclick="return confirmx('确认要删除该服务器分配流程吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>