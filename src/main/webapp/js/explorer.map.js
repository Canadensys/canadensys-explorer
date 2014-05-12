/****************************
Copyright (c) 2013 Canadensys
Explorer Map
****************************/
/*global EXPLORER, $, window, document, console, jQuery, CartoDBLayer, google, _, Wkt */

EXPLORER.map = (function() {

  'use strict';

  var _private = {

    map: {},
    marker: {},
    cartodb_gmapsv3: {},
    drawing_manager: {},
    drawing_overlays: [],
    center: new google.maps.LatLng(45.5,-73.5),

    init: function() {
      this.cartoDBsetBounds();
      this.cartoDBgenerateTile();
    },

    cartoDBsetBounds: function() {
      CartoDBLayer.prototype.setBounds = function() {
        var self = this, lon0, lat0;

        $.ajax({
          method:'get',
          url: 'mapcenter?q='+encodeURIComponent(self.options.query|| ''),
          dataType: 'json',
          success: function(result) {
            if(result && result[0]) {
              lon0 = result[0];
              lat0 = result[1];
              self.center = new google.maps.LatLng(lat0, lon0);
            }
            self.options.map.setCenter(self.center);
          }
        });
      };
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
        mapQuery : ""
      });

      this.map = new google.maps.Map($('#' + obj.mapCanvasId)[0], {
        center: new google.maps.LatLng(45.5,-73.5),
        zoom: 3,
        mapTypeId: google.maps.MapTypeId.TERRAIN,
        mapTypeControl: true
      });

      this.marker = new google.maps.Marker({
        position: null,
        map: this.map
      });

      this.createDrawingManager();

      this.cartodb_gmapsv3 = new CartoDBLayer({
        map: this.map,
        marker: this.marker,
        drawing_overlays: this.drawing_overlays,
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
      this.marker.setPosition(latlng);
      $.get('occurrence-preview/'+data.auto_id,'context=map')
        .success(function(htmlFragment){
          EXPLORER.preview.replacePreviewContent(htmlFragment);
          EXPLORER.preview.show();
        })
         .error(function(jqXHR, textStatus, errorThrown){
           console.log(textStatus+':'+errorThrown);
        });
    },

    createDrawingManager: function() {
      var self = this;

      this.drawing_manager = new google.maps.drawing.DrawingManager({
        drawingControl: true,
        drawingControlOptions: {
          position: google.maps.ControlPosition.TOP_CENTER,
          drawingModes: [
            google.maps.drawing.OverlayType.CIRCLE,
            google.maps.drawing.OverlayType.RECTANGLE,
            google.maps.drawing.OverlayType.POLYGON
          ]
        }
      });

      this.drawing_manager.setMap(this.map);

      google.maps.event.addListener(this.drawing_manager, 'drawingmode_changed', function() {
        if(self.drawing_overlays.length > 0 && self.drawing_manager.drawingMode) {
          self.clearDrawingOverlays();
          self.removeSpatialFilters();
          self.cartodb_gmapsv3.setInteraction(false);
        }
      });
      google.maps.event.addListener(this.drawing_manager, "overlaycomplete", function(e) {
        self.drawingDone(e, self);
        self.cartodb_gmapsv3.setInteraction(true);
      });
    },

    createDrawingOverlay: function(json) {
      switch(json.type) {
        case 'geoellipse':
          var coord = json.data.coords,
              center = new google.maps.LatLng(coord[0], coord[1]),
              options = {
                center : center,
                radius : json.data.radius,
                fillOpacity : 0.25
              },
              circle = new google.maps.Circle(options);

          circle.setMap(this.map);
          this.drawing_overlays.push(circle);
        break;

        case 'georectangle':
          //coords is an array of vertices eg [[lat0,lng0], [lat1,lng1]] expressed as [SW, NE]
          var bbox = json.data.coords,
              bounds = new google.maps.LatLngBounds(
                new google.maps.LatLng(bbox[0][0], bbox[0][1]),
                new google.maps.LatLng(bbox[1][0], bbox[1][1])
              ),
              options = {
                bounds : bounds,
                fillOpacity : 0.25
              },
              rectangle = new google.maps.Rectangle(options);

          rectangle.setMap(this.map);
          this.drawing_overlays.push(rectangle);
        break;

        case 'geopolygon':
         //coords is an array of vertices & last member is same as first eg [[lat0,lng0], [lat1,lng1], [lat0,lng0]]
          var vertices = json.data.coords,
              paths = $.map(vertices, function(n) { return new google.maps.LatLng(n[0], n[1]); }),
              polygon = new google.maps.Polygon({
                paths: paths,
                fillOpacity : 0.25
              });

          polygon.setMap(this.map);
          this.drawing_overlays.push(polygon);
        break;

      }
    },

    clearDrawingOverlays: function() {
      $.each(this.drawing_overlays, function() {
        if(this.hasOwnProperty('overlay')) {
          this.overlay.setMap(null);
        } else {
          this.setMap(null);
        }
      });
      this.drawing_overlays = [];
    },

    removeSpatialFilters: function() {
      $.each(["geoellipse", "georectangle", "geopolygon"], function() {
        EXPLORER.backbone.removeFilter({ searchableFieldName: this });
      });
    },

    drawingDone: function(e, scope) {
      var searchValue = [];

      scope.drawing_manager.setOptions({ drawingMode: null });
      scope.drawing_overlays.push(e);

      switch(e.type) {
        case 'circle':
          searchValue = [e.overlay.getCenter().lat() +','+e.overlay.getCenter().lng(),e.overlay.getRadius()];
          EXPLORER.backbone.addActiveFilter('geoellipse', searchValue, {valueText:'map'});
        break;

        case 'rectangle':
          //searchValue must be ["minLat,minLong","maxLat,maxLong"]
          searchValue = [e.overlay.getBounds().getNorthEast().lat() +', '+e.overlay.getBounds().getNorthEast().lng(),
          e.overlay.getBounds().getSouthWest().lat() +', '+e.overlay.getBounds().getSouthWest().lng()];
          EXPLORER.backbone.addActiveFilter('georectangle', searchValue, {valueText:'map'});
        break;

        case 'polygon':
          searchValue = $.map(e.overlay.getPath().getArray(), function(n) { return [n.lat()+', '+n.lng()]; });
          searchValue.push(searchValue[0]);
          EXPLORER.backbone.addActiveFilter('geopolygon', searchValue, {valueText:'map'});
        break;
      }
    }

  };

  return {
    init: function() { _private.init(); },
    setupMap: function(obj) {
      _private.setupMap(obj);
    },
    createDrawingOverlay: function(json) {
      _private.createDrawingOverlay(json);
    }
  };

}());