<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">

<head>
<title>${rc.getMessage("page.search.title")}</title>
<link rel="stylesheet" href="${rc.getContextUrl("/styles/"+formatFileInclude("occportal",root.currentVersion!,false,".css"))}" media="screen,print"/>
<link rel="dns-prefetch" href="http://tiles.canadensys.net"/>
<link rel="prefetch" href="http://tiles.canadensys.net"/>
</head>

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
			<p class="details">(${rc.getMessage("view.table.header.details")})</p>
			<ul class="buttons">
				<li><a href="${root.mapViewURL}">${rc.getMessage("view.map.header.button")}</a></li>
				<li><a href="${root.tableViewURL}" class="selected">${rc.getMessage("view.table.header.button")}</a></li>
				<li><a href="${root.statsViewURL}">${rc.getMessage("view.stats.header.button")}</a></li>
			</ul>
		</div>
		<div id="table_wrapper">
			<table id="results" class="sortable">
			<thead>
				<tr>
					<th class="view_1 sorttable_alpha">${rc.getMessage("occ.scientificname")}</th>
					<th class="view_2 sorttable_alpha">${rc.getMessage("occ.family")}</th>
					<th class="view_1 sorttable_alpha">${rc.getMessage("occ.country")}</th>
					<th class="view_1 sorttable_alpha">${rc.getMessage("occ.stateprovince")}</th>
					<th class="view_3 sorttable_alpha">${rc.getMessage("occ.locality")}</th>
					<th class="view_3 sorttable_alpha">${rc.getMessage("occ.habitat")}</th>
					<th class="view_2 sorttable_numeric">${rc.getMessage("occ.syear")}</th>
					<th class="view_1 sorttable_alpha">${rc.getMessage("occ.collectioncode")}</th>
					<th class="view_2 sorttable_numeric">${rc.getMessage("occ.catalognumber")}</th>
					<th class="view_1 sorttable_numeric extra">${rc.getMessage("view.table.extra")}</th>
					<th class="view_1 persist last_column"></th>
				</tr>
			</thead>
			<tbody>
			<#list root.occurrenceList as currOccurrence>

				<#if currOccurrence.hascoordinates == "true" && currOccurrence.hasmedia == "true">
					<#assign iconsort = 1>
				<#elseif currOccurrence.hascoordinates == "true">
					<#assign iconsort = 2>
				<#elseif currOccurrence.hasmedia == "true">
					<#assign iconsort = 3>
				<#else>
					<#assign iconsort = 4>
				</#if>

				<tr id="${currOccurrence.auto_id}">
					<td>${currOccurrence.scientificname}</td>
					<td>${currOccurrence.family}</td>
					<td>${currOccurrence.country}</td>
					<td>${currOccurrence.stateprovince}</td>
					<td>${currOccurrence.locality}</td>
					<td>${currOccurrence.habitat}</td>
					<td>${currOccurrence.syear}</td>
					<td>${currOccurrence.collectioncode}</td>
					<td>${currOccurrence.catalognumber}</td>
					<td class="icon_column icon_${iconsort}" sorttable_customkey="${iconsort}"></td>
					<td class="last_column"></td>
				</tr>
			</#list>
			</tbody>
			</table>
		</div><#-- table_wrapper -->
	</div>
</div><#-- body -->

<content tag="local_script">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<script src="${rc.getContextUrl("/js/"+formatFileInclude("occurrence-portal",root.currentVersion!,root.useMinified,".js"))}"></script>
<script src="${rc.getContextUrl("/js/lib/json2.js")}"></script>
<script src="${rc.getContextUrl("/js/lib/underscore-min.js")}"></script>
<script src="${rc.getContextUrl("/js/lib/backbone-min.js")}"></script>
<script src="${rc.getContextUrl("/js/lib/sorttable.js")}"></script>
<script src="${rc.getContextUrl("/js/"+formatFileInclude("occurrence-utils",root.currentVersion!,root.useMinified,".js"))}"></script>
<script src="${rc.getContextUrl("/js/"+formatFileInclude("occurrence-backbone",root.currentVersion!,root.useMinified,".js"))}"></script>
<script src="${rc.getContextUrl("/js/lib/rwd-table.js")}"></script>
<script src="${rc.getContextUrl("/js/lib/respond.js")}"></script>

<script>
$(function() {
	occurrenceControl.restoreDisplay();
	<@controlJavaScriptInit/> 
});
</script>
</content>
