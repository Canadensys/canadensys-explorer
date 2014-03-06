/****************************
Copyright (c) 2013 Canadensys
Explorer Portal
****************************/
/*global EXPLORER, $, window, document, console, google, _*/

/* Control the fly-out panel for occurrence record
Dependencies:
jQuery-UI: slide method
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
        self.hide();
      });
    },

    hide: function() {
      this.occPreview.hide("slide", 500);
    },

    show: function() {
      if($(window).scrollTop() >= this.sidebarTop) { this.occPreview.addClass('fixed'); }
      this.occPreview.show("slide", 500);
    },

    replacePreviewContent: function(htmlFragment) {
      this.occPreview.html(htmlFragment);
      this.initClosePreview();
    }

  };

  return {
    init: function() { _private.init(); },
    replacePreviewContent : function(htmlFragment) {
      _private.replacePreviewContent(htmlFragment);
    },
    hide : function() {
      _private.hide();
    },
    show : function() {
      _private.show();
    }
  };

}());

/* Control the columns in table view
Dependencies:
jquery.cookie.js
*/
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
      var self = this,
          display = $('#display_columns'),
          checkboxes = display.find('input:checkbox'),
          settingsCookie = $.cookie(this.COOKIE_NAME);

      if(settingsCookie){
        var arrayOfSettings = settingsCookie.split(',');
        $.each(checkboxes, function() {
          var isSelected = _.include(arrayOfSettings, $(this).val());
          if((isSelected && !$(this).prop("checked")) || (!isSelected && $(this).prop("checked"))){
            $(this).trigger('click');
          }
        });
      }

      display.on('click', 'input:checkbox', function() {
        var selectedColumn = [];

        $.each(checkboxes, function() {
          if($(this).prop("checked")) { selectedColumn.push($(this).val()); }
        });
        $.cookie(self.COOKIE_NAME, selectedColumn, { expires: 7 });
      });

    }

  };

  return {
    init: function() { _private.init(); },
    restoreDisplay: function() { _private.restoreDisplay(); }
  };

}());

/* Initialize table row clicks
Dependencies:
keynavigator.js
*/
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

    getFragment: function(id) {
      $.get("occurrence-preview/"+id, function(htmlFragment) {
        EXPLORER.preview.replacePreviewContent(htmlFragment);
      })
      .fail(function(jqXHR, textStatus, errorThrown) {
          console.log(textStatus);
        });
    },

    buildResponse: function(element) {
      this.getFragment(element.attr("id"));
      EXPLORER.preview.show();
      element.addClass('selected');
      $('.last_column',element).addClass('last_column_hide');
    },

    scrollWindow: function(element) {
      $('html,body').animate({scrollTop: element.offset().top + parseInt(element.css('padding-top'),10) },'fast');
    },

    loadEvents: function() {
      var self = this, previousSelection;

      $('tbody tr', this.results).on('click', function() {
        if(previousSelection && previousSelection === this && self.occPreview.is(":visible")) {
          EXPLORER.preview.hide();
          $('.last_column',$(this)).removeClass('last_column_hide');
        } else {
          self.buildResponse($(this));
        }
        previousSelection = this;
      })
      .keynavigator({
        activeClass: 'selected',
        cycle: false,
        keys: {
          left_arrow : function(element) {
            EXPLORER.preview.hide();
            $('.last_column',element).removeClass('last_column_hide');
          },
          escape : function(element) {
            EXPLORER.preview.hide();
            $('.last_column',element).removeClass('last_column_hide');
          },
          right_arrow : function(element) {
            self.buildResponse($(element));
          },
          enter : function(element) {
            self.buildResponse($(element));
          }
        }
      })
      .on('down', function() {
        if(!EXPLORER.utils.isScrolledIntoView($(this))) {
          self.scrollWindow($(this));
        }
      })
      .on('up', function() {
        var previousRow = $(this).prev().prev();
        if(previousRow.length > 0 && !EXPLORER.utils.isScrolledIntoView(previousRow)) {
          self.scrollWindow(previousRow);
        }
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

/* Display Google Map on occurrence view
Dependencies:
Google Maps
*/
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