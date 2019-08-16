<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>停车订单理管</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function(){
	$("button").click(function(){
		$.get("${ctx}/park/api/park",function(data,status){
			alert("数据: " + data );
		});
	});
});
</script>
</head>
<body>
	<button>发起存车订单</button>
</body>
</html>