//Some language resources used by dynamic view
var languageResources = (function($){
	var languageResources;
	
	function setLanguageResources(_languageResources){
		languageResources = _languageResources;
	}
	
	function getLanguageResource(key){
		return languageResources[key];
	}
	
	//Public methods
    return {
    	setLanguageResources : setLanguageResources,
    	getLanguageResource : getLanguageResource
	};
}(jQuery));

var searchAndFilter = (function($){
	var filterViewTemplate = _.template($('#filter_template_current').html());
	
	FilterKey = Backbone.Model.extend({
	  defaults: {
	    searchableFieldId: -1,
	    searchableFieldName : undefined,
	    searchableFieldText : undefined
	  }
	});
	
	FilterItem = Backbone.Model.extend({
	  defaults: {
	    searchableFieldId: -1,
	    searchableFieldName: undefined,
	    searchableFieldText: undefined,
	    groupId : -1,
	    op: undefined,
	    opText : undefined,
	    value: [],
	    valueJSON : undefined,
	    valueText : undefined
	  }
	});
	
	SearchResult = Backbone.Model.extend({
	  defaults: {
	    numberOfRecord: 0
	  }
	});
	
	TextSearch = Backbone.Model.extend({
	  defaults: {
	    currentText: undefined,
	    validate:false
	  }
	});
	
	FilterList = Backbone.Collection.extend({
	  model: FilterItem
	});
	
	var filterList = new FilterList();
	var currFilterKey = new FilterKey();
	var currSearchResult = new SearchResult();
	var currTextSearch = new TextSearch();
	
	var availableSearchFields;
	var initialFilterParamMap;
	var DEFAULT_SEARCHABLE_FIELD_ID = 16; //scientificName
	
	function setNumberOfResult(numberOfRecord){
		currSearchResult.set("numberOfRecord",numberOfRecord);
	}
	
	function setAvailableSearchFields(_availableSearchFields){
		availableSearchFields = _availableSearchFields;
	}
	
	//load filter form outer source
	function loadFilter(json){
		var currFilter;
		var filterItem;
		var lastSearchableFieldId;
		for (var key in json) {
			if (json.hasOwnProperty(key)) {
				filterItem = new FilterItem();
				filterItem.set(json[key]);
				//make sure the id is a String
				filterItem.set('searchableFieldId',json[key].searchableFieldId.toString());
				filterItem.set('searchableFieldText', getAvailableFieldText(json[key].searchableFieldName)),
				filterItem.set('value',json[key].valueList);
				filterItem.set('valueJSON',JSON.stringify(json[key].valueList));
				filterItem.set('opText',getOperatorText(json[key].op));
				filterItem.set('valueText',safeGetValueText(json[key].valueList));
				filterList.add(filterItem);
				lastSearchableFieldId = json[key].searchableFieldId.toString();
		    }
		}
		//keep the initial state, the other solution could be to receive this from the server
		initialFilterParamMap = $('form',$('#search')).serializeArray();
		
		if(!lastSearchableFieldId){
			lastSearchableFieldId = DEFAULT_SEARCHABLE_FIELD_ID;
		}
		currFilterKey.set({searchableFieldId:lastSearchableFieldId,
			searchableFieldName:availableSearchFields[lastSearchableFieldId].searchableFieldName,
			searchableFieldText:getAvailableFieldText(availableSearchFields[lastSearchableFieldId].searchableFieldName)});
	}
	
	function getInitialFilterParamMap(){
		return initialFilterParamMap;
	}
	//TODO move to canadensysUtils
	function isWithinYearLimit(year){
		return (year >0 && year < 9999);
	}
	function isWithinMonthLimit(month){
		return (month >=1 && month <=12);
	}
	function isWithinDayLimit(day){
		return (day >=1 && day <=31);
	}
	
	function isValidNumber(number){
		if(isNaN(number)){
			return false;
		}
		return true;
	}
	
	function isValidEmail(email){
		return /^\S+@\S+\.\S+$/i.test(email);
	}
	
	function isValidDateElement($el){
		var val = $el.val();
		//set the base of parseInt to 10 to accept int like 08
		var valAsInt = parseInt(val,10);
		//make sure it's a number
		if(isNaN(valAsInt)){
			return false;
		}
		
		if($el.hasClass('validationYear')){
			return isWithinYearLimit(valAsInt);
		}
		if($el.hasClass('validationMonth')){
			return isWithinMonthLimit(valAsInt);
		}
		if($el.hasClass('validationDay')){
			return isWithinDayLimit(valAsInt);
		}
	}
	
	function isValidPartialDate(year,month,day){
		//set the base of parseInt to 10 to accept int like 08
		var yAsInt = parseInt(year,10);
		var mAsInt = parseInt(month,10);
		var dAsInt = parseInt(day,10);
		
		//accept all partial dates for year alone and year/month if the day is not specified
		if(!isNaN(yAsInt) && isNaN(dAsInt)){
			return isWithinYearLimit(yAsInt) && (isNaN(mAsInt) || isWithinMonthLimit(mAsInt));
		}
		
		//accept all partial dates for month/day
		if(isNaN(yAsInt) && !isNaN(mAsInt) && !isNaN(dAsInt)){
			return isWithinMonthLimit(mAsInt) && isWithinDayLimit(dAsInt);
		}
		
		//accept all partial dates for month alone
		if(isNaN(yAsInt) && !isNaN(mAsInt) && isNaN(dAsInt)){
			return isWithinMonthLimit(mAsInt);
		}
		
		//accept all partial dates for day alone
		if(isNaN(yAsInt) && isNaN(mAsInt) && !isNaN(dAsInt)){
			return isWithinDayLimit(dAsInt);
		}
		
		if(!isNaN(yAsInt) && !isNaN(mAsInt) && !isNaN(dAsInt)){
			// day 0 means the last day/hour of the previous month, Date is 0 based so we ask for the month later
			var date = new Date(yAsInt, mAsInt,0);
			var numberOfDays = date.getDate();
			if(dAsInt > date.getDate()){
				return false;
			}
			return isWithinYearLimit(yAsInt) && isWithinMonthLimit(mAsInt) && isWithinDayLimit(dAsInt);
		}
		return false;
	}
	
	function getOperatorText(operator){
		var varName = 'operator.'+operator.toLowerCase();
		return languageResources.getLanguageResource(varName);
	}
	function getAvailableFieldText(fieldName){
		var varName = 'filter.'+fieldName.toLowerCase();
		return languageResources.getLanguageResource(varName);
	}
	
	function safeGetValueText(valueList){
		var parsedValueText = [];
		for (var i = 0; i < valueList.length; i++) {
			var varName = 'filter.value.'+valueList[i].toString().toLowerCase();
			if(languageResources.getLanguageResource(varName)){
				parsedValueText.push(languageResources.getLanguageResource(varName));
			}
			else{
				parsedValueText.push(valueList[i]);
			}
		}
		return parsedValueText.join();
	}
	
	//make sure we use the same fields on both date
	function isValidDateInterval(syear,smonth,sday,eyear,emonth,eday){
		return (isNaN(parseInt(syear)) === isNaN(parseInt(eyear)) &&
				isNaN(parseInt(smonth)) === isNaN(parseInt(emonth)) &&
				isNaN(parseInt(sday)) === isNaN(parseInt(eday)));
	}
	
	//View that supports the text entry
	var TextEntryView = Backbone.View.extend({
		textValueTemplate : _.template($('#filter_template_text_input').html()),
		initialize : function() {
			this.setElement(this.textValueTemplate());
		},
		render : function() {
			return this;
		},
		events : {
			"keyup input" : "onTextTyped"
		},
		onTextTyped : function(evt) {
			var value = undefined;
			var enter = undefined;
			if(evt){
				value = $(evt.currentTarget).val();
				currTextSearch.set({currentText:value,validate:(evt.keyCode === 13)});
			}
		}
	});
	
	//View that supports text suggestion
	var TextValueSuggestionView = Backbone.View.extend({
		suggestionValueTemplate : _.template($('#filter_template_suggestions').html()),
		cacheKeys : [],
		cacheMap : {},
		initialize : function() {
			//this.setElement(this.el);
			currTextSearch.bind('change:currentText', this.onTextChanged, this);
		},
		destroy : function(){
			currTextSearch.unbind('change:currentText',this.onTextChanged);
			this.remove();
		},
		render : function() {
			//this.$el.html(this.suggestionValueTemplate());
			this.setElement(this.suggestionValueTemplate());
			this.onTextChanged(currTextSearch);
			return this;
		},
		events : {
			"click #value_suggestions tr" : "createNewSuggestionFilter"
		},
		onTextChanged : function(textSearchModel) {
			var mapParam = {};
			var cacheKey = currFilterKey.get('searchableFieldId');
			var value = textSearchModel.get('currentText');
			
			mapParam['fieldId'] = currFilterKey.get('searchableFieldId');
			
			if(value){
				mapParam['curr'] = value;
				cacheKey += value;
			}
			
			if(this.cacheMap[cacheKey]) {
				this.replaceTableCellContent(this.cacheMap[cacheKey]);
			}
			else{
				var that = this;
				var req = $.get('livesearch',mapParam)
					.success(function(json){
						that.replaceTableCellContent(json);
						
						if(that.cacheKeys.length == 10){
							delete that.cacheMap[that.cacheKeys.shift()];
						}
						that.cacheKeys.push(cacheKey);
						that.cacheMap[cacheKey] = json;
					})
				   .error(function(jqXHR, textStatus, errorThrown){
					   alert(textStatus);
					});
			}
		},
		replaceTableCellContent : function(json){
			var rows = json['rows'];
			var lastRow=0;
			var row;
			var that = this;
			$.each(rows, function(key, val) {
				row = $('#value_suggestions tr:nth-child('+(key+1)+')',that.$el);
				row.find('td:nth-child(1)').html(val['value']);
				row.find('td:nth-child(2)').html(val['occurrence_count']);
				row.removeClass('hidden');
				row.attr('id',val['id']);
				lastRow = key+1; //to match tr index (who starts at 1)
			});
			
			//move to next row
			lastRow++;
			//clear remaining rows
			while(lastRow<=10){
				row = $('#value_suggestions tr:nth-child('+lastRow+')',that.$el);
				row.find('td:nth-child(1)').html(' ');
				row.find('td:nth-child(2)').html(' ');
				row.addClass('hidden');
				row.removeAttr('id');
				lastRow++;
			}
		},
		createNewSuggestionFilter : function(evt) {
			var value = [$(evt.currentTarget).find('td:nth-child(1)').text()];
			var valueJSON = JSON.stringify(value);
			
			//ignore duplicate filter (ignore case)
			if(filterList.find(function(currFilter){ 
				return (currFilter.get('searchableFieldId') === currFilterKey.get('searchableFieldId') &&
						currFilter.get('valueJSON').toLowerCase() === valueJSON.toLowerCase());
				})){
				return;
			}
			
			var newFilter = new FilterItem({
				searchableFieldId : currFilterKey.get('searchableFieldId'),
				searchableFieldName : currFilterKey.get('searchableFieldName'),
				searchableFieldText : getAvailableFieldText(currFilterKey.get('searchableFieldName')),
				op:'EQ',
				opText : getOperatorText('EQ'),
				value : value,
				valueJSON : valueJSON,
				valueText : safeGetValueText(value)
			});
			filterList.add(newFilter);
		}
	});
	
	//View that supports creation of a filter with the LIKE operator
	var PartialTextValueView = Backbone.View.extend({
		partialTextTemplate : _.template($('#filter_template_partial_match').html()),
		initialize : function() {
			currTextSearch.bind('change:currentText', this.onTextChanged, this);
			currTextSearch.bind('change:validate', this.onValidate, this);
			
			//find the like operator
			this.likeOp = _.find(availableSearchFields[currFilterKey.get('searchableFieldId')].supportedOperator,
					function(str){ return str.toLowerCase().search("^[s|e|c]like$") != -1; });
		},
		destroy : function(){
			currTextSearch.unbind('change:currentText', this.onTextChanged);
			currTextSearch.unbind('change:validate', this.onValidate);
			this.remove();
		},
		render : function() {
			this.setElement(this.partialTextTemplate({opText : getOperatorText(this.likeOp)}));
			return this;
		},
		events : {
			//bound to the root element
			"click button" : "createNewLikeFilter"
		},
		onTextChanged : function(textSearchModel) {
			$("#partial_match_value",this.$el).text(textSearchModel.get('currentText'));
		},
		onValidate : function(textSearchModel) {
			if(textSearchModel.get('validate')){
				this.createNewLikeFilter();
			}
		},
		createNewLikeFilter : function(evt) {
			var value = [currTextSearch.get('currentText')];
			var valueJSON = JSON.stringify(value);
			//skip empty filter
			if(!value || value.length === 0){
				return;
			}
			
			//ignore duplicate filter (ignore case)
			if(filterList.find(function(currFilter){ 
				return (currFilter.get('searchableFieldId') === currFilterKey.get('searchableFieldId') &&
						currFilter.get('valueJSON').toLowerCase() === valueJSON.toLowerCase());
				})){
				return;
			}
			var newFilter = new FilterItem({
				searchableFieldId : currFilterKey.get('searchableFieldId'),
				searchableFieldName : currFilterKey.get('searchableFieldName'),
				searchableFieldText : getAvailableFieldText(currFilterKey.get('searchableFieldName')),
				op:this.likeOp,
				opText : getOperatorText(this.likeOp),
				value : value,
				valueJSON : valueJSON,
				valueText : safeGetValueText(value)
			});
			filterList.add(newFilter);
		}
	});
	
	var SelectionValueView = Backbone.View.extend({
		textSelectionTemplate : _.template($('#filter_template_select').html()),
		initialize : function() {
		},
		destroy : function(){
			this.remove();
		},
		render : function() {
			this.setElement(this.textSelectionTemplate());
			this.loadContent(currFilterKey.get('searchableFieldId'));
			return this;
		},
		events : {
			"click button" : "createNewFilter"
		},
		loadContent : function(fieldId) {
			var that = this;
			//could also be loaded with a model fetch
			var req = $.get('getpossiblevalues',{fieldId:fieldId})
				.success(function(json){
					var $select = $("#value_select",this.$el);
					for (var key in json) {
						if (json.hasOwnProperty(key)) {
							var option = $('<option />');
						    option.attr('value', json[key].value).text(json[key].value);
						    $select.append(option);
					    }
					}
				})
			   .error(function(jqXHR, textStatus, errorThrown){
				   alert(textStatus);
				});
		},
		createNewFilter : function(evt) {
			var value = [$("#value_select",this.$el).val()];
			var valueJSON = JSON.stringify(value);
			//skip empty filter
			if(!value || value.length === 0){
				return;
			}
			
			if(filterList.where({searchableFieldId: currFilterKey.get('searchableFieldId'),valueJSON:valueJSON}).length !== 0){
				return;
			}
			var newFilter = new FilterItem({
				searchableFieldId : currFilterKey.get('searchableFieldId'),
				searchableFieldName : currFilterKey.get('searchableFieldName'),
				searchableFieldText : getAvailableFieldText(currFilterKey.get('searchableFieldName')),
				op:'EQ',
				opText : getOperatorText('EQ'),
				value : value,
				valueJSON : valueJSON,
				valueText : safeGetValueText(value)
			});
			filterList.add(newFilter);
		}
	});
	
	//View that supports creation of a filter with the LIKE operator
	var BooleanValueView = Backbone.View.extend({
		booleanValueTemplate : _.template($('#filter_template_boolean_value').html()),
		initialize : function() {

		},
		destroy : function(){
			this.remove();
		},
		render : function() {
			this.setElement(this.booleanValueTemplate({fieldText : currFilterKey.get('searchableFieldText')}));
			return this;
		},
		events : {
			//bound to the root element
			"click button" : "createNewLikeFilter"
		},
		createNewLikeFilter : function(evt) {
			var value =  [($('input[name=boolGroup]:checked',this.$el).val())];
			var valueJSON = JSON.stringify(value);
			//skip empty filter
			if(value.length === 0){
				return;
			}
			
			//ignore duplicate filter, boolean filter must not be already included
			if(filterList.where({searchableFieldId: currFilterKey.get('searchableFieldId')}).length !== 0){
				return;
			}
			
			var newFilter = new FilterItem({
				searchableFieldId : currFilterKey.get('searchableFieldId'),
				searchableFieldName : currFilterKey.get('searchableFieldName'),
				searchableFieldText : getAvailableFieldText(currFilterKey.get('searchableFieldName')),
				op:'EQ',
				opText : getOperatorText('EQ'),
				value : value,
				valueJSON : valueJSON,
				valueText : safeGetValueText(value)
			});
			filterList.add(newFilter);
		}
	});
	
	//Responsible to render the proper view based on the options of the searchable field.
	var TextValueView = Backbone.View.extend({
		textValueTemplate : _.template($('#filter_template_single').html()),
		initialize : function() {
			this.setElement(this.textValueTemplate());
			this.supportSuggestion = availableSearchFields[currFilterKey.get('searchableFieldId')].supportSuggestion;
			this.supportPartialMatch = availableSearchFields[currFilterKey.get('searchableFieldId')].supportPartialMatch;
			this.supportSelectionList = availableSearchFields[currFilterKey.get('searchableFieldId')].supportSelectionList;
			this.isBooleanFilter = (availableSearchFields[currFilterKey.get('searchableFieldId')].type.indexOf("Boolean") != -1);
			this.textValueSuggestionView = undefined;
			this.partialTextValueView = undefined;
			this.selectionValueView = undefined;
			this.booleanValueView = undefined;
		},
		render : function() {
			currTextSearch.set('currentText','');
			if(this.supportPartialMatch){
				this.partialTextValueView = new PartialTextValueView();
				this.$el.append(this.partialTextValueView.render().el);
			}
			if(this.supportPartialMatch || this.supportSuggestion){
				this.$el.append(new TextEntryView().render().el);
			}
			if(this.supportSuggestion){
				this.textValueSuggestionView = new TextValueSuggestionView();
				this.$el.append(this.textValueSuggestionView.render().el);
			}
			if(this.supportSelectionList){
				this.selectionValueView = new SelectionValueView();
				this.$el.append(this.selectionValueView.render().el);
			}
			if(this.isBooleanFilter){
				this.booleanValueView = new BooleanValueView();
				this.$el.append(this.booleanValueView.render().el);
			}
			return this;
		},
		destroy : function() {
			if(this.textValueSuggestionView){
				this.textValueSuggestionView.destroy();
				this.textValueSuggestionView = undefined;
			}
			if(this.partialTextValueView){
				this.partialTextValueView.destroy();
				this.partialTextValueView = undefined;
			}
			if(this.selectionValueView){
				this.selectionValueView.destroy();
				this.selectionValueView = undefined;
			}
			if(this.booleanValueView){
				this.booleanValueView.destroy();
				this.booleanValueView = undefined;
			}
			this.remove();
		}
	});
	
	var DateIntervalValueView = Backbone.View.extend({
		dateIntervalTemplate : _.template($('#filter_template_date').html()),
		initialize : function() {
		},
		destroy : function(){
		},
		render : function() {
			this.setElement(this.dateIntervalTemplate());
			//by default, this is hidden
			$("#date_end",this.el).hide();
			return this;
		},
		events : {
			"click button" : "createNewFilter",
			"focus input[type=text]" : "onFocus",
			"blur input[type=text]" : "onBlur",
			"change input[type=checkbox]" : "onSearchIntervalChanged"
		},
		onFocus : function(evt){
		},
		onBlur : function(evt){
			var $el = $(evt.currentTarget);
			//accept empty field
			if($el.val() && !isValidDateElement($el)){
				$el.addClass('error');
			}
			else{
				$el.removeClass('error');
			}
		},
		onSearchIntervalChanged : function(evt){
			$("#date_end",this.$el).toggle();
			$(".label_single",this.$el).toggleClass("hidden");
			$(".label_range",this.$el).toggleClass("hidden");
		},
		createNewFilter : function(e) {
			var syear = $.trim($("#date_start_y",this.$el).val());
			var smonth = $.trim($("#date_start_m",this.$el).val());
			var sday = $.trim($("#date_start_d",this.$el).val());
			
			var isInterval = $("#interval",this.$el).is(':checked');
			var eyear = $.trim($("#date_end_y",this.$el).val());
			var emonth = $.trim($("#date_end_m",this.$el).val());
			var eday = $.trim($("#date_end_d",this.$el).val());
			
			if(!isValidPartialDate(syear,smonth,sday) || (isInterval && !isValidPartialDate(eyear,emonth,eday))){
				//TODO use language resources
				alert("This date is not valid");
				return;
			}
			
			if(isInterval && !isValidDateInterval(syear,smonth,sday,eyear,emonth,eday)){
				//TODO use language resources
				alert("This is not valid date interval");
				return;
			}
			
			var searchValue = [syear+'-'+canadensysUtils.dateElementZeroPad(smonth)+'-'+canadensysUtils.dateElementZeroPad(sday)];
			if(isInterval){
				searchValue.push(eyear+'-'+canadensysUtils.dateElementZeroPad(emonth)+'-'+canadensysUtils.dateElementZeroPad(eday));
			}
			var valueJSON = JSON.stringify(searchValue);
			
			//ignore duplicate filter
			if(filterList.where({searchableFieldId: currFilterKey.get('searchableFieldId'),valueJSON:valueJSON}).length !== 0){
				return;
			}
			
			var newFilter = new FilterItem({
				searchableFieldId : currFilterKey.get('searchableFieldId'),
				searchableFieldName : currFilterKey.get('searchableFieldName'),
				searchableFieldText : getAvailableFieldText(currFilterKey.get('searchableFieldName')),
				value : searchValue,
				valueJSON : valueJSON,
				valueText : safeGetValueText(searchValue)
			});
			
			if(isInterval){
				newFilter.set('op','BETWEEN');
				newFilter.set('opText',getOperatorText('BETWEEN'));
			}
			else{
				newFilter.set('op','EQ');
				newFilter.set('opText',getOperatorText('EQ'));
			}
			filterList.add(newFilter);
		}
	});
	
	var MinMaxValueView = Backbone.View.extend({
		minMaxValueTemplate : _.template($('#filter_template_minmax').html()),
		initialize : function() {
		},
		destroy : function(){
		},
		render : function() {
			this.setElement(this.minMaxValueTemplate());
			//by default, this is hidden
			$("#interval_max",this.el).hide();
			return this;
		},
		events : {
			"click button" : "createNewFilter",
			"focus input[type=text]" : "onFocus",
			"blur input[type=text]" : "onBlur",
			"change input[type=checkbox]" : "onSearchIntervalChanged"
		},
		onFocus : function(evt){
		},
		onBlur : function(evt){
			var $el = $(evt.currentTarget);
			var value = $.trim($el.val());
			//accept empty field
			if(!isValidNumber(value)){
				$el.addClass('error');
			}
			else{
				$el.removeClass('error');
			}
		},
		onSearchIntervalChanged : function(evt){
			$("#interval_max",this.$el).toggle();
			$(".label_single",this.$el).toggleClass("hidden");
			$(".label_range",this.$el).toggleClass("hidden");
		},
		createNewFilter : function(e) {
			var minValue = $.trim($("#value_min",this.$el).val());
			var isInterval = $("#interval",this.$el).is(':checked');
			var maxValue = $.trim($("#value_max",this.$el).val());

			if(!isValidNumber(minValue)){
				//TODO use language resources
				alert("This number is not valid");
				return;
			}
			
			if(isInterval && !isValidNumber(maxValue)){
				//TODO use language resources
				alert("This is not valid number interval");
				return;
			}
			
			var searchValue = [minValue];
			if(isInterval){
				searchValue.push(maxValue);
			}
			var valueJSON = JSON.stringify(searchValue);
			
			//ignore duplicate filter
			if(filterList.where({searchableFieldId: currFilterKey.get('searchableFieldId'),valueJSON:valueJSON}).length !== 0){
				return;
			}
			
			var newFilter = new FilterItem({
				searchableFieldId : currFilterKey.get('searchableFieldId'),
				searchableFieldName : currFilterKey.get('searchableFieldName'),
				searchableFieldText : getAvailableFieldText(currFilterKey.get('searchableFieldName')),
				value : searchValue,
				valueJSON : valueJSON,
				valueText : safeGetValueText(searchValue)
			});
			
			if(isInterval){
				newFilter.set('op','BETWEEN');
				newFilter.set('opText',getOperatorText('BETWEEN'));
			}
			else{
				newFilter.set('op','EQ');
				newFilter.set('opText',getOperatorText('EQ'));
			}
			filterList.add(newFilter);
		}
	});
	
	CurrentFiltersView = Backbone.View.extend({
		initialize : function() {
			filterList.bind('add', this.addFilter, this);
			filterList.bind('remove', this.removeFilter, this);
			this.emptyFilterHtml = $('#filter_empty');
			this.filterCounter = 0;
			this.nbOfFilter = 0;
			//keep track of the grouping component
			this.filterGroupView = {};
		},
		addFilter : function(filter) {		
			//remove empty filter element
			if(this.nbOfFilter === 0){
				$('#filter_current').find(':first-child').remove();
			}
			
			//group exists ?		
			if(!this.filterGroupView[filter.get('searchableFieldId')]) {
				var view = new FilterGroupView({
					model : filter
				});
				this.filterGroupView[filter.get('searchableFieldId')] = view.render().el;
				$("#filter_current").append(this.filterGroupView[filter.get('searchableFieldId')]);
			}
			this.filterCounter = this.filterCounter+1;
			this.nbOfFilter = this.nbOfFilter+1;
			//Set the groupId because the counter is here
			filter.set('groupId', this.filterCounter);
			var filterView = new FilterView({
				model : filter
			});
			$('ul', this.filterGroupView[filter.get('searchableFieldId')]).append(filterView.render().el);
			//Enable search button
			$('#filter_submit').removeAttr('disabled');
		},
		removeFilter : function(filter) {
			//check if we just removed the last element
			if(filterList.where({searchableFieldId: filter.get('searchableFieldId')}).length === 0){
				$(this.filterGroupView[filter.get('searchableFieldId')]).remove();
				delete this.filterGroupView[filter.get('searchableFieldId')];
			}
			this.nbOfFilter = this.nbOfFilter-1;
			if(this.nbOfFilter===0){
				$("#filter_current").append(this.emptyFilterHtml);
				$('#filter_submit').attr('disabled','disabled');
			}
		},
		render : function() {
			return this;
		}
	});
	
	//Allows to select the field like country, taxonRank
	FilterFieldSelectionView = Backbone.View.extend({
		$key_select : undefined,
		initialize : function() {
			//el could be set through the caller
			this.setElement(this.el);
			this.$key_select = $('#key_select',this.$el); //cache this component
			//this.$key_select.val('1');
			currFilterKey.bind('change:searchableFieldId', this.onFilterKeyChanged, this);
		},
		events : {
			"change #key_select" : "onFilterFieldChanged"
		},
		onFilterKeyChanged : function(model){
			//make sure this event is not from us (onFilterFieldChanged)
			//this should be called on pageload to set the current filter properly
			if(this.$key_select.val() !== model.get('searchableFieldId')){
				this.$key_select.val(model.get('searchableFieldId'));
			}
		},
		onFilterFieldChanged : function() {
			var searchableFieldId = this.$key_select.val();
			currFilterKey.set({searchableFieldId:searchableFieldId,
				searchableFieldName:availableSearchFields[searchableFieldId].searchableFieldName,
				searchableFieldText:getAvailableFieldText(availableSearchFields[searchableFieldId].searchableFieldName)});
		},
		render : function() {
			return this;
		}
	});
	
	//Allows to select the specific value based on the field (previously selected)
	FilterSelectionView = Backbone.View.extend({
		initialize : function() {
			this.setElement('#filter_content');
			currFilterKey.bind('change:searchableFieldId', this.onFilterKeyChanged, this);
			lastComponent = undefined;
		},
		events : {
		},
		onFilterKeyChanged : function(filterKey, fid) {
			var searchableFieldTypeEnum = availableSearchFields[filterKey.get('searchableFieldId')].searchableFieldTypeEnum;
			
			//This is not necessary but it makes it clear that we create new element each time
			if(lastComponent){
				lastComponent.destroy();
			}
			
			if(searchableFieldTypeEnum == 'SINGLE_VALUE'){
				lastComponent = new TextValueView();
			}
			else if(searchableFieldTypeEnum == 'START_END_DATE'){
				lastComponent = new DateIntervalValueView();	
			}
			else if(searchableFieldTypeEnum == 'MIN_MAX_NUMBER'){
				lastComponent = new MinMaxValueView();
			}
			this.$el.html(lastComponent.render().el);
		},
		render : function() {
			return this;
		}
	});
	
	FilterGroupView = Backbone.View.extend({
		tagName: 'li', // name of tag to be created
		className: 'filter round',
		initialize : function() {
		},
		render : function() {
			$(this.el).html(this.model.get('searchableFieldText') + '<ul></ul>');
			return this;
		}
	});
	
	FilterView = Backbone.View.extend({ 
		tagName: 'li', // name of tag to be created
	    events: { 
	      'click span.delete': 'remove'
	    },    
		initialize : function() {
			this.model.bind('remove', this.unrender, this);
		},
		render : function() {
			//compute from template
			$(this.el).html(filterViewTemplate(this.model.toJSON()));
			return this;
		},
		unrender: function(){
	      $(this.el).remove();
	    },
		remove: function(){
	      this.model.destroy();
	    }
	});
	
	DownloadView = Backbone.View.extend({    
		initialize : function() {
			currSearchResult.bind('change:numberOfRecord', this.onNumberOfRecordChanged, this);
		},
		render : function() {
			currDownloadView = new DownloadEmailView({ el: $("#download_content") });
			currDownloadView.render();
			return this;
		},
		onNumberOfRecordChanged : function() {
			this.render();
		}
	});
	
	DownloadEmailView = Backbone.View.extend({
		downloadEmailTemplate : _.template($('#download_template_email').html()),
	    events: { 
	    	'click button' : 'onAskForDownload'
	    },    
		initialize : function() {
			//el could be set through the caller
			this.setElement(this.el);
		},
		render : function() {
			this.$el.html(this.downloadEmailTemplate());
			this.$requestElement = $('#request',this.$el);
			this.$statusElement = $('#status',this.$el);
			return this;
		},
		onAskForDownload : function(){
			var email = $.trim($('#email',this.$el).val());
			if(!isValidEmail(email)){
				alert(languageResources.getLanguageResource('control.download.email.error'));
				return;
			}
			
			var that = this;
			this.paramMap = [];
			_.extend(this.paramMap,initialFilterParamMap);
			this.paramMap.push({name:'e',value:email});
			that.$requestElement.hide();
			var req = $.get('downloadresult',this.paramMap)
				.success(function(json){
					if(json['status'] !== 'deferred'){
						that.$statusElement.html(json['error']);
					}
					that.$statusElement.show();
				})
			   .error(function(jqXHR, textStatus, errorThrown){
				   that.showError();
				});
		}
	});
	
	DisplayView = Backbone.View.extend({    
		initialize : function() {
			this.render();
		},
		render : function() {
			var currDisplayView;
			var $form = $('form',$('#search'));
			var currView = $('input[name=view]',$form).val();
			if(currView === 'table'){
				currDisplayView = new DisplayTableView({ el: $("#display") });
			}
			else{
				currDisplayView = new DisplayMapView({ el: $("#display") });
			}
			currDisplayView.render();
			return this;
		}
	});
	
	DisplayTableView = Backbone.View.extend({
		displayTableTemplate : _.template($('#display_template_table').html()),
	    events: {
	    },    
		initialize : function() {
			//el could be set through the caller
			this.setElement(this.el);
		},
		render : function() {
			this.$el.html(this.displayTableTemplate());
		}
	});
	
	DisplayMapView = Backbone.View.extend({
		displayMapTemplate : _.template($('#display_template_map').html()),
	    events: {
	    },    
		initialize : function() {
			//el could be set through the caller
			this.setElement(this.el);
		},
		render : function() {
			this.$el.html(this.displayMapTemplate());
		}
	});
	
	//TODO init method
	new CurrentFiltersView;
	new FilterSelectionView;
	new FilterFieldSelectionView({ el: $('#filter_select') });
	new DownloadView();
	new DisplayView();
	
	//Public methods
    return {
    	setNumberOfResult : setNumberOfResult,
    	setAvailableSearchFields : setAvailableSearchFields,
    	loadFilter : loadFilter,
    	getInitialFilterParamMap : getInitialFilterParamMap
	};
}(jQuery));
	
