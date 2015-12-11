<!-- JavaScript extend settings -->
<#macro controlJavaScriptSettings>
	$.extend(EXPLORER.settings, { "baseUrl" : "${page.contextURL}", "wsPath" : "/ws/", "locale" : "${rc.getLocale().getLanguage()}" });
</#macro>

<!-- JavaScript init call related to controls -->
<#macro controlJavaScriptInit>
	EXPLORER.i18n.setLanguageResources(${page.languageResources});
	EXPLORER.backbone.setAvailableSearchFields(${page.availableFiltersMap});
	EXPLORER.backbone.initActiveFilters(${page.searchCriteria}, _.has(EXPLORER, 'map') ? EXPLORER.map.setBounds : {}, this);
	EXPLORER.backbone.setNumberOfResult(${page.occurrenceCount?c});
</#macro>

<div id="control">
	<div class="nav_container">
		<ul id="control_buttons" class="buttons">
			<li><a href="#search" class="selected">${rc.getMessage("control.search.header.button")}</a></li>
			<li><a href="#download">${rc.getMessage("control.download.header.button")}</a></li>
			<li><a href="#display">${rc.getMessage("control.display.header.button")}</a></li>
		</ul>
	</div>
	<div id="control_wrapper">
		<div id="occ_preview"><#-- Will be injected --></div>
		<div id="control_bar" class="round clear_fix">
			<!-- Search -->
			<div id="search" class="clear_fix">
				<h3>${rc.getMessage("control.search.create.title")}</h3>
				<div id="filter_select">
					<select id="key_select">
						<optgroup label="${rc.getMessage("filter.group.classification")}">
							<option value="${page.availableFilters.scientificname}">${rc.getMessage("filter.scientificname")}</option>
							<option value="${page.availableFilters.kingdom}">${rc.getMessage("filter.kingdom")}</option>
							<option value="${page.availableFilters.phylum}">${rc.getMessage("filter.phylum")}</option>
							<option value="${page.availableFilters._class}">${rc.getMessage("filter._class")}</option>
							<option value="${page.availableFilters._order}">${rc.getMessage("filter._order")}</option>
							<option value="${page.availableFilters.family}">${rc.getMessage("filter.family")}</option>
							<option value="${page.availableFilters.genus}">${rc.getMessage("filter.genus")}</option>
							<option value="${page.availableFilters.taxonrank}">${rc.getMessage("filter.taxonrank")}</option>
						</optgroup>
						<optgroup label="${rc.getMessage("filter.group.dataset")}">
							<option value="${page.availableFilters.resourcename}">${rc.getMessage("filter.resourcename")}</option>
							<option value="${page.availableFilters.publishername}">${rc.getMessage("filter.publishername")}</option>
							<option value="${page.availableFilters.institutioncode}">${rc.getMessage("filter.institutioncode")}</option>
							<option value="${page.availableFilters.datasetname}">${rc.getMessage("filter.datasetname")}</option>
							<option value="${page.availableFilters.sourcefileid}">${rc.getMessage("filter.sourcefileid")}</option>
						</optgroup>
						<optgroup label="${rc.getMessage("filter.group.specimen")}">
							<option value="${page.availableFilters.collectioncode}">${rc.getMessage("filter.collectioncode")}</option>
							<option value="${page.availableFilters.catalognumber}">${rc.getMessage("filter.catalognumber")}</option>
							<option value="${page.availableFilters.recordedby}">${rc.getMessage("filter.recordedby")}</option>
							<option value="${page.availableFilters.recordnumber}">${rc.getMessage("filter.recordnumber")}</option>
						</optgroup>
						<optgroup label="${rc.getMessage("filter.group.date")}">
							<option value="${page.availableFilters.daterange}">${rc.getMessage("filter.daterange")}</option>
						</optgroup>
						<optgroup label="${rc.getMessage("filter.group.location")}">
							<option value="${page.availableFilters.continent}">${rc.getMessage("filter.continent")}</option>
							<option value="${page.availableFilters.country}">${rc.getMessage("filter.country")}</option>
							<option value="${page.availableFilters.stateprovince}">${rc.getMessage("filter.stateprovince")}</option>
							<option value="${page.availableFilters.county}">${rc.getMessage("filter.county")}</option>
							<option value="${page.availableFilters.municipality}">${rc.getMessage("filter.municipality")}</option>
							<option value="${page.availableFilters.locality}">${rc.getMessage("filter.locality")}</option>
							<option value="${page.availableFilters.altituderange}">${rc.getMessage("filter.altituderange")}</option>
						</optgroup>
						<optgroup label="${rc.getMessage("filter.group.extra")}">
							<option value="${page.availableFilters.hascoordinates}">${rc.getMessage("filter.hascoordinates")}</option>
							<option value="${page.availableFilters.hasmedia}">${rc.getMessage("filter.hasmedia")}</option>
							<#if page.availableFilters.hasassociatedsequences?has_content>
								<option value="${page.availableFilters.hasassociatedsequences}">${rc.getMessage("filter.hasassociatedsequences")}</option>
							</#if>	
							<option value="${page.availableFilters.hastypestatus}">${rc.getMessage("filter.hastypestatus")}</option>
						</optgroup>
					</select>
				</div>
				<div id="filter_content" class="clear_fix">
				<#-- Content injected with javascript -->
				</div>
				
				<h3>${rc.getMessage("control.search.current.title")}</h3>
				<form method="get" action="<@i18nResource resourceName="search"/>">
					<input type="hidden" name="view" value="${page.currentView}">
					<ul id="filter_current" class="custom_list">
						<li id="filter_empty">${rc.getMessage("control.search.current.empty")}</li>
					</ul>
					<p><input id="filter_submit" type="submit" class="search_button" value="${rc.getMessage("control.search.button.search")}" /></p>
				</form>
			</div>
			
			<!-- Download -->
			<div id="download" class="clear_fix hidden">
				<h3>${rc.getMessage("control.download.create.title")}</h3>
				<div id="download_content" class="clear_fix">
				<#-- Content injected with javascript -->
				</div>
			</div>
			
			<!-- Display -->
			<div id="display" class="clear_fix hidden">
				<#-- Content injected with javascript -->
			</div>
		</div>
	</div>
