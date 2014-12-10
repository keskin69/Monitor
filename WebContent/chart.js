/**
 * 
 */
function drawChart() {
	var tblName = document.getElementById("rptName").innerHTML;
	drawChart2D("Detay", tblName);
}

function drawChart2D(title, name) {
	var data = new google.visualization.DataTable();
	data.addColumn('datetime', 'Tarih');
	data.addColumn('number', 'Değer');

	var table = document.getElementById(name);

	for (var i = 0, row; row = table.rows[i]; i++) {
		if (i > 0) {
			var dt = row.cells[0].innerHTML;
			var vl = row.cells[1].innerHTML;

			var year = parseInt(dt.substring(0, 4));
			var month = parseInt(dt.substring(5, 7)) - 1;
			var day = parseInt(dt.substring(8, 10));

			if (dt.length > 10) {
				var hour = parseInt(dt.substring(11, 13));
				var min = parseInt(dt.substring(14, 16));

				data.addRow([ new Date(year, month, day, hour, min),
						parseFloat(vl) ]);
			} else {
				data.addRow([ new Date(year, month, day), parseFloat(vl) ]);
			}
		}
	}

	var options = {
		title : title,
		hAxis : {
			title : 'Tarih',
		}
	};

	var chart = new google.visualization.ColumnChart(document
			.getElementById('chart_div_' + name));

	chart.draw(data, options);
}

function drawChart3D(title, name, name2) {
	var data = new google.visualization.DataTable();
	data.addColumn('datetime', 'Tarih');
	data.addColumn('number', name);
	data.addColumn('number', name2);

	var table1 = document.getElementById(name);
	var table2 = document.getElementById(name2);

	for (var i = 0, row; row = table1.rows[i]; i++) {
		if (i > 0) {
			var dt = row.cells[0].innerHTML;
			var vl = row.cells[1].innerHTML;

			var year = parseInt(dt.substring(0, 4));
			var month = parseInt(dt.substring(5, 7)) - 1;
			var day = parseInt(dt.substring(8, 10));

			val2 = table2.rows[i].cells[1].innerHTML;

			if (dt.length > 10) {
				var hour = parseInt(dt.substring(11, 13));
				var min = parseInt(dt.substring(14, 16));

				data.addRow([ new Date(year, month, day, hour, min),
						parseInt(vl), parseInt(val2) ]);
			} else {
				data.addRow([ new Date(year, month, day), parseInt(vl),
						parseInt(val2) ]);
			}
		}
	}

	var options = {
		title : title,
		hAxis : {
			title : 'Tarih',
		}
	};

	var chart = new google.visualization.ColumnChart(document
			.getElementById('chart_div_' + name));

	chart.draw(data, options);
}
