var occurrencePreview = (function($){
	var $occPreview, $closeElement, $sideBar;
	var sidebarTop;
	
	//initialize will run in document.ready
	$(function initialize(){
		$sideBar = $('#control_wrapper');
		$occPreview = $('#occ_preview');
		$closeElement = $('#preview_close');
		
		//if the module exists in the HTML
		if($occPreview.length && $closeElement.length){
			sidebarTop = $sideBar.offset().top - 36;
			loadEvents();
		}
	});
	
	function loadEvents() {
    initClosePreview();
		$(window).scroll(function(event) {
			if ($(this).scrollTop() >= sidebarTop) {
				$occPreview.addClass('fixed');
			} else {
				$occPreview.removeClass('fixed');
			}
		});
	}
	
	function initClosePreview() {
	  $('#preview_close').on('click', function(){
			$occPreview.hide('slide',500);
		});
	}
	
	function togglePreview(oldSelectionId,newSelectionId){
		//check if the click is on the same element
		if(oldSelectionId && (oldSelectionId === newSelectionId)){
			if($occPreview.is(":visible")){
				$occPreview.hide("slide",500);
			}
			else{
				$occPreview.show("slide",500);
			}
		}
		else{
			//check if the component is not already shown
			if(!$occPreview.is(":visible")){
				$occPreview.show("slide",500);
			}
		}
	}
	
	function replacePreviewContent(htmlFragment){
		$occPreview.html(htmlFragment);
		initClosePreview();
	}
	
	//Public methods
    return {
    	togglePreview : togglePreview,
    	replacePreviewContent : replacePreviewContent
	};
}(jQuery));


var occurrenceControl = (function($){
	var $control_buttons;
	var COOKIE_NAME = 'canadensys-responsive-table-settings';
	
	//initialize will run in document.ready
	$(function initialize(){
		$control_buttons = $('#control_buttons');
		
		//if the module exists in the HTML
		if($control_buttons.length){
			loadEvents();
		}
	});
	
	function loadEvents() {
		$('#control_buttons').on('click', 'a', function() {
		  var id = $(this).attr("href");
			$(this).addClass("selected").parent().siblings().children().removeClass("selected");
			$(id).removeClass("hidden").siblings().addClass("hidden");
			return false;
		});
	}
	
	/**
	 * This function is to restore the display settings from the cookie
	 */
	function restoreDisplay() {
	  var $display = $('#display_columns');
		if($display.length){
			$($display).on('click', 'input:checkbox', function() {
				var selectedColumn = [];
				$.each($display.find('input:checked'), function() {
					selectedColumn.push($(this).val());
				});
				canadensysUtils.createCookie(COOKIE_NAME,selectedColumn.join('|'),7);
			});
			
			var settingsCookie = canadensysUtils.readCookie(COOKIE_NAME);
			if(settingsCookie){
				//we cannot store a ; in the cookie so use a pipe (|)
				var arrayOfSettings = settingsCookie.split('|');
				$.each($display.find('input:checkbox'), function() {
					var isSelected = _.include(arrayOfSettings, $(this).val());
					if(isSelected !== this.checked){
						//need to click to update the table display
						this.trigger('click');
					}
				});
			}
		}
	}
	//Public methods
    return {
    	restoreDisplay : restoreDisplay
	};
}(jQuery));

var occurrenceTable  = (function($){
	
	var $results;
	
	//initialize will run in document.ready
	$(function initialize(){
		$results = $('#results');
		$occPreview = $('#occ_preview');
		//if the module exists in the HTML
		if($occPreview.length && $results.length){
			loadEvents();
		}
	});
	
	function loadEvents() {
		var $oldSelection;
    		
		$results.on('click', 'tbody tr', function() {
			
			//do not send query to the server for the same element
			if(!$oldSelection || $oldSelection.attr('id') !== $(this).attr('id')){
			
				$.get("occurrence-preview/"+$(this).attr("id"), function(htmlFragment) {
					occurrencePreview.replacePreviewContent(htmlFragment);
				})
				.fail(function(jqXHR, textStatus, errorThrown) {
    				console.log( textStatus );
    			});
			}
			
			if($oldSelection){
				occurrencePreview.togglePreview($oldSelection.attr('id'),$(this).attr('id'));
			}
			else{
				occurrencePreview.togglePreview(undefined,$(this).attr('id'));
			}
			
			if($oldSelection){
				$oldSelection.toggleClass('selected');
			}
			$(this).toggleClass('selected');
			$oldSelection = $(this); 
		});
		
		//responsive design
		$results.table({
	      	idprefix: "co-",
			persist: "persist"
		});
	}
}(jQuery));

var occurrenceDetails  = (function($){
	var $dwcTable;
	//initialize will run in document.ready
	$(function initialize(){
		$dwcTable = $('#dwc_table');
		//if the module exists in the HTML
		if($dwcTable.length){
			loadEvents();
		}
	});
	
	function loadEvents() {
		$('#dwc_table_toggle').on('click', function() {
			$('tr.unused').toggle();
		});
	}
	
	function setupSingleOccurrenceMap(mapCanvasId,lat,lng,uncertainty) {
		
		if(!lat || !lng){
			$('#'+mapCanvasId).addClass('no_geo');
			return;
		}
		
		var latlng = new google.maps.LatLng(lat,lng);
		var map = new google.maps.Map(document.getElementById(mapCanvasId), {
		  center: latlng,
		  zoom: 12,
		  scaleControl: true,
		  mapTypeId: google.maps.MapTypeId.TERRAIN,
		  mapTypeControl: true
		});
	
		var marker = new google.maps.Marker({
		    position:latlng,
		    map: map
		});
		
		var coordinateUncertainty = new google.maps.Circle({
			map: map,
			center:latlng,
			radius: uncertainty,
			strokeWeight: 1
//			strokeColor: 'white',
//			strokeOpacity: 0.5,
//			fillColor: '#2C48A6',
//			fillOpacity: 0.2
         });
	}
	
	//Public methods
    return {
    	setupSingleOccurrenceMap : setupSingleOccurrenceMap
	};
}(jQuery));