/****************************
Copyright (c) 2013,2014 Canadensys
Script to handle Google charts
****************************/
/*global EXPLORER, $, window, _, google, console*/

EXPLORER.chart = (function(){

  'use strict';

  var _private = {
    
    loadPieChart : function(fieldKey,rows){
      var options = {
        type : 'pie',
        title: EXPLORER.i18n.getLanguageResource('view.stats.chart.piechart.title') + ' ' + EXPLORER.i18n.getLanguageResource('filter.'+fieldKey)
      },
      googleChart = new google.visualization.ColumnChart($('#chart_pie')[0]),
      chartData = new google.visualization.DataTable();

      chartData.addColumn('string', fieldKey);
      chartData.addColumn('number', 'count');
      chartData.addRows(rows.length);

      $.each(rows, function(i) {
        chartData.setValue(i, 0, this[0]);
        chartData.setValue(i, 1, this[1]);
      });
    
      googleChart.draw(chartData, options);
    }
  };

  return {
    init: function() { return; },
    loadPieChart: function(fieldKey,columns,rows) {
      return _private.loadPieChart(fieldKey,columns,rows);
    }
  };

}());