</div>

<!-- Single value template -->
<script type="text/template" id="filter_template_single">
	<div id="filter_text"></div>
</script>

<!-- Partial match -->
<script type="text/template" id="filter_template_partial_match">
<p id="partial_match" class="clear_fix">
<button type="button">${rc.getMessage("control.search.button.add")}</button> ${rc.getMessage("control.search.partial.operatorprefix")} <%= opText %>: <span id="partial_match_value"></span>
</p>
</script>

<!-- Text input -->
<script type="text/template" id="filter_template_text_input">
<p><input id="value_search" type="text"/></p>
</script>

<!-- Select box -->
<script type="text/template" id="filter_template_select">
<div>
	<p><select id="value_select"></select></p>
	<p class="clear_fix"><button type="button">${rc.getMessage("control.search.button.add")}</button></p>
</div>
</script>

<!-- Suggestions -->
<script type="text/template" id="filter_template_suggestions">
<div id="filter_suggestions" class="round">
	<table id="value_suggestions">
	<tbody>
	<#list 1..10 as i>
	 	<tr class="hidden" tabindex="-1">
			<td></td>
			<td class="right"></td>
		</tr>
	</#list>
	</tbody> 
	</table>
</div>
</script>
	
<!-- Boolean input -->
<script type="text/template" id="filter_template_boolean_value">
<div id="filter_boolean" class="clear_fix">
  <fieldset>
    <legend><%= fieldText %>:</legend>
    <input type="radio" id="filter_template_boolean_true" name="boolGroup" value="true" checked />
    <label for="filter_template_boolean_true">${rc.getMessage("filter.value.true")}</label>
    <input type="radio" id="filter_template_boolean_false" name="boolGroup" value="false" />
    <label for="filter_template_boolean_false">${rc.getMessage("filter.value.false")}</label>
  </fieldset>
  <button type="button">${rc.getMessage("control.search.button.add")}</button>
</div>
</script>

