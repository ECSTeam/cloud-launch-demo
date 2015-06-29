[#ftl]
<head>
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" />

<link rel="stylesheet" href="css/main.css" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
[#if standardDemo]
	<script src="js/pagetext.js"></script>
[/#if]
[#if statisticsEnabled]
	<script type="text/javascript"
		src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1','packages':['corechart','gauge']}]}"></script>
	<script src="js/dashboard.js"></script>
[/#if]
[#if selfUpdateEnabled]
	<script src="js/updates.js"></script>
[/#if]

<script type="text/javascript">
[#if standardDemo]
	$(pageTextClosure);
[/#if]
[#if statisticsEnabled]
	$(dashboardClosure);
[/#if]
[#if selfUpdateEnabled]
	$(updateClosure);
[/#if]
</script>

[#include "custom-head.ftl"]
</head>