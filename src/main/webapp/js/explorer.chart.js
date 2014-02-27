/****************************
Copyright (c) 2013 Canadensys
Script to handle Google charts
****************************/
/*global EXPLORER, $, window, _, google, console*/

EXPLORER.chart = (function(){

  'use strict';

  var _private = {

    baseChart : {
      type : 'pie',
      title : '',
      axis : { xtitle:'x', ytitle:'y' },
      googleChart : undefined,
      googleChartOptions : undefined,
      transformData : function(json) { return json; },
      loadData : function(columns, json){
        var options, data = new google.visualization.DataTable();

        $.each(columns, function() {
          data.addColumn(this.type, this.text);
        });

        json = this.transformData(json);

        data.addRows(json.length);
        $.each(json, function(i) {
          data.setValue(i, 0, this[0]);
          data.setValue(i, 1, this[1]);
        });

        options = {
          title: this.title,
          width: 560,
          height: 360,
          bar:{ groupWidth:'98%' }
        };

        if(this.googleChartOptions){
          _.extend(options, this.googleChartOptions);
        }

        this.googleChart.draw(data, options);
      }
    },

    createChart : function(options) {
      return _.extend({}, this.baseChart, options);
    }

  };

  return {
    init: function() { return; },
    createChart: function(options) {
      return _private.createChart(options);
    }
  };

}());