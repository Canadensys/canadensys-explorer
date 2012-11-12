// Chart base
var canadensysChart = (function($){
	
	var baseChart = {
		type : 'pie',
		title : '',
		axis : {xtitle:'x', ytitle:'y'},
		googleChart : undefined,
		googleChartOptions : undefined,
		loadData : function loadData(columns, json){
			
			json = this.transformData(json);
			var data = new google.visualization.DataTable();
			
			for (var i = 0; i < columns.length; i++) {
				data.addColumn(columns[i].type, columns[i].text);
		    }
		    
			data.addRows(json.length);
		    for (var i = 0; i < json.length; i++) {
		        data.setValue(i, 0, json[i][0]);
		        data.setValue(i, 1, json[i][1]);
		    }
		    
			var options = {
				title: this.title,
		  	    width: 560,
		  	    height: 360,
		  	    bar:{groupWidth:'98%'}
	  	    };
			
			if(this.googleChartOptions){
				_.extend(options, this.googleChartOptions);
			}
			
			this.googleChart.draw(data, options);
		},
		transformData : function transformData(json){return json;}
	}
	
	function createChart(options){
		return  _.extend({}, baseChart, options);
	}

	//Public methods
    return {
    	createChart : createChart
	};
	
}(jQuery));