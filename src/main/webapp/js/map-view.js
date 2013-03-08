var occurrenceMap  = (function($){
	//We override some function from CartoDBLayer to use our own tiler.
	/**
	* OVERRIDE
	* Zoom to cartodb geometries
	*/
	CartoDBLayer.prototype.setBounds = function() {
		var self = this;
		// Zoom to your geometries
		  $.ajax({
			method:'get',
		    url: 'mapcenter?q='+(self.options.query|| ''),
		    dataType: 'json',
		    success: function(result) {
		      if(result[0]) {
	              var lon0 = result[0]
	                , lat0 = result[1]
	              var center = new google.maps.LatLng(lat0, lon0);
	    		  self.options.map.setCenter(center);
		      }
		      else{
		    	  var center = new google.maps.LatLng(45.5,-73.5);
		    	  self.options.map.map.setCenter(center);
		      }
		    },
		    error: function(e) {}
		  });
	}
	
	/**
	* OVERRIDE
	* Generate tilejson for wax
	* @return {Object} Options for ImageMapType
	*/
    CartoDBLayer.prototype._generateTileJson = function () {
      var core_url = this._generateUrl("tiler")
        , base_url = core_url + '/database/dataportal/table/' + this.options.table_name + '/{z}/{x}/{y}'
        , tile_url = base_url + '.png'
        , grid_url = base_url + '.grid.json'
      
      // SQL?
      if (this.options.query) {
        var query = 'sql=' + encodeURIComponent(this.options.query.replace(/\{\{table_name\}\}/g,this.options.table_name));
        tile_url = this._addUrlData(tile_url, query);
        grid_url = this._addUrlData(grid_url, query);
      }

      // STYLE?
      if (this.options.tile_style) {
        var style = 'style=' + encodeURIComponent(this.options.tile_style.replace(/\{\{table_name\}\}/g,this.options.table_name));
        tile_url = this._addUrlData(tile_url, style);
        grid_url = this._addUrlData(grid_url, style);
      }

      // INTERACTIVITY?
      if (this.options.interactivity) {
        var interactivity = 'interactivity=' + encodeURIComponent(this.options.interactivity.replace(/ /g,''));
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
          return data
        }
      };
    }
	// Setup map component
	function setupMap(previewElementId,mapCanvasId,mapQuery) {
		var $previewElement = $("#"+previewElementId);
		var lastAutoId = -1;
		var map = new google.maps.Map(document.getElementById(mapCanvasId), {
		  center: new google.maps.LatLng(45.5,-73.5),
		  zoom: 3,
		  mapTypeId: google.maps.MapTypeId.TERRAIN,
		  mapTypeControl: true
		});
	
		var marker = new google.maps.Marker({
		    position: null,
		    map: map
		});
	
		cartodb_gmapsv3 = new CartoDBLayer({
		  map: map,
		  table_name: 'occurrence',
		  interactivity:'auto_id',
		  query: mapQuery,
		  map_style: false,
		  infowindow: true,
		  auto_bound: true,
		  featureClick:onMapClick,
		  tiler_domain:'tiles.canadensys.net',
		  featureOver: function(ev, latlng, pos, data) {
	          map.setOptions({draggableCursor: 'pointer'});
	      },
	      featureOut: function() {
	          map.setOptions({draggableCursor: 'default'});
	      }
		});
	
		function onMapClick(ev, latlng, pos, data) {
			if(this.lastAutoId === data.auto_id){
				marker.setPosition(null);
				occurrencePreview.togglePreview(this.lastAutoId,data.auto_id);
				this.lastAutoId = -1;
				return;
			}
			marker.setPosition(latlng);
			var req = $.get('occurrence-summary/'+data.auto_id,'context=map')
				.success(function(json){
					occurrencePreview.replacePreviewContent(json);
					occurrencePreview.togglePreview(undefined,data.auto_id);
				})
			   .error(function(jqXHR, textStatus, errorThrown){
				   console.log(textStatus+':'+errorThrown);
				});
			this.lastAutoId = data.auto_id;
		}
	}
	
//Public methods
  return {
	  setupMap : setupMap
	};
}(jQuery));
