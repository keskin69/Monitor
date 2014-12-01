/**
 * 
 */
function drawChart(str) {
	var data = new google.visualization.DataTable();
	data.addColumn('datetime', 'Date');
	data.addColumn('number', 'Value');
	window.alert(str);
	var name = document.getElementById("rptName").innerHTML;
	var table = document.getElementById(name);
	for (var i = 0, row; row = table.rows[i]; i++) {
		if (i > 0) {
			var dt = row.cells[0].innerHTML;
			var vl = row.cells[1].innerHTML;

			var year = parseInt(dt.substring(0, 4));
			var month = parseInt(dt.substring(5, 7));
			var day = parseInt(dt.substring(8, 10));

			if (dt.length > 10) {
				var hour = parseInt(dt.substring(11, 13));
				var min = parseInt(dt.substring(14, 16));

				data.addRow([ new Date(year, month, day, hour, min),
						parseInt(vl) ]);
			} else {
				data.addRow([ new Date(year, month, day), parseInt(vl) ]);
			}
		}
	}

	var options = {
		title : name,
		hAxis : {
			title : 'Zaman',
		}
	};

	var chart = new google.visualization.ColumnChart(document
			.getElementById('chart_div_' + name));

	chart.draw(data, options);
}
