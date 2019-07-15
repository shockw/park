<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>停车架管理</title>
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
		<li class="active"><a href="${ctx}/park/parkJiffyStand/">停车架列表</a></li>
		<shiro:hasPermission name="park:parkJiffyStand:edit"><li><a href="${ctx}/park/parkJiffyStand/form">停车架添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="parkJiffyStand" action="${ctx}/park/parkJiffyStand/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>楼层：</label>
				<form:select path="floor" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('park_floor')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>停车架：</label>
				<form:select path="jiffyStand" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('park_jiffy_stand')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>楼层</th>
				<th>停车架</th>
				<th>总量</th>
				<th>在用数量</th>
				<th>空闲数量</th>
				<shiro:hasPermission name="park:parkJiffyStand:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="parkJiffyStand">
			<tr>
				<td><a href="${ctx}/park/parkJiffyStand/form?id=${parkJiffyStand.id}">
					${fns:getDictLabel(parkJiffyStand.floor, 'park_floor', '')}
				</a></td>
				<td>
					${fns:getDictLabel(parkJiffyStand.jiffyStand, 'park_jiffy_stand', '')}
				</td>
				<td>
					${parkJiffyStand.count}
				</td>
				<td>
					${parkJiffyStand.inuseCount}
				</td>
				<td>
					${parkJiffyStand.idleCount}
				</td>
				<shiro:hasPermission name="park:parkJiffyStand:edit"><td>
    				<a href="${ctx}/park/parkJiffyStand/form?id=${parkJiffyStand.id}">修改</a>
					<a href="${ctx}/park/parkJiffyStand/delete?id=${parkJiffyStand.id}" onclick="return confirmx('确认要删除该停车架吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>