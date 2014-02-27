/****************************
Copyright (c) 2013 Canadensys
Explorer Map
****************************/
/*global EXPLORER, $, window, document, console, jQuery, CartoDBLayer, google*/

EXPLORER.map = (function() {

  'use strict';

  var _private = {

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
          url: 'mapcenter?q='+(self.options.query|| ''),
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

    setupMap: function(previewElementId, mapCanvasId, tileServer, mapQuery) {
      var lastAutoId = -1, map, marker, cartodb_gmapsv3;

      map = new google.maps.Map($('#' + mapCanvasId)[0], {
        center: new google.maps.LatLng(45.5,-73.5),
        zoom: 3,
        mapTypeId: google.maps.MapTypeId.TERRAIN,
        mapTypeControl: true
      });

      marker = new google.maps.Marker({
        position: null,
        map: map
      });

      function onMapClick(e, latlng, pos, data) {
        if(lastAutoId === data.auto_id){
          marker.setPosition(null);
          EXPLORER.preview.togglePreview(this.lastAutoId,data.auto_id);
          lastAutoId = -1;
          return;
        }
        marker.setPosition(latlng);
        $.get('occurrence-preview/'+data.auto_id,'context=map')
          .success(function(htmlFragment){
            EXPLORER.preview.replacePreviewContent(htmlFragment);
            EXPLORER.preview.togglePreview(undefined,data.auto_id);
          })
           .error(function(jqXHR, textStatus, errorThrown){
             console.log(textStatus+':'+errorThrown);
          });
        lastAutoId = data.auto_id;
      }

      cartodb_gmapsv3 = new CartoDBLayer({
        map: map,
        table_name: 'occurrence',
        interactivity:'auto_id',
        query: mapQuery,
        map_style: false,
        infowindow: true,
        auto_bound: true,
        featureClick: onMapClick,
        tiler_domain: tileServer.substr(tileServer.indexOf('://')+3),
        featureOver: function() {
          map.setOptions({draggableCursor: 'pointer'});
        },
        featureOut: function() {
          map.setOptions({draggableCursor: 'default'});
        }
      });

    }

  };

  return {
    init: function() { _private.init(); },
    setupMap: function(previewElementId, mapCanvasId, tileServer, mapQuery) {
      _private.setupMap(previewElementId, mapCanvasId, tileServer, mapQuery);
    }
  };

}());