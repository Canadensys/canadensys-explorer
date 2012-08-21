<div id="control">
	<div class="nav_container">
		<ul id="control_buttons" class="buttons">
			<li><a href="#search" class="selected">${ltext("control.search.header.button")}</a></li>
			<li><a href="#download">${ltext("control.download.header.button")}</a></li>
			<li><a href="#display">${ltext("control.display.header.button")}</a></li>
		</ul>
	</div>
	<div id="control_wrapper">
		<div id="occ_preview"><#include "../occurrence-preview.ftl"></div>
		<div id="control_bar" class="round clear_fix">
			<!-- Search -->
			<div id="search" class="clear_fix">
				<h3>${ltext("control.search.create.title")}</h3>
				<div id="filter_select">
					<select id="key_select">
						<optgroup label="${ltext("filter.group.classification")}">
							<option value="${root.availableFilters.scientificname}">${ltext("filter.scientificname")}</option>
							<option value="${root.availableFilters.kingdom}">${ltext("filter.kingdom")}</option>
							<option value="${root.availableFilters.phylum}">${ltext("filter.phylum")}</option>
							<option value="${root.availableFilters._class}">${ltext("filter._class")}</option>
							<option value="${root.availableFilters._order}">${ltext("filter._order")}</option>
							<option value="${root.availableFilters.family}">${ltext("filter.family")}</option>
							<option value="${root.availableFilters.taxonrank}">${ltext("filter.taxonrank")}</option>
						</optgroup>
						<optgroup label="${ltext("filter.group.dataset")}">
							<option value="${root.availableFilters.institutioncode}">${ltext("filter.institutioncode")}</option>
							<option value="${root.availableFilters.datasetname}">${ltext("filter.datasetname")}</option>
						</optgroup>
						<optgroup label="${ltext("filter.group.specimen")}">
							<option value="${root.availableFilters.collectioncode}">${ltext("filter.collectioncode")}</option>
							<option value="${root.availableFilters.catalognumber}">${ltext("filter.catalognumber")}</option>
							<option value="${root.availableFilters.recordedby}">${ltext("filter.recordedby")}</option>
							<option value="${root.availableFilters.recordnumber}">${ltext("filter.recordnumber")}</option>
						</optgroup>
						<optgroup label="${ltext("filter.group.date")}">
							<option value="${root.availableFilters.daterange}">${ltext("filter.daterange")}</option>
						</optgroup>
						<optgroup label="${ltext("filter.group.location")}">
							<option value="${root.availableFilters.continent}">${ltext("filter.continent")}</option>
							<option value="${root.availableFilters.country}">${ltext("filter.country")}</option>
							<option value="${root.availableFilters.stateprovince}">${ltext("filter.stateprovince")}</option>
							<option value="${root.availableFilters.county}">${ltext("filter.county")}</option>
							<option value="${root.availableFilters.municipality}">${ltext("filter.municipality")}</option>
							<option value="${root.availableFilters.locality}">${ltext("filter.locality")}</option>
							<option value="${root.availableFilters.altituderange}">${ltext("filter.altituderange")}</option>
						</optgroup>
						<optgroup label="${ltext("filter.group.extra")}">
							<option value="${root.availableFilters.hascoordinates}">${ltext("filter.hascoordinates")}</option>
							<option value="${root.availableFilters.hasmedia}">${ltext("filter.hasmedia")}</option>
						</optgroup>
					</select>
				</div>
				<div id="filter_content" class="clear_fix">
				<#-- Content injected with javascript -->
				</div>
				
				<h3>${ltext("control.search.current.title")}</h3>
				<form method="get" action="search">
					<input type="hidden" name="view" value="${root.currentView}">
					<ul id="filter_current" class="custom_list">
						<li id="filter_empty">${ltext("control.search.current.empty")}</li>
					</ul>
					<p><input id="filter_submit" type="submit" class="search_button" disabled="disabled" value="${ltext("control.search.button.search")}" /></p>
				</form>
			</div>
			
			<!-- Download -->
			<div id="download" class="clear_fix hidden">
				<h3>${ltext("control.download.create.title")}</h3>
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
	<p id="partial_match" class="clear_fix"><button type="button">${ltext("control.search.button.add")}</button> ${ltext("control.search.partial.operatorprefix")} <%= opText %>: <span id="partial_match_value"></span></p>
	</script>
	<!-- Text input -->
	<script type="text/template" id="filter_template_text_input">
	<p><input id="value_search" type="text"/></p>
	</script>
	<!-- Select box -->
	<script type="text/template" id="filter_template_select">
	<div>
		<p><select id="value_select"></select></p>
		<p class="clear_fix"><button type="button">${ltext("control.search.button.add")}</button></p>
	</div>
	</script>
	<!-- Suggestions -->
	<script type="text/template" id="filter_template_suggestions">
	<div id="filter_suggestions" class="round">
		<table id="value_suggestions">
		<tbody>
		<#list 1..10 as i>
		 	<tr class="hidden">
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
	<p><button type="button">${ltext("control.search.button.add")}</button> <%= fieldText %>: <input type="radio" name="boolGroup" value="true" checked /> ${ltext("filter.value.true")} <input type="radio" name="boolGroup" value="false" /> ${ltext("filter.value.false")}</p>
	</script>

