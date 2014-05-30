/****************************
Copyright (c) 2013 Canadensys
Explorer Map
****************************/
/*global EXPLORER, $, window, document, console, jQuery, CartoDBLayer, google, _ */

EXPLORER.map = (function() {

  'use strict';

  var _private = {

    map: {},
    marker: {},
    cartodb_gmapsv3: {},
    drawing_manager: {},
    drawing_overlay: {},
    drawing_types: ["geoellipse", "georectangle", "geopolygon"],
    filter: {},

    init: function() {
      this.addBoundsMethod();
      this.cartoDBsetBounds();
      this.cartoDBgenerateTile();
      EXPLORER.backbone.bindToFilterList('add',this.onAddFilter,this);
      EXPLORER.backbone.bindToFilterList('remove',this.removeFilterListener,this);
    },

    // Adapted from http://tutorialspots.com/google-maps-javascript-api-v3-method-polygon-getbounds-515.html
    addBoundsMethod: function() {
      if (!google.maps.Polygon.prototype.getBounds) {
        google.maps.Polygon.prototype.getBounds = function(){
          var bounds = new google.maps.LatLngBounds();
          this.getPath().forEach(function(element,index){ bounds.extend(element); });
          return bounds;
        };
      }
    },

    cartoDBsetBounds: function() {
      CartoDBLayer.prototype.setBounds = function() { return; };
    },

    onAddFilter: function(filter) {
      var self = this, fieldName = filter.get('searchableFieldName'), valueList, coords, data, overlay_type;

      if($.inArray(fieldName, this.drawing_types) !== -1 && $.isEmptyObject(this.drawing_overlay)) {
        self.filter = filter;
        valueList = filter.get('valueList');

        switch (fieldName) {
          case this.drawing_types[0]:
            overlay_type = 'circle';
            coords = valueList[0].split(",");
            data = { coords : [coords[0],coords[1]], radius : parseInt(valueList[1], 10) };
          break;

          case this.drawing_types[1]:
            overlay_type = 'rectangle';
            coords = $.map(valueList.reverse(), function(n) { return n.split(","); });
            data = { coords : [ [coords[0], coords[1]], [coords[2], coords[3]] ] };
          break;

          case this.drawing_types[2]:
            overlay_type = 'polygon';
            coords = $.map(valueList, function(n) { return [ n.split(",") ]; });
            data = { coords : coords };
          break;
        }

        this.addDrawingOverlay({ type : fieldName, data : data });
        this.addDrawingOverlayListeners(overlay_type);
      }
    },

    removeFilterListener: function(filter) {
      if($.inArray(filter.get('searchableFieldName'), this.drawing_types) !== -1) {
        this.removeDrawingOverlay();
        this.filter = {};
      }
    },

    setBounds: function(filterList) {
      var self = this, spatial_filter = false, bounds;

      $.each(this.drawing_types, function() {
        if(filterList.where({ searchableFieldName : this }).length > 0) {
          spatial_filter = true;
          return;
        }
      });

      if(filterList.length > 0 && !spatial_filter) {
        $.ajax({
          method:'GET',
          url: EXPLORER.settings.baseUrl + EXPLORER.settings.wsPath + 'mapinfo?q='+encodeURIComponent(this.cartodb_gmapsv3.options.query || ''),
          dataType: 'json',
          success: function(result) {
            if(result.extentMin[1]*result.extentMax[1] > 0 &&
            self.getDistanceAtEquator(result.extentMin[1], result.extentMax[1]) < 6000) { //set bounds if distance at equator is < 6k km
              bounds = new google.maps.LatLngBounds();
              bounds.extend(new google.maps.LatLng(result.extentMin[0], result.extentMin[1]));
              bounds.extend(new google.maps.LatLng(result.extentMax[0], result.extentMax[1]));
              self.map.fitBounds(bounds);
            } else {
              self.map.setZoom(2);
              self.map.setCenter(new google.maps.LatLng(35, -15));
            }
          }
        });
      }
    },

    radians: function(x) {
      return x * Math.PI / 180;
    },

    //Adapted from http://stackoverflow.com/a/1502821
    getDistanceAtEquator: function(lng1, lng2) {
      var R = 6378.137, // Earth’s mean radius in km
          dLong = this.radians(lng2 - lng1),
          a = Math.sin(dLong / 2) * Math.sin(dLong / 2),
          c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      return R * c;
    },

    cartoDBgenerateTile: function() {
      CartoDBLayer.prototype._generateTileJson = function() {
        var core_url = this._generateUrl("tiler"),
            base_url = core_url + '/database/dataportal/table/' + this.options.table_name + '/{z}/{x}/{y}',
            tile_url = base_url + '.png',
            grid_url = base_url + '.grid.json',
            query, style, interactivity;

        // SQL?
        if (this.options.query) {
          query = 'sql=' + encodeURIComponent(this.options.query.replace(/\{\{table_name\}\}/g,this.options.table_name));
          tile_url = this._addUrlData(tile_url, query);
          grid_url = this._addUrlData(grid_url, query);
        }

        // STYLE?
        if (this.options.tile_style) {
          style = 'style=' + encodeURIComponent(this.options.tile_style.replace(/\{\{table_name\}\}/g,this.options.table_name));
          tile_url = this._addUrlData(tile_url, style);
          grid_url = this._addUrlData(grid_url, style);
        }

        // INTERACTIVITY?
        if (this.options.interactivity) {
          interactivity = 'interactivity=' + encodeURIComponent(this.options.interactivity.replace(/ /g,''));
          tile_url = this._addUrlData(tile_url, interactivity);
          grid_url = this._addUrlData(grid_url, interactivity);
        }

        // Build up the tileJSON
        return {
          blankImage: '../img/blank_tile.png',
          tilejson: '1.0.0',
          scheme: 'xyz',
          name: this.options.table_name,
          tiles: [tile_url],
          grids: [grid_url],
          tiles_base: tile_url,
          grids_base: grid_url,
          opacity: this.options.opacity,
          formatter: function(options, data) {
            return data;
          }
        };
      };
    },

    setupMap: function(obj) {

      _.defaults(obj, {
        mapCanvasId : "map_canvas",
        tilerDomain : "tiles.example.com",
        tilerProtocol : "http",
        tilerPort : 80,
        mapQuery : "",
        mapCenter : [0,0],
        mapZoom : 3
      });

      this.map = new google.maps.Map($('#' + obj.mapCanvasId)[0], {
        center: new google.maps.LatLng(obj.mapCenter[0],obj.mapCenter[1]),
        defaults: {
          editable: true,
          strokeColor: '#0B0B09',
          fillColor: '#E7E7E7',
          fillOpacity: 0.4
        },
        zoom: obj.mapZoom,
        mapTypeId: google.maps.MapTypeId.TERRAIN,
        mapTypeControl: true
      });

      this.marker = new google.maps.Marker({
        position: null,
        map: this.map
      });

      this.addDrawingManager();
      this.addDrawingListeners();

      this.cartodb_gmapsv3 = new CartoDBLayer({
        map: this.map,
        marker: this.marker,
        table_name: 'occurrence',
        interactivity: 'auto_id',
        query: obj.mapQuery,
        map_style: false,
        infowindow: true,
        auto_bound: true,
        featureClick: this.onMapClick,
        tiler_domain: obj.tilerDomain,
        tiler_port: parseInt(obj.tilerPort,10),
        tiler_protocol: obj.tilerProtocol,
        featureOver: function() {
          this.map.setOptions({draggableCursor: 'pointer'});
        },
        featureOut: function() {
          this.map.setOptions({draggableCursor: 'default'});
        }
      });
    },

    onMapClick: function(e, latlng, pos, data) {
      var url = EXPLORER.settings.baseUrl + '/' + EXPLORER.settings.locale + '/' + EXPLORER.i18n.getLanguageResource('url.occurrence-preview') + '/' + data.auto_id;
      this.marker.setPosition(latlng);
      $.get(url,'context=map')
        .success(function(htmlFragment){
          EXPLORER.preview.replacePreviewContent(htmlFragment);
          EXPLORER.preview.show();
        })
         .error(function(jqXHR, textStatus, errorThrown){
           console.log(textStatus+':'+errorThrown);
        });
    },

    addDrawingManager: function() {
      this.drawing_manager = new google.maps.drawing.DrawingManager({
        drawingControl: true,
        drawingControlOptions: {
          position: google.maps.ControlPosition.TOP_CENTER,
          drawingModes: [
            google.maps.drawing.OverlayType.CIRCLE,
            google.maps.drawing.OverlayType.RECTANGLE,
            google.maps.drawing.OverlayType.POLYGON
          ]
        },
        circleOptions: this.map.defaults,
        rectangleOptions: this.map.defaults,
        polygonOptions: this.map.defaults
      });

      this.drawing_manager.setMap(this.map);
    },

    addDrawingListeners: function() {
      var self = this;

      google.maps.event.addListener(this.drawing_manager, 'drawingmode_changed', function() {
        if(self.drawing_manager.drawingMode) {
          self.removeDrawingOverlay();
          self.removeSpatialFilter();
          self.cartodb_gmapsv3.setInteraction(false);
        }
      });

      google.maps.event.addListener(this.drawing_manager, "overlaycomplete", function(e) {
        self.drawing_manager.setOptions({ drawingMode: null });
        self.drawing_overlay = e.overlay;
        self.filter = self.addFilter(e.type);
        self.addDrawingOverlayListeners(e.type);
        self.cartodb_gmapsv3.setInteraction(true);
      });
    },

    addDrawingOverlayListeners: function(type) {
      var self = this;

      switch(type) {
        case 'circle':
          $.each(["center", "bounds"], function() {
            google.maps.event.addListener(self.drawing_overlay, this+"_changed", function() {
              EXPLORER.backbone.updateActiveFilter(self.filter, { valueList : self.circleParameters() });
            });
          });
        break;

        case 'rectangle':
          google.maps.event.addListener(self.drawing_overlay, 'bounds_changed', function() {
            EXPLORER.backbone.updateActiveFilter(self.filter, { valueList : self.rectangleParameters() });
          });
        break;

        case 'polygon':
          $.each(["insert", "remove", "set"], function() {
            google.maps.event.addListener(self.drawing_overlay.getPath(), this+"_at", function() {
              EXPLORER.backbone.updateActiveFilter(self.filter, { valueList : self.polygonParameters() });
            });
          });
        break;
      }
    },

    circleParameters: function() {
      var o = this.drawing_overlay;
      return [o.getCenter().lat()+','+o.getCenter().lng(),o.getRadius()];
    },

    rectangleParameters: function() {
      var o = this.drawing_overlay;
      return [o.getBounds().getNorthEast().lat() + ',' + o.getBounds().getNorthEast().lng(),
        o.getBounds().getSouthWest().lat() + ',' + o.getBounds().getSouthWest().lng()];
    },

    polygonParameters: function() {
      var o = this.drawing_overlay,
          searchValue = $.map(o.getPath().getArray(), function(n) { return [n.lat() + ',' + n.lng()]; });
      searchValue.push(searchValue[0]);
      return searchValue;
    },

    addDrawingOverlay: function(json) {
      var coord, center, options, circle, bbox, bounds, rectangle, paths, vertices, polygon;

      switch(json.type) {
        case this.drawing_types[0]:
          coord = json.data.coords;
          center = new google.maps.LatLng(coord[0], coord[1]);
          options = $.extend({ center : center, radius : json.data.radius }, this.map.defaults);
          circle = new google.maps.Circle(options);

          circle.setMap(this.map);
          this.map.fitBounds(circle.getBounds());
          this.drawing_overlay = circle;
        break;

        case this.drawing_types[1]:
          //bbox is an array of vertices eg [[lat0,lng0], [lat1,lng1]] expressed as [SW, NE]
          bbox = json.data.coords;
          bounds = new google.maps.LatLngBounds(
            new google.maps.LatLng(bbox[0][0], bbox[0][1]),
            new google.maps.LatLng(bbox[1][0], bbox[1][1])
          );
          options = $.extend({ bounds : bounds }, this.map.defaults);
          rectangle = new google.maps.Rectangle(options);

          rectangle.setMap(this.map);
          this.map.fitBounds(rectangle.getBounds());
          this.drawing_overlay = rectangle;
        break;

        case this.drawing_types[2]:
          //vertices is an array of arrays where last member is identical to first eg [[lat0,lng0], [lat1,lng1], [lat0,lng0]]
          vertices = json.data.coords;
          paths = $.map(vertices, function(n) { return new google.maps.LatLng(n[0], n[1]); });
          options = $.extend({ paths: paths }, this.map.defaults);
          polygon = new google.maps.Polygon(options);

          polygon.setMap(this.map);
          this.map.fitBounds(polygon.getBounds());
          this.drawing_overlay = polygon;
        break;

      }
    },

    removeDrawingOverlay: function() {
      if(!$.isEmptyObject(this.drawing_overlay)) {
        this.drawing_overlay.setMap(null);
      }
      this.drawing_overlay = {};
    },

    removeSpatialFilter: function() {
      $.each(this.drawing_types, function() {
        EXPLORER.backbone.removeFilter({ searchableFieldName: this });
      });
    },

    addFilter: function(type) {
      var filter = {};
      switch(type) {
        case 'circle':
          filter = EXPLORER.backbone.addActiveFilter({ searchableFieldName : 'geoellipse', valueList : this.circleParameters() });
        break;

        case 'rectangle':
          filter = EXPLORER.backbone.addActiveFilter({ searchableFieldName : 'georectangle', valueList : this.rectangleParameters() });
        break;

        case 'polygon':
          filter = EXPLORER.backbone.addActiveFilter({searchableFieldName : 'geopolygon', valueList : this.polygonParameters() });
        break;
      }
      return filter;
    }

  };

  return {
    init: function() { _private.init(); },
    setupMap: function(obj) {
      _private.setupMap(obj);
    },
    setBounds: function(obj) {
      _private.setBounds(obj);
    }
  };

}());