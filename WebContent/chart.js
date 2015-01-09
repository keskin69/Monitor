/**
 * 
 */
function drawChart() {
	var tblName = document.getElementById("rptName").innerHTML;
	drawChart2D("Detay", tblName);
}

function getData(name, data) {
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

}

function drawChart2D(title, name) {
	var data = new google.visualization.DataTable();
	data.addColumn('datetime', 'Tarih');
	data.addColumn('number', name);

	getData(name, data);

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
	var data1 = new google.visualization.DataTable();
	data1.addColumn('datetime', 'Tarih');
	data1.addColumn('number', name);

	var data2 = new google.visualization.DataTable();
	data2.addColumn('datetime', 'Tarih');
	data2.addColumn('number', name2);
	getData(name, data1);
	getData(name2, data2);
	var joinedData = google.visualization.data.join(data1, data2, 'full', [ [
			0, 0 ] ], [ 1 ], [ 1 ]);

	var options = {
		title : title,
		hAxis : {
			title : 'Tarih',
		}
	};

	var chart = new google.visualization.ColumnChart(document
			.getElementById('chart_div_' + name));

	chart.draw(joinedData, options);
}
