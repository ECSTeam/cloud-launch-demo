function updateClosure() {
	var updateStateClasses = [ 'updateavailable', 'nolocal', 'uptodate',
			'noremote' ];
	var updateStateStrings = [
			[ 'An update is available!' ],
			[
					'An update may be available',
					"We weren't able to determine the local version of the code. If you update, you may lose any changes you made locally" ],
			[ 'Currently up to date!' ], [ 'Cannot contact GitHub' ] ];
	var getDeployedVersion = function() {
		var localSha = null;

		$.ajax({
			url : '/services/buildInfo/deployed',
			dataType : "json",
			async : false
		}).done(function(data) {
			localSha = data.deployedSha;
		});

		return localSha;
	};

	var getSourceVersion = function() {
		var remoteSha = null;

		$.ajax({
			url : '/services/buildInfo/source',
			dataType : "json",
			async : false
		}).done(function(data) {
			remoteSha = data.sha;
		});

		return remoteSha;
	};

	var getUpdateState = function() {
		var localSha = getDeployedVersion();
		if (localSha == null) {
			return 1;
		}

		var remoteSha = getSourceVersion();
		if (remoteSha == null) {
			return 3;
		}

		console.log("local sha " + localSha);
		console.log("remote sha" + remoteSha);

		return (localSha == remoteSha) ? 2 : 0;
	};
	
	var triggerUpdate = function() {
		$('#updateButton').html('<span class="fa fa-spin fa-spinner"></span>&nbsp;Updating...');
		
		var postUri = '/services/builds/trigger';
		
		$.post(postUri).done(monitorBuild);
	};
	
	var monitorBuild = function(data) {
		var uri = data.monitorUri;
		var wait = data.wait || 0;
		
		var isStillBuilding = data.stillBuilding;
		var result = data.result;
		
		if (isStillBuilding) {
			// if we're still building, then check again in 2 seconds
			wait = 2000;
		} else if (result) {
			// if we're not building and we have a result, then we're done
			if (result == 'SUCCESS') {
				location.reload();
			} else {
				$('#updateButton').text(result);
				return;
			}
		}
		
		setTimeout(function() {
			$.getJSON(uri).done(monitorBuild);
		}, wait);
	}

	var updateState = getUpdateState();

	var lines = updateStateStrings[updateState];
	
	var root = $('#update');
	
	$.each(lines, function(i, line) {
		var p = $('<p/>');
		p.text(line);
		
		root.append(p);
	});

	var button = $('<button/>');
	button.attr('type','button');
	button.attr('id', 'updateButton');
	button.addClass(updateStateClasses[updateState]);
	button.text('Update');
	
	if (updateState > 1) {
		button.attr('disabled', 'disabled');
	}
	
	button.click(triggerUpdate);
	root.append(button);
}