<!-- Date template -->
<script type="text/template" id="filter_template_date">
<div id="filter_date">
	<p class="clear_fix">
		<span id="date_start"><label for="date_start_y" class="label_single">${ltext("control.search.date.singledate")}</label><label for="date_start_y" class="label_range hidden">${ltext("control.search.date.startdate")}</label>
			<input id="date_start_y" class="validationYear" type="text" maxlength="4" placeholder="yyyy"/>
			<input id="date_start_m" class="validationMonth" type="text" maxlength="2" placeholder="mm"/>
			<input id="date_start_d" class="validationDay" type="text" maxlength="2" placeholder="dd"/> 
		</span>
		<span id="date_end"><label for="date_end_y">${ltext("control.search.date.enddate")}</label>
			<input id="date_end_y" class="validationYear" type="text" maxlength="4" placeholder="yyyy"/>
			<input id="date_end_m" class="validationMonth" type="text" maxlength="2" placeholder="mm"/>
			<input id="date_end_d" class="validationDay" type="text" maxlength="2" placeholder="dd"/>
		</span>
	</p>
	<p class="clear_fix"><button type="button">${ltext("control.search.button.add")}</button> <input id="interval" type="checkbox" value="interval"/> ${ltext("control.search.date.range")}</p>
	<div class="filter_info round">
	<table>
		<tr><td>${ltext("control.search.date.example1.title")}</td><td>${ltext("control.search.date.example1")}</td></tr>
		<tr><td>${ltext("control.search.date.example2.title")}</td><td>${ltext("control.search.date.example2")}</td></tr>
		<tr><td>${ltext("control.search.date.example3.title")}</td><td>${ltext("control.search.date.example3")}</td></tr>
		<tr><td>${ltext("control.search.date.example4.title")}</td><td>${ltext("control.search.date.example4")}</td></tr>
		<tr><td>${ltext("control.search.date.example5.title")}</td><td>${ltext("control.search.date.example5")}</td></tr>
	</table>
	</div>
</div>
</script>

<!-- Minmax template -->
<script type="text/template" id="filter_template_minmax">
<div id="filter_minmax">
	<p class="clear_fix">
		<span id="interval_min"><label for="value_min" class="label_single">${ltext("control.search.minmax.single")}</label><label for="value_min" class="label_range hidden">${ltext("control.search.minmax.min")}</label> <input id="value_min" class="validationNumber" type="text"/></span>
		<span id="interval_max"><label for="value_max">${ltext("control.search.minmax.max")}</label> <input id="value_max" class="validationNumber" type="text"/></span>
	</p>
	<p class="clear_fix"><button type="button">${ltext("control.search.button.add")}</button> <input id="interval" type="checkbox" value="interval" /> ${ltext("control.search.minmax.range")}</p>
</div>
</script>

<!-- Current filters template -->
<script type="text/template" id="filter_template_current">
	<%= valueText %>
	<span>(<%= opText %>)</span>
	<span class="delete">${ltext("control.search.button.delete")}</span>
	<input type="hidden" name="<%= groupId %>_f" value="<%= searchableFieldId %>"/>
	<input type="hidden" name="<%= groupId %>_o" value="<%= op %>"/>
	<% _.each(value, function(value,key) { %> <input type="hidden" name="<%= groupId %>_v_<%= key+1 %>" value="<%= value %>"/> <% }); %>
</script>

<!-- Download templates -->
<script type="text/template" id="download_template_email">
	<div id="request">
		<p>${ltext("control.download.request.details1")} <strong>${root.occurrenceCount}</strong> ${ltext("control.download.request.details2")}</p>
		<p>${ltext("control.download.request.details3")}</p>
		<p>${ltext("control.download.request.details4")}</p>
		<p><label for="email">${ltext("control.download.email.label")}</label><input id="email" type="text"/></p>
		<p class="clear_fix"><button type="button" onClick="_gaq.push(['_trackEvent', 'Archive', 'Download', '${root.occurrenceCount}']);">${ltext("control.download.button.send")}</button></p>
	</div>
	<div id="status" class="hidden">
		<p>${ltext("control.download.status.details1")}</p>
		<p>${ltext("control.download.status.details2")}</p>
	</div>
</script>

<!-- Display templates -->
<script type="text/template" id="display_template_table">
	<h3>${ltext("control.display.table.title")}</h3>
	<p>${ltext("control.display.table.details1")}</p>
	<ul id="display_columns" class="custom_list">
	<#-- List items are added by rwd-table.js. Items with class="persist" are not available to deselect. -->
	</ul>
</script>

<script type="text/template" id="display_template_map">
	<p>${ltext("control.display.map.details1")}</p>
</script>

<#-- JavaScript init call functions -->
<#assign javaScriptSetupCallList = javaScriptSetupCallList + ["languageResources.setLanguageResources(${root.languageResources})","searchAndFilter.setAvailableSearchFields(${root.availableFiltersMap})","searchAndFilter.loadFilter(${root.searchCriteria})","searchAndFilter.setNumberOfResult(${root.occurrenceCount?c})"]>