[#ftl]
<body>
	[#if statisticsEnabled]
	<header>
		<div id="dashboard">
			<div id="appHealth" class="tile onethird">
				<div class="title">Application Health</div>
				<div class="container chart" id="ringChart">
					<div class="loading"><span class="fa fa-spin fa-gear"></span> Please wait...</div>
				</div>
			</div>
			<div id="instHealth" class="tile onethird">
				<div class="title">Instances</div>
				<div class="container" id="instanceStatContainer">
					<div class="loading">
						<span class="fa fa-spin fa-gear"></span> Please wait...
					</div>
				</div>
			</div>
		</div>
	</header>
	[/#if]
	[#if standardDemo]
	<h1 class="title">ECS Team Cloud Launch</h1>
	[/#if]
	<div id="main">
		[#if selfUpdateEnabled]
		<div id="update"></div>
		[/#if]
		[#if standardDemo]
		<div id="pageContent"></div>
		[/#if]
		[#include "custom-body.ftl"]
	</div>
</body>