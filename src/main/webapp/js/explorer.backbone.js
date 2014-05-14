/****************************
Copyright (c) 2014 Canadensys
Explorer backbone
****************************/
/*global EXPLORER, $, window, _, Backbone, google, alert*/

EXPLORER.backbone = (function(){

  'use strict';

  var filterViewTemplate = _.template($('#filter_template_current').html()),

  FilterKey = Backbone.Model.extend({
    defaults: {
      searchableFieldId: -1,
      searchableFieldName : undefined,
      searchableFieldText : undefined
    }
  }),

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
  }),

  SearchResult = Backbone.Model.extend({
    defaults: {
      numberOfRecord: 0
    }
  }),

  TextSearch = Backbone.Model.extend({
    defaults: {
      currentText: undefined,
      validate:false
    }
  }),

  FilterList = Backbone.Collection.extend({
    model: FilterItem
  }),

  filterList = new FilterList(),
  currFilterKey = new FilterKey(),
  currSearchResult = new SearchResult(),
  currTextSearch = new TextSearch(),

  availableSearchFields,
  initialFilterParamMap,
  DEFAULT_SEARCHABLE_FIELD_ID = 16; //scientificName

  function setNumberOfResult(numberOfRecord){
    currSearchResult.set("numberOfRecord",numberOfRecord);
  }

  function setAvailableSearchFields(_availableSearchFields){
    availableSearchFields = _availableSearchFields;
  }

  //Get the searchableFieldTypeEnum(e.g. SINGLE_VALUE) of a searchableFieldId
  function getSearchableFieldTypeEnum(searchableFieldId) {
    return availableSearchFields[searchableFieldId].searchableFieldTypeEnum;
  }

  function getInitialFilterParamMap(){
    return initialFilterParamMap;
  }

  function getOperatorText(operator){
    var varName = 'operator.'+operator.toLowerCase();
    return EXPLORER.i18n.getLanguageResource(varName);
  }

  function getAvailableFieldText(fieldName){
    var varName = 'filter.'+fieldName.toLowerCase();
    return EXPLORER.i18n.getLanguageResource(varName);
  }

  function safeGetValueText(valueList){
    var parsedValueText = [], varName;
    $.each(valueList, function() {
      varName = 'filter.value.'+this.toString().toLowerCase();
      if(EXPLORER.i18n.getLanguageResource(varName)){
        parsedValueText.push(EXPLORER.i18n.getLanguageResource(varName));
      } else {
        parsedValueText.push(this);
      }
    });
    return parsedValueText.join();
  }

  //Bind an action on the current filters list
  function bindToFilterList(action,callback,context){
    filterList.bind(action, callback, context);
  }
  
  //Add a new active filter based on provided filter properties.
  //Mandatory properties are: (searchableFieldId or searchableFieldName) and valueList
  //A copy of the newly created FilterItem will be returned.
  function addActiveFilter(filterProps){
    filterProps = filterProps || {};
    var searchableFieldId = filterProps.searchableFieldId,
    searchableFieldName = filterProps.searchableFieldName,
    valueList = filterProps.valueList,
    searchableField, operator, valueJSON, newFilter;

    //find the searchableField by id or name
    if(searchableFieldId){
      searchableField = availableSearchFields[searchableFieldId.toString()];
    }
    else if(searchableFieldName){
      searchableField = _.find(availableSearchFields,
          function(sf){ return sf.searchableFieldName === searchableFieldName; });
    }

    if(!searchableField){
      //log to console for debug?
      return;
    }
    //if no operator is specified, we can accomodate that if the searchableField contains only 1 operator
    if(!filterProps.op && searchableField.supportedOperator.length !== 1){
      //log to console for debug?
      return;
    }

    operator = filterProps.op || searchableField.supportedOperator[0];
    valueJSON = JSON.stringify(valueList);
    newFilter = new FilterItem({
      searchableFieldId : searchableField.searchableFieldId,
      searchableFieldName : searchableField.searchableFieldName,
      searchableFieldText : getAvailableFieldText(searchableField.searchableFieldName),
      op : operator,
      opText : getOperatorText(operator),
      value : valueList,
      valueJSON : valueJSON,
      valueText : safeGetValueText(valueList)
    });
    filterList.add(newFilter);
    //return a copy, maybe we should only do it if the caller asked for it?
    return _.extend({},newFilter);
  }
  
  //Update a FilterItem that is currently in the list
  //Only the 'valueList' array field can be updated for now
  function updateActiveFilter(filterItem,props){
    var _filterItem = filterList.get(filterItem.cid);
    if(_filterItem){
      var _valueList = valueList.value || [],
      newValues = {
        value : _valueList,
        valueJSON : JSON.stringify(_valueList),
        valueText : safeGetValueText(_valueList)};
      _filterItem.set(newValues);
    }
  }

  //initialize active filters from a list of json properties
  function initActiveFilters(json){
    var filterItem, lastSearchableFieldId, key, searchableFieldTypeEnum;

    for (key in json) {
      if (json.hasOwnProperty(key)) {
        addActiveFilter(json[key]);
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

  function removeFilter(json) {
    var filter = filterList.where(json);
    if(filter.length !== 0) {
      filterList.remove(filter);
    }
  }

  function getFilter(json) {
    return filterList.where(json);
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
    onTextTyped : function(e) {
      var value;

      if(e){
        value = $(e.currentTarget).val();
        currTextSearch.set({currentText:value,validate:(e.keyCode === 13)});
      }
    }
  });

  //View that supports text suggestion
  var TextValueSuggestionView = Backbone.View.extend({
    suggestionValueTemplate : _.template($('#filter_template_suggestions').html()),
    cacheKeys : [],
    cacheMap : {},
    initialize : function() {
      currTextSearch.bind('change:currentText', this.onTextChanged, this);
    },
    destroy : function(){
      currTextSearch.unbind('change:currentText',this.onTextChanged);
      this.remove();
    },
    render : function() {
      this.setElement(this.suggestionValueTemplate());
      this.onTextChanged(currTextSearch);
      return this;
    },
    events : {
      "click #value_suggestions tr" : "createNewSuggestionFilter"
    },
    onTextChanged : function(textSearchModel) {
      var mapParam = {},
          cacheKey = currFilterKey.get('searchableFieldId'),
          value = textSearchModel.get('currentText'),
          self = this;

      mapParam.fieldId = currFilterKey.get('searchableFieldId');

      if(value){
        mapParam.curr = value;
        cacheKey += value;
      }

      if(this.cacheMap[cacheKey]) {
        this.replaceTableCellContent(this.cacheMap[cacheKey]);
      }
      else{
        $.get('livesearch',mapParam)
          .success(function(json){
            self.replaceTableCellContent(json);
            
            if(self.cacheKeys.length === 10){
              delete self.cacheMap[self.cacheKeys.shift()];
            }
            self.cacheKeys.push(cacheKey);
            self.cacheMap[cacheKey] = json;
          })
           .error(function(jqXHR, textStatus, errorThrown){
             alert(textStatus);
          });
      }
    },
    replaceTableCellContent : function(json){
      var rows = json.rows,
          lastRow=0,
          row,
          self = this;

      $.each(rows, function(key, val) {
        row = $('#value_suggestions tr:nth-child('+(key+1)+')',self.$el);
        row.find('td:nth-child(1)').html(val.value);
        row.find('td:nth-child(2)').html(val.occurrence_count);
        row.removeClass('hidden');
        row.attr('id',val.id);
        lastRow = key+1; //to match tr index (who starts at 1)
      });

      //move to next row
      lastRow += 1;
      //clear remaining rows
      while(lastRow<=10){
        row = $('#value_suggestions tr:nth-child('+lastRow+')',self.$el);
        row.find('td:nth-child(1)').html(' ');
        row.find('td:nth-child(2)').html(' ');
        row.addClass('hidden');
        row.removeAttr('id');
        lastRow += 1;
      }
    },
    createNewSuggestionFilter : function(e) {
      var value = [$(e.currentTarget).find('td:nth-child(1)').text()],
          valueJSON = JSON.stringify(value);

      //ignore duplicate filter (ignore case)
      if(filterList.find(function(currFilter){ 
        return (currFilter.get('searchableFieldId') === currFilterKey.get('searchableFieldId') &&
            currFilter.get('valueJSON').toLowerCase() === valueJSON.toLowerCase());
        })){
        return;
      }
      //TODO use addActiveFilter
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
          function(str){ return str.toLowerCase().search("^[s|e|c]like$") !== -1; });
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
    createNewLikeFilter : function() {
      var value = [currTextSearch.get('currentText')],
          valueJSON = JSON.stringify(value);

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
      //TODO use addActiveFilter
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
    initialize: function() {
      return;
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
      var $select, options = "";
      //could also be loaded with a model fetch
      $.get('getpossiblevalues',{fieldId:fieldId})
        .success(function(json){
          $select = $("#value_select",this.$el);
          $.each(json, function() {
            options += '<option value="'+this.value+'">'+this.value+'</option>';
          });
          $select.append(options);
        })
         .error(function(jqXHR, textStatus, errorThrown){
           alert(textStatus);
        });
    },
    createNewFilter : function() {
      var value = [$("#value_select",this.$el).val()],
          valueJSON = JSON.stringify(value);

      //skip empty filter
      if(!value || value.length === 0){
        return;
      }
      //ignore if a current filter exists
      if(filterList.where({searchableFieldId: currFilterKey.get('searchableFieldId'),valueJSON:valueJSON}).length !== 0){
        return;
      }
      
      addActiveFilter({
        searchableFieldId:currFilterKey.get('searchableFieldId'),
        valueList : value
      });
    }
  });

  //View that supports creation of a filter with the LIKE operator
  var BooleanValueView = Backbone.View.extend({
    booleanValueTemplate : _.template($('#filter_template_boolean_value').html()),
    initialize: function() {
      return;
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
    createNewLikeFilter : function() {
      var value =  [($('input[name=boolGroup]:checked',this.$el).val())],
          valueJSON = JSON.stringify(value),
          newFilter;

      //skip empty filter
      if(value.length === 0){
        return;
      }

      //ignore duplicate filter, boolean filter must not be already included
      if(filterList.where({searchableFieldId: currFilterKey.get('searchableFieldId')}).length !== 0){
        return;
      }
      //TODO use addActiveFilter
      newFilter = new FilterItem({
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
      var currFilter = availableSearchFields[currFilterKey.get('searchableFieldId')];
      this.supportSuggestion = currFilter.supportSuggestion;
      this.supportPartialMatch = currFilter.supportPartialMatch;
      this.supportSelectionList = currFilter.supportSelectionList;
      this.isBooleanFilter = (!_.isUndefined(currFilter.type) && currFilter.type.indexOf("Boolean") !== -1);
        
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
    initialize: function() {
      return;
    },
    destroy: function() {
      return;
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
    onBlur : function(e){
      var $el = $(e.currentTarget);
      //accept empty field
      if($el.val() && !EXPLORER.utils.isValidDateElement($el)){
        $el.addClass('error');
      }
      else{
        $el.removeClass('error');
      }
    },
    onSearchIntervalChanged : function(){
      $("#date_end",this.$el).toggle();
      $(".label_single",this.$el).toggleClass("hidden");
      $(".label_range",this.$el).toggleClass("hidden");
    },
    createNewFilter : function() {
      var syear = $.trim($("#date_start_y",this.$el).val()),
          smonth = $.trim($("#date_start_m",this.$el).val()),
          sday = $.trim($("#date_start_d",this.$el).val()),
          isInterval = $("#interval",this.$el).is(':checked'),
          eyear = $.trim($("#date_end_y",this.$el).val()),
          emonth = $.trim($("#date_end_m",this.$el).val()),
          eday = $.trim($("#date_end_d",this.$el).val()),
          valueJSON, searchValue, newFilter;

      if(!EXPLORER.utils.isValidPartialDate(syear,smonth,sday) || (isInterval && !EXPLORER.utils.isValidPartialDate(eyear,emonth,eday))){
        alert(EXPLORER.i18n.getLanguageResource('control.invalid.date'));
        return;
      }

      if(isInterval && !EXPLORER.utils.isValidDateInterval(syear,smonth,sday,eyear,emonth,eday)){
        alert(EXPLORER.i18n.getLanguageResource('control.invalid.dateinterval'));
        return;
      }

      searchValue = [syear+'-'+EXPLORER.utils.dateElementZeroPad(smonth)+'-'+EXPLORER.utils.dateElementZeroPad(sday)];
      if(isInterval){
        searchValue.push(eyear+'-'+EXPLORER.utils.dateElementZeroPad(emonth)+'-'+EXPLORER.utils.dateElementZeroPad(eday));
      }
      valueJSON = JSON.stringify(searchValue);

      //ignore duplicate filter
      if(filterList.where({searchableFieldId: currFilterKey.get('searchableFieldId'),valueJSON:valueJSON}).length !== 0){
        return;
      }
      //TODO use addActiveFilter
      newFilter = new FilterItem({
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
    initialize: function() {
      return;
    },
    destroy: function() {
      return;
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
    onFocus: function() {
      return;
    },
    onBlur : function(e){
      var $el = $(e.currentTarget),
          value = $.trim($el.val());

      //accept empty field
      if(!EXPLORER.utils.isValidNumber(value)){
        $el.addClass('error');
      }
      else{
        $el.removeClass('error');
      }
    },
    onSearchIntervalChanged : function(){
      $("#interval_max",this.$el).toggle();
      $(".label_single",this.$el).toggleClass("hidden");
      $(".label_range",this.$el).toggleClass("hidden");
    },
    createNewFilter : function() {
      var minValue = $.trim($("#value_min",this.$el).val()),
          isInterval = $("#interval",this.$el).is(':checked'),
          maxValue = $.trim($("#value_max",this.$el).val()),
          searchValue, valueJSON, newFilter;

      if(!EXPLORER.utils.isValidNumber(minValue)){
        alert(EXPLORER.i18n.getLanguageResource('control.invalid.number'));
        return;
      }

      if(isInterval && !EXPLORER.utils.isValidNumber(maxValue)){
        alert(EXPLORER.i18n.getLanguageResource('control.invalid.numberinterval'));
        return;
      }

      searchValue = [minValue];
      if(isInterval){
        searchValue.push(maxValue);
      }
      valueJSON = JSON.stringify(searchValue);

      //ignore duplicate filter
      if(filterList.where({searchableFieldId: currFilterKey.get('searchableFieldId'),valueJSON:valueJSON}).length !== 0){
        return;
      }
      //TODO use addActiveFilter
      newFilter = new FilterItem({
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

  //View of a group of active filters of the same type.
  var FilterGroupView = Backbone.View.extend({
    tagName: 'li', // name of tag to be created
    className: 'filter round',
    initialize: function() {
      return;
    },
    render : function() {
      $(this.el).html(this.model.get('searchableFieldText') + '<ul></ul>');
      return this;
    }
  });

  //View of a single active filter
  var FilterView = Backbone.View.extend({
    tagName: 'li', // name of tag to be created
    events: {
      'click span.delete': 'remove'
    },
    initialize : function() {
      this.model.bind('remove', this.unrender, this);
      this.model.bind('change', this.render, this);
    },
    render : function() {
      //compute from template
      $(this.el).html(filterViewTemplate(this.model.toJSON()));
      return this;
    },
    unrender: function(){
      $(this.el).remove();
      EXPLORER.EventBus.trigger("filterRemove", this);
    },
    remove: function(){
      //destoy will also remove events bound to the model
      this.model.destroy();
    }
  });

  var CurrentFiltersView = Backbone.View.extend({
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
  var FilterFieldSelectionView = Backbone.View.extend({
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
  var FilterSelectionView = Backbone.View.extend({
    lastComponent: undefined,
    initialize : function() {
      this.setElement('#filter_content');
      currFilterKey.bind('change:searchableFieldId', this.onFilterKeyChanged, this);
    },
    events : {
    },
    onFilterKeyChanged : function(filterKey) {
      var searchableFieldTypeEnum = availableSearchFields[filterKey.get('searchableFieldId')].searchableFieldTypeEnum;

      //This is not necessary but it makes it clear that we create new element each time
      if(this.lastComponent){
        this.lastComponent.destroy();
      }

      if(searchableFieldTypeEnum === 'SINGLE_VALUE'){
        this.lastComponent = new TextValueView();
      }
      else if(searchableFieldTypeEnum === 'START_END_DATE'){
        this.lastComponent = new DateIntervalValueView();
      }
      else if(searchableFieldTypeEnum === 'MIN_MAX_NUMBER'){
        this.lastComponent = new MinMaxValueView();
      }
      else{
        this.lastComponent = new TextValueView();
      }
      this.$el.html(this.lastComponent.render().el);
    },
    render : function() {
      return this;
    }
  });

  var DownloadEmailView = Backbone.View.extend({
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
      var self = this, email = $.trim($('#email',this.$el).val());

      if(!EXPLORER.utils.isValidEmail(email)){
        alert(EXPLORER.i18n.getLanguageResource('control.download.email.error'));
        return;
      }

      this.paramMap = [];
      _.extend(this.paramMap,initialFilterParamMap);
      this.paramMap.push({name:'e',value:email});
      this.$requestElement.hide();
      $.get('downloadresult',this.paramMap)
        .success(function(json){
          if(json.status !== 'deferred'){
            self.$statusElement.html(json.error);
          }
          self.$statusElement.show();
        })
         .error(function(jqXHR, textStatus, errorThrown){
           self.showError();
        });
    }
  });

  var DownloadView = Backbone.View.extend({
    initialize : function() {
      currSearchResult.bind('change:numberOfRecord', this.onNumberOfRecordChanged, this);
    },
    render : function() {
      var currDownloadView = new DownloadEmailView({ el: $("#download_content") });
      currDownloadView.render();
      return this;
    },
    onNumberOfRecordChanged : function() {
      this.render();
    }
  });

  var DisplayMapView = Backbone.View.extend({
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

  var DisplayTableView = Backbone.View.extend({
    displayTableTemplate : _.template($('#display_template_table').html()),
    initialize : function() {
      //el could be set through the caller
      this.setElement(this.el);
    },
    render : function() {
      this.$el.html(this.displayTableTemplate());
    }
  });

  var DisplayView = Backbone.View.extend({
    initialize : function() {
      this.render();
    },
    render : function() {
      var currDisplayView,
          $form = $('form',$('#search')),
          currView = $('input[name=view]',$form).val();

      currDisplayView = (currView === 'table') ? new DisplayTableView({ el: $("#display") }) : new DisplayMapView({ el: $("#display") });
      currDisplayView.render();
      return this;
    }
  });

  function init() {
    new CurrentFiltersView();
    new FilterSelectionView();
    new FilterFieldSelectionView({ el: $('#filter_select') });
    new DownloadView();
    new DisplayView();
  }

  //Public methods
  return {
    init: init,
    setNumberOfResult : setNumberOfResult,
    setAvailableSearchFields : setAvailableSearchFields,
    getFilter : getFilter,
    initActiveFilters : initActiveFilters,
    addActiveFilter : addActiveFilter,
    updateActiveFilter : updateActiveFilter,
    bindToFilterList : bindToFilterList,
    removeFilter : removeFilter,
    getInitialFilterParamMap : getInitialFilterParamMap
  };

}());