<!-- Date template -->
<script type="text/template" id="filter_template_date">
<div id="filter_date">
	<p class="clear_fix">
		<span id="date_start">
		<label for="date_start_y" class="label_single">${rc.getMessage("control.search.date.singledate")}</label>
		<label for="date_start_y" class="label_range hidden">${rc.getMessage("control.search.date.startdate")}</label>
			<input id="date_start_y" class="validationYear" type="text" maxlength="4" placeholder="yyyy"/>
			<input id="date_start_m" class="validationMonth" type="text" maxlength="2" placeholder="mm"/>
			<input id="date_start_d" class="validationDay" type="text" maxlength="2" placeholder="dd"/> 
		</span>
		<span id="date_end"><label for="date_end_y">${rc.getMessage("control.search.date.enddate")}</label>
			<input id="date_end_y" class="validationYear" type="text" maxlength="4" placeholder="yyyy"/>
			<input id="date_end_m" class="validationMonth" type="text" maxlength="2" placeholder="mm"/>
			<input id="date_end_d" class="validationDay" type="text" maxlength="2" placeholder="dd"/>
		</span>
	</p>
	<p class="clear_fix"><button type="button">${rc.getMessage("control.search.button.add")}</button>
	<input id="interval" type="checkbox" value="interval"/><label for="interval">${rc.getMessage("control.search.date.range")}</label></p>
	<div class="filter_info round">
	<table>
		<tr><td>${rc.getMessage("control.search.date.example1.title")}</td><td>${rc.getMessage("control.search.date.example1")}</td></tr>
		<tr><td>${rc.getMessage("control.search.date.example2.title")}</td><td>${rc.getMessage("control.search.date.example2")}</td></tr>
		<tr><td>${rc.getMessage("control.search.date.example3.title")}</td><td>${rc.getMessage("control.search.date.example3")}</td></tr>
		<tr><td>${rc.getMessage("control.search.date.example4.title")}</td><td>${rc.getMessage("control.search.date.example4")}</td></tr>
		<tr><td>${rc.getMessage("control.search.date.example5.title")}</td><td>${rc.getMessage("control.search.date.example5")}</td></tr>
	</table>
	</div>
</div>
</script>

<!-- Minmax template -->
<script type="text/template" id="filter_template_minmax">
<div id="filter_minmax">
	<p class="clear_fix">
		<span id="interval_min">
		<label for="value_min" class="label_single">${rc.getMessage("control.search.minmax.single")}</label>
		<label for="value_min" class="label_range hidden">${rc.getMessage("control.search.minmax.min")}</label>
		<input id="value_min" class="validationNumber" type="text"/></span>
		<span id="interval_max">
		  <label for="value_max">${rc.getMessage("control.search.minmax.max")}</label>
		  <input id="value_max" class="validationNumber" type="text"/>
		</span>
	</p>
	<p class="clear_fix"><button type="button">${rc.getMessage("control.search.button.add")}</button>
	<input id="interval" type="checkbox" value="interval" /><label for="interval">${rc.getMessage("control.search.minmax.range")}</label></p>
</div>
</script>

<!-- Current filters template -->
<script type="text/template" id="filter_template_current">
	<%- valueText %>
	<span>(<%= opText %>)</span>
	<span class="delete">${rc.getMessage("control.search.button.delete")}</span>
	<input type="hidden" name="<%= groupId %>_f" value="<%= searchableFieldId %>"/>
	<input type="hidden" name="<%= groupId %>_o" value="<%= op %>"/>
	<% _.each(valueList, function(value,key) { %> <input type="hidden" name="<%= groupId %>_v_<%= key+1 %>" value="<%= value %>"/> <% }); %>
</script>

<!-- Download templates -->
<script type="text/template" id="download_template_email">
	<div id="request">
		<p>${rc.getMessage("control.download.request.details1")} <strong>${page.occurrenceCount}</strong> ${rc.getMessage("control.download.request.details2")}</p>
		<p>${rc.getMessage("control.download.request.details3")}</p>
		<p>${rc.getMessage("control.download.request.details4")}</p>
		<p><label for="email">${rc.getMessage("control.download.email.label")}</label><input id="email" type="text"/></p>
		<p class="clear_fix"><button type="button" onClick="_gaq.push(['_trackEvent', 'Archive', 'Download', '${page.occurrenceCount}']);">${rc.getMessage("control.download.button.send")}</button></p>
	</div>
	<div id="status" class="hidden">
		<p>${rc.getMessage("control.download.status.details1")}</p>
		<p>${rc.getMessage("control.download.status.details2")}</p>
	</div>
</script>

<!-- Display templates -->
<script type="text/template" id="display_template_table">
	<h3>${rc.getMessage("control.display.table.title")}</h3>
	<p>${rc.getMessage("control.display.table.details1")}</p>
	<ul id="display_columns" class="custom_list">
	<#-- List items are added by rwd-table.js. Items with class="persist" are not available to deselect. -->
	</ul>
</script>

<script type="text/template" id="display_template_map">
	<p>${rc.getMessage("control.display.map.details1")}</p>
</script>
