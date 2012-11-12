var occurrencePreview = (function($){
	var $occPreview, $closeElement, $sideBar;
	var sidebarTop;
	
	//initialize will run in document.ready
	$(function initialize(){
		$sideBar = $('#control_wrapper');
		$occPreview = $('#occ_preview',$sideBar);
		$closeElement = $('#preview_close',$sideBar);
		
		//if the module exists in the HTML
		if($occPreview.length && $closeElement.length){
			sidebarTop = $sideBar.offset().top - 36;
			loadEvents();
		}
	});
	
	function loadEvents() {
		$closeElement.click(function(){  
			$occPreview.hide('slide',500);
		});
		
		$(window).scroll(function(event) {
			if ($(this).scrollTop() >= sidebarTop) {
				$occPreview.addClass('fixed');
			} else {
				$occPreview.removeClass('fixed');
			}
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
	
	function replacePreviewContent(json){
		for (var key in json) {
			if (json.hasOwnProperty(key)) {
				$("#"+key,$occPreview).html(json[key]);
			}
		}
		
		//special elements
		var $occPreviewLinks = $('.occ_preview_links','#occ_preview_content');
		var $viewSourceRecordLink = $('#viewsourcerecord', $occPreviewLinks);
		var $viewFullRecordLink = $('#viewfullrecord', $occPreviewLinks);
		
		$viewFullRecordLink.attr('href', 'd/'+json['sourcefileid']+'/'+json['occurrenceid']);
		
		if(json['_references']){
			$viewSourceRecordLink.attr('href', json['_references']);
			$viewSourceRecordLink.closest('span').show();
		}
		else{
			$viewSourceRecordLink.closest('span').hide();
		}
		
		var dateRangeStr = canadensysUtils.formatDate(json['syear'],json['smonth'],json['sday']);
		var endDate = canadensysUtils.formatDate(json['eyear'],json['emonth'],json['eday']);
		if(endDate){
			dateRangeStr =+ '/' + endDate;
		}
		var $daterange = $("#daterange","#occ_preview_content");
		$daterange.html(dateRangeStr);
		
		var altituteStr = "";
		//since 0 convert to false but it's a valid value
		if(json['minimumelevationinmeters'] || json['minimumelevationinmeters']===0){
			altituteStr = json['minimumelevationinmeters'];
			if((json['maximumelevationinmeters'] || json['minimumelevationinmeters']===0) &&
					json['maximumelevationinmeters'] !== json['minimumelevationinmeters']){
				altituteStr += '-'+json['maximumelevationinmeters'];
			}
			altituteStr += ' m';
		}
		var $altituderange = $('#altituderange','#occ_preview_content');
		$altituderange.html(altituteStr);
		
		var $occPreviewMedia = $("#occ_preview_media","#occ_preview_content");
		$occPreviewMedia.empty();
		if(json['associatedmedia']){
			var associatedmedia = json['associatedmedia'].split('; ');
			for (var i = 0; i < associatedmedia.length; i++) {
				var href = $('<a />');
				href.attr('href', associatedmedia[i]).attr('target', '_blank');
				var image = $('<img />');
				image.attr('src', associatedmedia[i]).attr('alt','Image ' + (i+1));
				href.append(image);
				$occPreviewMedia.append(href);
			}
		}
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
		$('#control_buttons a').click(function() {
			var id = $(this).attr("href");
			$("#control_buttons a").removeClass("selected");
			$(this).addClass("selected");
			$("#control_bar > div").addClass("hidden");
			$(id).removeClass("hidden");
			return false;
		});
	}
	
	/**
	 * This function is to restore the display settings from the cookie
	 */
	function restoreDisplay() {
		if($('#display_columns').length){
			$('#display_columns').on('click', ':checkbox', function() {
				var selectedColumn = [];
				$('#display_columns :checkbox:checked').each(function(index) {
					selectedColumn.push($(this).val());
				});
				canadensysUtils.createCookie(COOKIE_NAME,selectedColumn.join('|'),7);
			});
			
			var settingsCookie = canadensysUtils.readCookie(COOKIE_NAME);
			if(settingsCookie){
				//we cannot store a ; in the cookie so use a pipe (|)
				var arrayOfSettings = settingsCookie.split('|');
				$('#display_columns :checkbox').each(function(index) {
					var isSelected = _.include(arrayOfSettings, $(this).val());
					if(isSelected !== this.checked){
						//need to click to update the table display
						this.click();
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
	
	var $occPreview, $results;
	
	//initialize will run in document.ready
	$(function initialize(){
		$occPreview = $('#occ_preview');
		$results = $('#results');
		//if the module exists in the HTML
		if($occPreview.length && $results.length){
			loadEvents();
		}
	});
	
	function loadEvents() {
		var $oldSelection;
		$results.delegate('tbody tr', 'click', function(event) {
			//TODO:switch to deferred like livesearch
			
			//do not send query to the server for the same element
			if(!$oldSelection || $oldSelection.attr('id') !== $(this).attr('id')){
				$.ajax({
					type: "GET",
					url: "occurrence-summary/"+$(this).attr("id"),
					dataType: "json",
					//data: mapParam,
					context: document.body,
					success: function(json){
						occurrencePreview.replacePreviewContent(json);
					},
					error: function(jqXHR, textStatus, errorThrown){
						alert(textStatus);
					}
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
		$('#dwc_table_toggle').click(function() {
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