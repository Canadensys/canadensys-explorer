/****************************
Copyright (c) 2013 Canadensys
Explorer Portal
****************************/
/*global EXPLORER, $, window, document, console, google, _*/

/* Control the fly-out panel for occurrence record
Dependency: jQuery-UI's "slide" method
*/
EXPLORER.preview = (function() {

  'use strict';

  var _private = {

    sideBar: "",
    occPreview: "",
    closeElement: "",
    sidebarTop: 0,

    init: function() {
      this.sideBar = $('#control_wrapper');

      if(this.sideBar.length > 0) {
        this.occPreview = $('#occ_preview');
        this.closeElement = $('#preview_close');
        this.sidebarTop = this.sideBar.offset().top - 36;
        this.loadEvents();
      }
    },

    loadEvents: function() {
      var self = this;

      this.initClosePreview();
      $(window).scroll(function() {
        if ($(this).scrollTop() >= self.sidebarTop) {
          self.occPreview.addClass('fixed');
        } else {
          self.occPreview.removeClass('fixed');
        }
      });
    },

    initClosePreview: function() {
      var self = this;
      $('#preview_close').on('click', function(){
        self.occPreview.hide('slide',500);
      });
    },

    togglePreview: function(oldSelectionId, newSelectionId) {
      //check if the click is on the same element
      if(oldSelectionId && (oldSelectionId === newSelectionId)){
        if(this.occPreview.is(":visible")){
          this.occPreview.hide("slide",500);
        } else {
          this.occPreview.show("slide",500);
        }
      } else {
        //check if the component is not already shown
        if(!this.occPreview.is(":visible")){
          this.occPreview.show("slide",500);
        }
      }
    },

    replacePreviewContent: function(htmlFragment) {
      this.occPreview.html(htmlFragment);
      this.initClosePreview();
    }

  };

  return {
    init: function() { _private.init(); },
    togglePreview : function(oldSelectionId, newSelectionId) {
      _private.togglePreview(oldSelectionId, newSelectionId);
    },
    replacePreviewContent : function(htmlFragment) {
      _private.replacePreviewContent(htmlFragment);
    }
  };

}());


EXPLORER.control = (function() {

  'use strict';

  var _private = {

    COOKIE_NAME: 'canadensys-responsive-table-settings',

    init: function() {
      this.loadEvents();
    },

    loadEvents: function() {
      var id = "";

      $('#control_buttons').on('click', 'a', function(e) {
        e.preventDefault();
        id = $(this).attr("href");
        $(this).addClass("selected").parent().siblings().children().removeClass("selected");
        $(id).removeClass("hidden").siblings().addClass("hidden");
      });
    },

    restoreDisplay: function() {
      var self = this, display = $('#display_columns');

      if(display.length > 0){
        display.on('click', 'input:checkbox', function() {
          var selectedColumn = [];

          $.each(display.find('input:checked'), function() {
            selectedColumn.push($(this).val());
          });
          EXPLORER.utils.createCookie(self.COOKIE_NAME,selectedColumn.join('|'),7);
        });

        var settingsCookie = EXPLORER.utils.readCookie(self.COOKIE_NAME);
        if(settingsCookie){
          //we cannot store a ; in the cookie so use a pipe (|)
          var arrayOfSettings = settingsCookie.split('|');
          $.each(display.find('input:checkbox'), function() {
            var isSelected = _.include(arrayOfSettings, $(this).val());
            if(isSelected !== this.checked){
              //need to click to update the table display
              this.trigger('click');
            }
          });
        }
      }
    }

  };

  return {
    init: function() { _private.init(); },
    restoreDisplay: function() { _private.restoreDisplay(); }
  };

}());


EXPLORER.table = (function() {

  'use strict';

  var _private = {

    results: "",
    occPreview: "",

    init: function() {
      this.results = $('#results');
      this.occPreview = $('#occ_preview');
      if(this.occPreview.length && this.results.length){
        this.loadEvents();
      }
    },

    loadEvents: function() {
      var oldSelection;

      this.results.on('click', 'tbody tr', function() {
        //do not send query to the server for the same element
        if(!oldSelection || oldSelection.attr('id') !== $(this).attr('id')){

          $.get("occurrence-preview/"+$(this).attr("id"), function(htmlFragment) {
            EXPLORER.preview.replacePreviewContent(htmlFragment);
          })
          .fail(function(jqXHR, textStatus, errorThrown) {
              console.log( textStatus );
            });
        }

        if(oldSelection){
          EXPLORER.preview.togglePreview(oldSelection.attr('id'),$(this).attr('id'));
        } else {
          EXPLORER.preview.togglePreview(undefined,$(this).attr('id'));
        }

        if(oldSelection){
          oldSelection.toggleClass('selected');
        }
        $(this).toggleClass('selected');
        oldSelection = $(this); 
      });

      //responsive design
      this.results.table({
        idprefix: "co-",
        persist: "persist"
      });
    }

  };

  return {
    init: function() { _private.init(); }
  };

}());


EXPLORER.details = (function() {

  'use strict';

  var _private = {

    init: function() {
      if($('#dwc_table').length) {
        this.loadEvents();
      }
    },

    loadEvents: function() {
      $('#dwc_table_toggle').on('click', function(e) {
        e.preventDefault();
        $('tr.unused').toggle();
      });
    },

    setupSingleOccurrenceMap: function(mapCanvasId, lat, lng, uncertainty) {
      var latlng, map, marker, coordinateUncertainty;

      if(!lat || !lng){
        $('#'+mapCanvasId).addClass('no_geo');
        return;
      }

      latlng = new google.maps.LatLng(lat,lng);

      map = new google.maps.Map($('#'+mapCanvasId)[0], {
        center: latlng,
        zoom: 12,
        scaleControl: true,
        mapTypeId: google.maps.MapTypeId.TERRAIN,
        mapTypeControl: true
      });

      marker = new google.maps.Marker({
        position:latlng,
        map: map
      });

      coordinateUncertainty = new google.maps.Circle({
        map: map,
        center: latlng,
        radius: uncertainty,
        strokeWeight: 1
      });
    }

  };

  return {
    init: function() { _private.init(); },
    setupSingleOccurrenceMap : function(mapCanvasId, lat, lng, uncertainty) {
      _private.setupSingleOccurrenceMap(mapCanvasId, lat, lng, uncertainty);
    }
  };

}());