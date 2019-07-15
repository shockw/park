<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>微信支付查询</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});
</script>
</head>
<body>
	<form:form id="searchForm" action="${ctx}/park/bill/list" method="post"
		class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>订单日期：</label> <input name="billDate" type="text"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${billDate}" pattern="yyyyMMdd"/>"
				onclick="WdatePicker({dateFmt:'yyyyMMdd',isShowClear:false});" /> -
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" /></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<tbody>
			<c:forEach items="${list}" var="childList">
				<tr>
					<c:forEach items="${childList}" var="item">
						<td>${item }</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</body>
</html>