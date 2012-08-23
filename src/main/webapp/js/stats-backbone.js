
var occurrenceStats = (function($){
	
	UniqueCountModel = Backbone.Model.extend({
	  defaults: {
	    fieldId: -1,
	    count : -1,
	    fieldText : undefined,
	    groupId : undefined
	  }
	});
	
	UniqueCountModelList = Backbone.Collection.extend({
		model: UniqueCountModel
	});
	
	StatsModel = Backbone.Model.extend({
	  defaults: {
	    fieldId: -1
	  }
	});
	
	var uniqueCountModelList = new UniqueCountModelList();
	var currFieldKey = new StatsModel();
	var searchQuery;
	
	var histogram={};
	
	function initStatsView(_searchQuery){
		searchQuery = _searchQuery;
		
		new PieChartView({groupId:'classification'});
		new PieChartView({groupId:'location'});
		
		histogram['decade'] = new ColumnChartView({fieldIdText:'decade',transformDataFunction:transformDecadeData});
		histogram['altitude'] = new ColumnChartView({fieldIdText:'altitude',transformDataFunction:transformAltitudeData});
	}
	
	// A static chart is always displayed
	function loadStaticChart(fieldId,fieldIdText,fieldText){
		google.setOnLoadCallback(function() {
	    	$(function() {
				if(histogram[fieldIdText]){
					histogram[fieldIdText].loadChart(fieldId,fieldIdText,fieldText);
				}
	    	});
	    });
	}
	
	function selectDefaultChart(fieldId){
		google.setOnLoadCallback(function() {
	    	$(function() {
	    		$('input[type=radio][value="'+fieldId+'"]').click();
	    	});
	    });
	}
	
	// Loading the count a specific field
	function loadFieldUniqueCount(fieldId,fieldText,groupId){
		var countModel = new UniqueCountModel();
		countModel.set('fieldId',fieldId);
		countModel.set('fieldText',fieldText);
		countModel.set('groupId',groupId);
		uniqueCountModelList.add(countModel);
		
		var req = $.get('stats/unique/'+fieldId,searchQuery)
			.success(function(json){
				countModel.set('count',json.count);
			})
		   .error(function(jqXHR, textStatus, errorThrown){
			   console.log(textStatus+" : "+errorThrown);
			});
	}
	
	var StatsSelectionView = Backbone.View.extend({
		initialize : function() {
			uniqueCountModelList.bind('add', this.addStats, this);
			//el could be set through the caller
			this.setElement(this.el);
		},
		render : function() {
			return this;
		},
		events : {
			"click input[type=radio]" : "onSelection"
		},
		addStats : function(uniqueCountModel) {
			//make sure this uniqueCountModel is in our group
			var field = uniqueCountModelList.where({fieldId:uniqueCountModel.get('fieldId')});
			if(field.length === 0 || field[0].get('groupId') !== this.options.groupId){
				return;
			}
			var statsFieldTemplate = _.template($('#stats_field_template').html());
			var $tr = $(statsFieldTemplate({fieldId : uniqueCountModel.get('fieldId'),fieldText : uniqueCountModel.get('fieldText'),groupId:uniqueCountModel.get('groupId')}));
			
			uniqueCountModel.bind('change:count',
				function(model){
					$tr.find('td:nth-child(2)').html(uniqueCountModel.get('count'));
				},
				this);
			$('tbody', this.$el).append($tr);
		},
		onSelection : function(evt) {
			var value  = parseInt($(evt.currentTarget).val(),10);
			currFieldKey.set('fieldId',value);
		}
	});
	
	// PieChart view
	var PieChartView = Backbone.View.extend({
		initialize : function() {
			var self = this;
			// Set a callback to run when the Google Visualization API is loaded.
		    google.setOnLoadCallback(function() {
		    	$(function() {
		    		self.googleChart = new google.visualization.PieChart(document.getElementById(self.options.groupId + '_chart_div'));
		    	});
		    });
		    currFieldKey.bind('change:fieldId', this.onSelection, this);
		    this.$spinner = $('#spinner');
		},
		render : function() {
			return this;
		},
		events : {
		},
		onSelection : function(_currFieldKey) {
			//make sure this uniqueCountModel is in our group
			var field = uniqueCountModelList.where({fieldId:_currFieldKey.get('fieldId')});
			if(field.length === 0 || field[0].get('groupId') !== this.options.groupId){
				return;
			}
			field = field[0];
			
			this.paramMap = [];
			_.extend(this.paramMap,searchQuery);
			this.paramMap.push({name:'max',value:10});
			
			var self = this;
			self.$spinner.show();
			var jsonData = $.ajax({
	  	          url: 'stats/chart/' + _currFieldKey.get('fieldId'),
	  	          dataType:"json",
	  	          data:this.paramMap,
	  	          async: false
	  	          })
	  	          .always(function() { self.$spinner.hide(); })
	  	          .responseText;
			var jsonData = JSON.parse(jsonData);
			var options = {
				googleChart: self.googleChart,
				title: languageResources.getLanguageResource('view.stats.chart.piechart.title') + ' ' + field.get('fieldText')
			}
				
			var columns = [{type:'string',text:field.get('fieldText')}, {type:'number',text:'count',interval:1}];
			var chart = canadensysChart.createChart(options);
			chart.loadData(columns,jsonData.rows);
		}
	});
	
	// Method to prepare the Decade data before display
	function transformDecadeData(json){
		var START_DECADE = 1850;
		json = _.sortBy(json, function(data){ 
			if(isNaN(parseInt(data[0]))){
				return 0;
			}
			return data[0];
		});
		
		var formattedJson = [];
		var decade = START_DECADE;
		var currYear = new Date().getFullYear();
		
		var beforeStartDecade = _.filter(json, function(data){ return data[0] < START_DECADE; });
		var afterStartDecade = _.filter(json, function(data){ return data[0] >= START_DECADE; });
		
		var beforeStartDecadeCount = 0;
		for(var i=0;i<beforeStartDecade.length;i++){
			beforeStartDecadeCount = beforeStartDecadeCount + beforeStartDecade[i][1];
		}
		if(beforeStartDecadeCount > 0){
			formattedJson.push([languageResources.getLanguageResource('view.stats.chart.decade.before') + ' ' + decade,beforeStartDecadeCount]);
		}
		var i = 0;
		while(i < afterStartDecade.length && decade < currYear) {
			if(decade === afterStartDecade[i][0]){
				formattedJson.push(afterStartDecade[i]);
				i++;
			}
			else{
				formattedJson.push([decade,0]);
			}
			decade = decade+10;
	    }
		
		json = _.map(formattedJson, function(data){ 
			data[0] = data[0].toString()+'s';
			return data; 
		});
		return json;
	}
	
	// Method to prepare the Altitude data before display
	function transformAltitudeData(json){
		var MAX_ALTITUDE = 2000;
		var formattedJson = [];
		json = _.sortBy(json, function(data){ 
			if(isNaN(parseInt(data[0]))){
				return 0;
			}
			return data[0];
		});
		
		var aboveMaxMetersCount = 0;
		formattedJson.push([languageResources.getLanguageResource('view.stats.chart.altitude.below') +" 0",0]);
		for(i=0;i < json.length;i++) {
			if(canadensysUtils.isInteger(json[i][0])){
				if(json[i][0] < 0){
					formattedJson[0][1] = formattedJson[0][1] + json[i][1];
				}
				else if(json[i][0] >= MAX_ALTITUDE){
					aboveMaxMetersCount = aboveMaxMetersCount + json[i][1];
				}
				else{
					formattedJson.push(json[i]);
				}
			}
	    }
		formattedJson.push([languageResources.getLanguageResource('view.stats.chart.altitude.above')+" 2000",aboveMaxMetersCount]);
		
		formattedJson = _.map(formattedJson, function(data){ 
			data[0] = data[0].toString()+' m';
			return data; 
		});
		return formattedJson;
	}
	
	// Column Chart view
	var ColumnChartView = Backbone.View.extend({
		initialize : function() {
			var self = this;
			// Set a callback to run when the Google Visualization API is loaded.
		    google.setOnLoadCallback(function() {
		    	$(function() {
		    		self.googleChart = new google.visualization.ColumnChart(document.getElementById(self.options.fieldIdText + '_chart_div'));
		    	});
		    });
		    this.$spinner = $('#spinner');
		},
		render : function() {
			return this;
		},
		loadChart : function(fieldId, fieldIdText, fieldText) {
			if(fieldIdText !== this.options.fieldIdText){
				return;
			}
			
			var self = this;
			self.$spinner.show();
			var jsonData = $.ajax({
	  	          url: 'stats/chart/' + fieldId,
	  	          dataType:"json",
	  	          data:searchQuery,
	  	          async: false
	  	          })
	  	          .always(function() { self.$spinner.hide(); })
	  	          .responseText;
			
			//Create our data table out of JSON data loaded from server.
		    var jsonData = JSON.parse(jsonData);
		    var maxCount = _.max(jsonData.rows, function(data){
		    	if(canadensysUtils.isInteger(data[0])){
		    		return data[1];
		    	}
		    	return 0;
		    });
		    //Make sure we never have more grid line than max count
		    var gridlinesCount = maxCount[1] >= 5 ? 5 :maxCount[1]+1; 
		    
			var options = {
				type : 'column',
				title : fieldText,
				googleChart: self.googleChart,
				googleChartOptions : {hAxis:{slantedText:true,slantedTextAngle:90},vAxis:{format:'#',gridlines:{count:gridlinesCount}},legend:{position: 'none'}},
				transformData : this.options.transformDataFunction
			}
			
			var columns = [{type:'string',text:fieldText}, {type:'number',text:'count'}];
			var chart = canadensysChart.createChart(options);
			chart.loadData(columns,jsonData.rows);
		}
	});
	
	// Instantiate stats selector
	new StatsSelectionView({ el: $('#classification_stats'), groupId:'classification' });
	new StatsSelectionView({ el: $('#location_stats'), groupId:'location' });
	
	//Public methods
    return {
    	initStatsView : initStatsView,
    	loadFieldUniqueCount : loadFieldUniqueCount,
    	loadStaticChart : loadStaticChart,
    	selectDefaultChart : selectDefaultChart
	};
}(jQuery));