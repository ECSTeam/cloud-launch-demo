function dashboardClosure() {
	google.load('visualization', '1', {
		packages : [ 'corechart' ]
	});

	var ringChartObject = {};

	var stateClassMap = {
		'RUNNING' : [ 'fa', 'fa-arrow-circle-up', 'instanceState', 'RUNNING' ],
		'STARTING' : [ 'fa', 'fa-spin', 'fa-gear', 'instanceState', 'STARTING' ],
		'FLAPPING' : [ 'fa', 'fa-warning', 'instanceState', 'FLAPPING' ],
		'CRASHED' : [ 'fa', 'fa-minus-circle', 'instanceState', 'CRASHED' ],
		'DOWN' : [ 'fa', 'fa-arrow-circle-down', 'instanceState', 'DOWN' ],
		'UNKNOWN' : [ 'fa', 'fa-question-circle', 'instanceState', 'UNKNOWN' ]
	};

	var stateToNameMap = {
		'RUNNING' : 'Running',
		'STARTING' : 'Starting',
		'DOWN' : 'Down',
		'CRASHED' : 'Crashed',
		'FLAPPING' : 'Flapping',
		'UNKNOWN' : 'Unknown'
	};

	var sizeString = function(bytes, sigDigits) {
		if (!sigDigits && sigDigits !== 0)
			sigDigits = 2;

		if (bytes == 0) {
			return '0';
		}

		var sizes = [ 'B', 'KB', 'MB', 'GB', 'TB' ];

		var power = Math.floor(Math.log(bytes) / Math.log(1024));
		var reducedSize = bytes / Math.pow(1024, power);
		var roundedSize = reducedSize.toFixed(sigDigits);

		return [ roundedSize, ' ', sizes[power] ].join('');
	}

	var getStateColor = function(state) {
		var testObj = $('<div/>');

		testObj.addClass('instanceState');
		testObj.addClass(state);

		$('body').append(testObj);
		var color = testObj.css('color');
		testObj.remove();

		return color;
	};

	var buildDashboard = function() {
		var uri = '/services/statistics';

		$.getJSON(uri).done(function(stats) {
			stats.instances = stats.instances || [];

			buildInstanceRing(stats);
			buildInstanceDetailTable(stats);
		}).fail(function(jqXHR, errorText) {
		}).always(function() {
			setTimeout(buildDashboard, 15000);
		});

		setTopMargin();
	};

	var buildInstanceDetailTable = function(stats) {
		var locationRootDiv = $('<div/>');
		locationRootDiv.addClass('locationDetails');

//		var locationDiv = $('<div/>');
//		locationDiv.text([ 'Current listening address: ', stats.host, ':',
//				stats.port ].join(''));
//
//		var killDiv = $('<div/>');
//		var killLink = $('<a/>');
//		killLink.attr('href', 'javascript:void(0)');
//		killLink.text('Kill the active instance!');
//
//		killLink.click(function() {
//			$.getJSON('/services/kill');
//		});
//
//		killDiv.append(killLink);
//		locationRootDiv.append(locationDiv);
//		locationRootDiv.append(killDiv);

		var table = $('<table/>');
		table.addClass('instDetails');
		var thead = $('<thead/>');

		var headerTr = $('<tr/>');
		$.each([ 'Instance', 'RAM', 'Disk' ], function(i, field) {
			var th = $('<th/>');
			th.text(field);
			headerTr.append(th);
		});
		thead.append(headerTr);

		var tbody = $('<tbody/>');
		var numInstances = stats.instances.length;
		$.each(stats.instances, function(index, instance) {
			var name = [ 'Instance ', index + 1, ' of ', numInstances,
					'&nbsp;&nbsp;' ].join('');

			var ramUsage = sizeString(instance.memory, 1);
			var diskUsage = sizeString(instance.disk, 1);

			var ramLimit = sizeString(stats.ramLimit, 0);
			var diskLimit = sizeString(stats.diskLimit, 0);

			var isActive = (index == stats.activeInstance);

			var instRow = $('<tr/>');
			if (isActive) {
				instRow.addClass('activeInstance');
			}

			var nameTd = $('<td/>');
			var nameSpan = $('<span/>');

			nameSpan.html(name);

			var stateSpan = $('<span/>');
			var classArray = stateClassMap[instance.state]
					|| stateClassMap['UNKNOWN'];
			$.each(classArray, function(i, clazz) {
				stateSpan.addClass(clazz);
			});
			stateSpan.attr('title', stateToNameMap[instance.state]);

			nameTd.append(nameSpan);
			nameTd.append(stateSpan);

			var ramTd = $('<td/>');
			ramTd.text([ ramUsage, ' of ', ramLimit ].join(''));

			var diskTd = $('<td/>');
			diskTd.text([ diskUsage, ' of ', diskLimit ].join(''));

			instRow.append(nameTd);
			instRow.append(ramTd);
			instRow.append(diskTd);

			tbody.append(instRow);
		});

		table.append(thead);
		table.append(tbody);

		var root = $('#instanceStatContainer');
		root.empty();
		root.append(locationRootDiv);
		root.append(table);
	};

	var buildInstanceRing = function(stats) {
		if (!(ringChartObject && ringChartObject.chartObj)) {
			var container = $('#ringChart');
			ringChartObject.chartObj = new google.visualization.PieChart(
					container[0]);
			ringChartObject.domNode = container;
		}

		var height = 180;
		var width = ringChartObject.domNode.width();

		var options = {
			backgroundColor : {
				'fill' : 'none',
				'strokeWidth' : 0
			},
			pieSliceText : 'none',
			pieHole : 0.6,
			legend : {
				alignment : 'center',
				position : 'right'
			},
			slices : {},
			height : height,
			width : width
		};

		var instances = stats.instances || [];
		var countMap = {};

		var count = 0;
		$.each(instances, function(idx, instance) {
			var count = countMap[instance.state];
			if (count) {
				++count;
			} else {
				count = 1;
			}

			countMap[instance.state] = count;
		});

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Instance State');
		data.addColumn('number', 'Number of Instances');

		var i = 0;
		$.each(countMap, function(state, count) {
			console.log(state);
			var label = stateToNameMap[state] + ' Instances: ' + count;
			data.addRow([ label, count ]);
			options.slices[i++] = {
				color : getStateColor(state)
			};
		});

		ringChartObject.chartObj.draw(data, options);
	};

	var setTopMargin = function() {
		var headerHeight = $('header').height();
		$('body').css('marginTop', headerHeight + 20 + 'px');
	};

	google.setOnLoadCallback(buildDashboard);
}