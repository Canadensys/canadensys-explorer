<!DOCTYPE html>
<html>
<#include "inc/functions.ftl">
<#assign javaScriptIncludeList = ["js/chart.js","js/occurrence-portal.js","js/stats-backbone.js"]>
<#assign javaScriptSetupCallList = []>
<#include "inc/header.ftl">
<body>
<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${ltext("feedback.hover")}">&nbsp;</a></div>
<#include "inc/canadensys-header.ftl">
<div id="body" class="fullscreen">
	<#include "inc/control.ftl">
	<div id="view" class="clear_fix">
		<div class="nav_container">
			<#if root.allRecordsTargeted >
				<h1>${ltext("view.header.all")} ${root.occurrenceCount} ${ltext("view.header.records")}</h1>
			<#else>
				<h1>${root.occurrenceCount} ${ltext("view.header.results")}</h1>
			</#if>
			<p class="details" id="spinner">(${ltext("view.stats.header.loading")})</p>
			<ul class="buttons">
				<li><a href="${root.mapViewURL}">${ltext("view.map.header.button")}</a></li>
				<li><a href="${root.tableViewURL}">${ltext("view.table.header.button")}</a></li>
				<li><a href="${root.statsViewURL}" class="selected">${ltext("view.stats.header.button")}</a></li>
			</ul>
		</div>
		
		<div class="stats_group chart_pie">
			<h2>${ltext("view.stats.group.classification")}</h2>
			<div class="chart_container" id="classification_chart_div"></div>
			<table class="stats_container" id="classification_stats">
			<tbody>
			<#-- Content injected with javascript -->
			</tbody> 
			</table>
		</div>
		
		<div class="stats_group chart_pie">
			<h2>${ltext("view.stats.group.location")}</h2>
			<div class="chart_container" id="location_chart_div"></div>
			<table class="stats_container" id="location_stats">
			<tbody>
			<#-- Content injected with javascript -->
			</tbody> 
			</table>	
		</div>
		
		<div class="stats_group chart_histogram">
			<h2>${ltext("view.stats.group.date")}</h2>	
			<div class="chart_container" id="decade_chart_div"></div>
		</div>
		
		<div class="stats_group chart_histogram">
			<h2>${ltext("view.stats.group.altitude")}</h2>	
			<div class="chart_container" id="altitude_chart_div"></div>
		</div>
		
		<script type="text/template" id="stats_field_template">
			<tr>
				<td><input type="radio" name="group<%= groupId %>" value="<%= fieldId %>" id="<%= fieldId %>"/> <label for="<%= fieldId %>"><%= fieldText %></label></td>
				<td class="right">-</td>
			</tr>
		</script>
	</div>
</div><#-- body -->

<#assign javaScriptIncludeList = ["http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js",
"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js","js/lib/json2.js","https://www.google.com/jsapi","js/lib/underscore-min.js","js/lib/backbone-min.js","js/occurrence-utils.js","js/occurrence-backbone.js"]+javaScriptIncludeList>

<#assign javaScriptSetupCallList = javaScriptSetupCallList + [
"occurrenceStats.initStatsView(searchAndFilter.getInitialFilterParamMap())",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.kingdom},\"${ltext(\"view.stats.kingdom\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.phylum},\"${ltext(\"view.stats.phylum\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters._class},\"${ltext(\"view.stats.class\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters._order},\"${ltext(\"view.stats.order\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.family},\"${ltext(\"view.stats.family\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.genus},\"${ltext(\"view.stats.genus\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.scientificname},\"${ltext(\"view.stats.scientificname\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.continent},\"${ltext(\"view.stats.continent\")}\",'location')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.country},\"${ltext(\"view.stats.country\")}\",'location')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.stateprovince},\"${ltext(\"view.stats.stateprovince\")}\",'location')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.county},\"${ltext(\"view.stats.county\")}\",'location')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.municipality},\"${ltext(\"view.stats.municipality\")}\",'location')",
"occurrenceStats.loadStaticChart(${root.availableFilters.decade},'decade',\"${ltext(\"view.stats.chart.decade.title\")}\")",
"occurrenceStats.loadStaticChart(${root.availableFilters.averagealtituderounded},'altitude',\"${ltext(\"view.stats.chart.altitude.title\")}\")",
"occurrenceStats.selectDefaultChart(${root.availableFilters.family})",
"occurrenceStats.selectDefaultChart(${root.availableFilters.stateprovince})"]>
<#assign nojQueryJSSetupCallList = ["google.load('visualization', '1', {'packages':['corechart']})"]>

<#include "inc/footer.ftl">
</body>
</html>