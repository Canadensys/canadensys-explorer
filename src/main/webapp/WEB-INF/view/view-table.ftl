<#include "inc/functions.ftl">
<#include "inc/paging.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("page.search.title")}</title>
<@cssAsset fileName="occportal" version=root.currentVersion! useMinified=false/> 
</head>
<content tag="lang_switch">
<@i18nLanguageSwitch resourceName="search"/>
</content>
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
				<li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","map")}">${rc.getMessage("view.map.header.button")}</a></li>
				<li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","table")}" class="selected">${rc.getMessage("view.table.header.button")}</a></li>
				<li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","stats")}">${rc.getMessage("view.stats.header.button")}</a></li>
			</ul>
		</div>
		<a id="main-content"></a>
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
	<#if ((root.occurrenceCount!0) >= root.pageSize)>
		<#assign totalPages=(root.occurrenceCount!0/root.pageSize)?ceiling/>
		<@pages 1..totalPages root.pageNumber!1 />
	</#if>
</div><#-- body -->

<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<@jsLibAsset libName="json2.js"/>
<@jsLibAsset libName="underscore-min.js"/>
<@jsLibAsset libName="backbone-min.js"/>
<@jsLibAsset libName="sorttable.js"/>
<@jsAsset fileName="explorer" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.utils" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.backbone" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.portal" version=root.currentVersion! useMinified=root.useMinified/>
<@jsLibAsset libName="rwd-table.js"/>
<@jsLibAsset libName="respond.js"/>

<script>
$(function() {
	EXPLORER.control.restoreDisplay();
	<@controlJavaScriptInit/> 
});
</script>
</content>
