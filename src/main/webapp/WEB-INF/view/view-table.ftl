<#include "inc/functions.ftl">
<#assign page={"title":rc.getMessage("page.search.title"),"cssList":["${root.rootURL?if_exists}styles/occportal.css"],"prefetchList":["http://tiles.canadensys.net"],
"javaScriptIncludeList":
["http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js",
"js/lib/json2.js",
"js/lib/underscore-min.js",
"js/lib/backbone-min.js",
"js/lib/sorttable.js",
"js/${formatFileInclude(\"occurrence-utils\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/${formatFileInclude(\"occurrence-backbone\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/${formatFileInclude(\"occurrence-portal\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/lib/rwd-table.js",
"js/lib/respond.js"]}>

<#include "inc/header.ftl">
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
			<p class="details">(${ltext("view.table.header.details")})</p>
			<ul class="buttons">
				<li><a href="${root.mapViewURL}">${ltext("view.map.header.button")}</a></li>
				<li><a href="${root.tableViewURL}" class="selected">${ltext("view.table.header.button")}</a></li>
				<li><a href="${root.statsViewURL}">${ltext("view.stats.header.button")}</a></li>
			</ul>
		</div>
		<div id="table_wrapper">
			<table id="results" class="sortable">
			<thead>
				<tr>
					<th class="view_1 sorttable_alpha">${ltext("occ.scientificname")}</th>
					<th class="view_2 sorttable_alpha">${ltext("occ.family")}</th>
					<th class="view_1 sorttable_alpha">${ltext("occ.country")}</th>
					<th class="view_1 sorttable_alpha">${ltext("occ.stateprovince")}</th>
					<th class="view_3 sorttable_alpha">${ltext("occ.locality")}</th>
					<th class="view_3 sorttable_alpha">${ltext("occ.habitat")}</th>
					<th class="view_2 sorttable_numeric">${ltext("occ.syear")}</th>
					<th class="view_1 sorttable_alpha">${ltext("occ.collectioncode")}</th>
					<th class="view_2 sorttable_numeric">${ltext("occ.catalognumber")}</th>
					<th class="view_1 sorttable_numeric extra">${ltext("view.table.extra")}</th>
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

<#assign page = page + {"jQueryJavaScriptSetupCallList": page.jQueryJavaScriptSetupCallList + ["occurrenceControl.restoreDisplay()"]}>
<#include "inc/footer.ftl">
