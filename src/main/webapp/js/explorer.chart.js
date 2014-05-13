/****************************
Copyright (c) 2013,2014 Canadensys
Script to handle Google charts
****************************/
/*global EXPLORER, $, window, _, google, console*/

EXPLORER.chart = (function(){

  'use strict';

  var _private = {
    
    loadPieChart : function(chartTitle,fieldKey,rows){
      var options = {
        title: chartTitle,
        type : 'pie'
      },
      googleChart = new google.visualization.PieChart($('#chart_pie')[0]),
      chartData = new google.visualization.DataTable(),
      rowIdx = 0;

      chartData.addColumn('string', fieldKey);
      chartData.addColumn('number', 'count');
      chartData.addRows(_.size(rows));

      $.each(rows, function(key,value) {
        chartData.setValue(rowIdx, 0, key);
        chartData.setValue(rowIdx, 1, value);
        rowIdx += 1;
      });
      googleChart.draw(chartData, options);
    },

    loadBarChart : function(chartTitle,fieldKey,rows){
      var maxCount = _.max(rows, function(data){ 
            if(EXPLORER.utils.isInteger(data[0])){
              return data[1];
            }
            return 0;
          }),
          //Ensure to not draw too many grid lines on the chart if there is less than 5 elements
          gridlinesCount = maxCount[1] >= 5 ? 5 :maxCount[1]+1,
    
          options = {
            title: chartTitle,
            hAxis:{slantedText:true,slantedTextAngle:90},
            vAxis:{format:'#',gridlines:{count:gridlinesCount}},
            legend:{position: 'none'}
          },
          googleChart = new google.visualization.ColumnChart($('#chart_bar')[0]),
          rowIdx = 0,

      chartData = new google.visualization.DataTable();
      chartData.addColumn('string', fieldKey);
      chartData.addColumn('number', 'count');
      chartData.addRows(_.size(rows));

      $.each(rows, function(key,value) {
        chartData.setValue(rowIdx, 0, key);
        chartData.setValue(rowIdx, 1, value);
        rowIdx += 1;
      });
      googleChart.draw(chartData, options);
    }
  };

  return {
    init: function() { return; },
    loadPieChart: function(chartTitle,fieldKey,rows) {
      return _private.loadPieChart(chartTitle,fieldKey,rows);
    },
    loadBarChart: function(chartTitle,fieldKey,rows) {
      return _private.loadBarChart(chartTitle,fieldKey,rows);
    }
  };

}());