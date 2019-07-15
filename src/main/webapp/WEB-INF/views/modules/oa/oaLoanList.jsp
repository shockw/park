<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借款流程管理</title>
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
		<li class="active"><a href="${ctx}/oa/oaLoan/">借款流程列表</a></li>
		<shiro:hasPermission name="oa:oaLoan:edit"><li><a href="${ctx}/oa/oaLoan/form">借款流程添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="oaLoan" action="${ctx}/oa/oaLoan/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>流程实例编号：</label>
				<form:input path="processInstanceId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${oaLoan.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
				<th>用户</th>
				<th>归属部门</th>
				<th>借款事由</th>
				<th>借款金额</th>
				<th>借款原因</th>
				<th>开户行</th>
				<th>账号</th>
				<th>账号名</th>
				<th>财务预审</th>
				<th>部门领导意见</th>
				<th>总经理意见</th>
				<th>出纳支付</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="oa:oaLoan:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaLoan">
			<tr>
				<td><a href="${ctx}/oa/oaLoan/form?id=${oaLoan.id}">
					${oaLoan.processInstanceId}
				</a></td>
				<td>
					${oaLoan.user.name}
				</td>
				<td>
					${oaLoan.office.name}
				</td>
				<td>
					${oaLoan.summary}
				</td>
				<td>
					${oaLoan.fee}
				</td>
				<td>
					${oaLoan.reason}
				</td>
				<td>
					${oaLoan.actbank}
				</td>
				<td>
					${oaLoan.actno}
				</td>
				<td>
					${oaLoan.actname}
				</td>
				<td>
					${oaLoan.financialText}
				</td>
				<td>
					${oaLoan.leadText}
				</td>
				<td>
					${oaLoan.mainLeadText}
				</td>
				<td>
					${oaLoan.teller}
				</td>
				<td>
					<fmt:formatDate value="${oaLoan.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${oaLoan.remarks}
				</td>
				<shiro:hasPermission name="oa:oaLoan:edit"><td>
    				<a href="${ctx}/oa/oaLoan/form?id=${oaLoan.id}">修改</a>
					<a href="${ctx}/oa/oaLoan/delete?id=${oaLoan.id}" onclick="return confirmx('确认要删除该借款流程吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>