<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>停车订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#parkButton").click(function(){
				$.get("${ctx}/park/api/park",function(data,status){
					if(data.indexOf("true") > 0 ){
						alert("请到入口处进行人脸识别！" );
					}else{
						alert("已有人在存取车，请稍后再试！" );
					}
				});
			});
			
			$("#dataClean").click(function(){
				$.get("${ctx}/park/api/clean",function(data,status){
					if(data.indexOf("true") > 0 ){
						alert("门禁数据清理完毕！" );
					}else{
						alert("门禁数据清理失败！" );
					}
				});
			});
			
			$("#inAccess").click(function(){
				$.get("${ctx}/park/api/in",function(data,status){
					if(data.indexOf("true") > 0 ){
						alert("开门成功！" );
					}else{
						alert("开门失败！" );
					}
				});
			});
			$("#outAccess").click(function(){
				$.get("${ctx}/park/api/out",function(data,status){
					if(data.indexOf("true") > 0 ){
						alert("开门成功！" );
					}else{
						alert("开门失败！" );
					}
				});
			});
			/* $("#orderClean").click(function(){
				$.get("${ctx}/park/api/orderClean",function(data,status){
					if(data.indexOf("true") > 0 ){
						alert("在途订单清理成功！" );
					}else{
						alert("在途订单清理失败！" );
					}
				});
			}); */
			
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
		<li class="active"><a href="${ctx}/park/parkOrder/list">停车订单列表</a></li>
		<%-- <shiro:hasPermission name="park:parkOrder:edit"><li><a href="${ctx}/park/parkOrder/form">停车订单添加</a></li></shiro:hasPermission> --%>
	</ul>
	<button id="parkButton" style="width:100px;height:50px;font-size:20px;color:red" >我要停车</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<button id="dataClean" style="width:120px;height:50px;font-size:16px;color:red">门禁数据清理</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<button id="inAccess" style="width:100px;height:50px;font-size:20px;color:red">入口开门</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<button id="outAccess" style="width:100px;height:50px;font-size:20px;color:red">出口开门</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<!-- 	<button id="orderClean" style="width:100px;height:50px;font-size:20px;color:red">在途订单清理</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 -->	<p></p>
	<form:form id="searchForm" modelAttribute="parkOrder" action="${ctx}/park/parkOrder/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>楼层：</label>
				<form:select path="floor" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('park_floor')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('park_order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>用户id：</label>
				<form:input path="personId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>存车时间：</label>
				<input name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${parkOrder.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${parkOrder.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>楼层</th>
				<th>停车架</th>
				<th>用户id</th>
				<th>存车照片</th>
				<th>取车照片</th>
				<th>存车时间</th>
				<th>取车时间</th>
				<th>付款时间</th>
				<th>费用</th>
				<th>订单状态</th>
				<shiro:hasPermission name="park:parkOrder:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="parkOrder">
			<tr>
				<td><a href="${ctx}/park/parkOrder/form?id=${parkOrder.id}">
					${fns:getDictLabel(parkOrder.floor, 'park_floor', '')}
				</a></td>
				<td>
					${fns:getDictLabel(parkOrder.jiffyStand, 'park_jiffy_stand', '')}
				</td>
				<%-- <td>
				
					${parkOrder.personId}
				</td> --%>
				<td style="width:50px;">
					<div style="width:50px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;" title="${parkOrder.personId}">
						${parkOrder.personId}
					</div>
				</td>
				<td>
							<img src="data:image/png;base64,${parkOrder.inPic}" height="100" width="100"/>
				</td>
				<td>
							<img src="data:image/png;base64,${parkOrder.outPic}" height="100" width="100"/>
				</td>
				<td>
					<fmt:formatDate value="${parkOrder.startTime}" pattern="MM-dd HH:mm"/>
				</td>
				<td>
					<fmt:formatDate value="${parkOrder.endTime}" pattern="MM-dd HH:mm"/>
				</td>
				<td>
					<fmt:formatDate value="${parkOrder.payTime}" pattern="MM-dd HH:mm"/>
				</td>
				<td>
					${parkOrder.cost}元
				</td>
				<td>
					${fns:getDictLabel(parkOrder.status, 'park_order_status', '')}
				</td>
				<shiro:hasPermission name="park:parkOrder:edit"><td>
    				<a href="${ctx}/park/parkOrder/form?id=${parkOrder.id}">修改</a>
					<a href="${ctx}/park/parkOrder/delete?id=${parkOrder.id}" onclick="return confirmx('确认要删除该停车订单吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>