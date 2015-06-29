function pageTextClosure() {
	var root = $('#pageContent');
	
	$.getJSON('/services/pagetext').done(function(data) {
		var lines = data.pageText || [];
		
		$.each(lines, function(index, line) {
			var paragraph = $('<p/>');
			paragraph.text(line);
			
			root.append(paragraph);
		});
	}).fail(function(jqXHR, status, errorText) {
		var paragraph = $('<p/>');
		paragraph.addClass('error');
		
		paragraph.text(errorText);
	});
}