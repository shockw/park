<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口日志管理</title>
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
		<li class="active"><a href="${ctx}/park/parkIntfLog/">接口日志列表</a></li>
		<shiro:hasPermission name="park:parkIntfLog:edit"><li><a href="${ctx}/park/parkIntfLog/form">接口日志模拟添加</a></li></shiro:hasPermission> 
	</ul>
	<form:form id="searchForm" modelAttribute="parkIntfLog" action="${ctx}/park/parkIntfLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>接口名：</label>
				<form:input path="intfName" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>调用方式：</label>
				<form:select path="callMethod" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('park_intf_call_method')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>订单编号：</label>
				<form:input path="orderId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>调用方：</label>
				<form:input path="caller" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>被调用方：</label>
				<form:input path="callee" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>请求时间：</label>
				<input name="beginReqTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${parkIntfLog.beginReqTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endReqTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${parkIntfLog.endReqTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>响应时间：</label>
				<input name="beginRspTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${parkIntfLog.beginRspTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endRspTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${parkIntfLog.endRspTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>调用状态：</label>
				<form:select path="callStatus" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('park_intf_call_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>接口名</th>
				<th>调用方式</th>
				<th>订单编号</th>
				<th>调用方</th>
				<th>被调用方</th>
				<th>请求时间</th>
				<th>响应时间</th>
				<th>调用状态</th>
				<shiro:hasPermission name="park:parkIntfLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="parkIntfLog">
			<tr>
				<td><a href="${ctx}/park/parkIntfLog/formreadonly?id=${parkIntfLog.id}">
					${parkIntfLog.intfName}
				</a></td>
				<td>
					${fns:getDictLabel(parkIntfLog.callMethod, 'park_intf_call_method', '')}
				</td>
				<td>
					${parkIntfLog.orderId}
				</td>
				<td>
					${parkIntfLog.caller}
				</td>
				<td>
					${parkIntfLog.callee}
				</td>
				<td>
					<fmt:formatDate value="${parkIntfLog.reqTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${parkIntfLog.rspTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(parkIntfLog.callStatus, 'park_intf_call_status', '')}
				</td>
				<shiro:hasPermission name="park:parkIntfLog:edit"><td>
    				<a href="${ctx}/park/parkIntfLog/formreadonly?id=${parkIntfLog.id}">查看</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>