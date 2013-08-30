<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<#assign page={"title":rc.getMessage("page.search.title"),"cssList":[rc.getContextUrl('/styles/occportal.css')],"prefetchList":["http://tiles.canadensys.net"],
"javaScriptIncludeList":
["http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js",
"js/lib/json2.js",
"https://www.google.com/jsapi",
"js/lib/underscore-min.js",
"js/lib/backbone-min.js",
"js/${formatFileInclude(\"occurrence-utils\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/${formatFileInclude(\"occurrence-backbone\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/${formatFileInclude(\"chart\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/${formatFileInclude(\"occurrence-portal\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/${formatFileInclude(\"stats-backbone\",root.currentVersion?if_exists,root.useMinified,\".js\")}"]}>
<#include "inc/header.ftl">
<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${rc.getMessage("feedback.hover")}">&nbsp;</a></div>
<#include "inc/canadensys-header.ftl">
<div id="body" class="fullscreen">
	<#include "inc/control.ftl">
	<div id="view" class="clear_fix">
		<div class="nav_container">
			<#if root.allRecordsTargeted >
				<h1>${rc.getMessage("view.header.all")} ${root.occurrenceCount} ${rc.getMessage("view.header.records")}</h1>
			<#else>
				<h1>${root.occurrenceCount} ${rc.getMessage("view.header.results")}</h1>
			</#if>
			<p class="details" id="spinner">(${rc.getMessage("view.stats.header.loading")})</p>
			<ul class="buttons">
				<li><a href="${root.mapViewURL}">${rc.getMessage("view.map.header.button")}</a></li>
				<li><a href="${root.tableViewURL}">${rc.getMessage("view.table.header.button")}</a></li>
				<li><a href="${root.statsViewURL}" class="selected">${rc.getMessage("view.stats.header.button")}</a></li>
			</ul>
		</div>
		
		<div class="stats_group chart_pie">
			<h2>${rc.getMessage("view.stats.group.classification")}</h2>
			<div class="chart_container" id="classification_chart_div"></div>
			<table class="stats_container" id="classification_stats">
			<tbody>
			<#-- Content injected with javascript -->
			</tbody> 
			</table>
		</div>
		
		<div class="stats_group chart_pie">
			<h2>${rc.getMessage("view.stats.group.location")}</h2>
			<div class="chart_container" id="location_chart_div"></div>
			<table class="stats_container" id="location_stats">
			<tbody>
			<#-- Content injected with javascript -->
			</tbody> 
			</table>	
		</div>
		
		<div class="stats_group chart_histogram">
			<h2>${rc.getMessage("view.stats.group.date")}</h2>	
			<div class="chart_container" id="decade_chart_div"></div>
		</div>
		
		<div class="stats_group chart_histogram">
			<h2>${rc.getMessage("view.stats.group.altitude")}</h2>	
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
<#assign page = page + {"jQueryJavaScriptSetupCallList": page.jQueryJavaScriptSetupCallList + [
"occurrenceStats.initStatsView(searchAndFilter.getInitialFilterParamMap())",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.kingdom},\"${rc.getMessage(\"view.stats.kingdom\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.phylum},\"${rc.getMessage(\"view.stats.phylum\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters._class},\"${rc.getMessage(\"view.stats.class\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters._order},\"${rc.getMessage(\"view.stats.order\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.family},\"${rc.getMessage(\"view.stats.family\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.genus},\"${rc.getMessage(\"view.stats.genus\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.scientificname},\"${rc.getMessage(\"view.stats.scientificname\")}\",'classification')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.continent},\"${rc.getMessage(\"view.stats.continent\")}\",'location')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.country},\"${rc.getMessage(\"view.stats.country\")}\",'location')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.stateprovince},\"${rc.getMessage(\"view.stats.stateprovince\")}\",'location')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.county},\"${rc.getMessage(\"view.stats.county\")}\",'location')",
"occurrenceStats.loadFieldUniqueCount(${root.availableFilters.municipality},\"${rc.getMessage(\"view.stats.municipality\")}\",'location')",
"occurrenceStats.loadStaticChart(${root.availableFilters.decade},'decade',\"${rc.getMessage(\"view.stats.chart.decade.title\")}\")",
"occurrenceStats.loadStaticChart(${root.availableFilters.averagealtituderounded},'altitude',\"${rc.getMessage(\"view.stats.chart.altitude.title\")}\")",
"occurrenceStats.selectDefaultChart(${root.availableFilters.family})",
"occurrenceStats.selectDefaultChart(${root.availableFilters.stateprovince})"]}>
<#if !(page.javaScriptSetupCallList)??>
<#assign page = page + {"javaScriptSetupCallList":[]}>
</#if>
<#assign page = page + {"javaScriptSetupCallList" : page.javaScriptSetupCallList + ["google.load('visualization', '1', {'packages':['corechart']})"]}>
<#include "inc/footer.ftl">
