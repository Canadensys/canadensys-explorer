<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("page.search.title")}</title>
<link rel="stylesheet" href="${rc.getContextUrl("/styles/"+formatFileInclude("occportal",root.currentVersion!,false,".css"))}" media="screen,print"/>
<link rel="dns-prefetch" href="http://tiles.canadensys.net"/>
<link rel="prefetch" href="http://tiles.canadensys.net"/>
</head>

<div id="body" class="fullscreen full_height">
	<#include "inc/control.ftl">
	<div id="view" class="clear_fix">
		<div class="nav_container">
			<#if root.allRecordsTargeted >
				<h1>${rc.getMessage("view.header.all")} ${root.occurrenceCount} ${rc.getMessage("view.header.records")}</h1>
			<#else>
				<h1>${root.occurrenceCount} ${rc.getMessage("view.header.results")}</h1>
			</#if>
			<p class="details">(${rc.getMessage("view.map.header.details")}: ${root.georeferencedOccurrenceCount})</p>
			<ul class="buttons">
				<li><a href="${root.mapViewURL}" class="selected">${rc.getMessage("view.map.header.button")}</a></li>
				<li><a href="${root.tableViewURL}">${rc.getMessage("view.table.header.button")}</a></li>
				<li><a href="${root.statsViewURL}">${rc.getMessage("view.stats.header.button")}</a></li>
			</ul>
		</div>
		<a id="main-content"></a>
		<div id="map_canvas">
		</div>
	</div>
</div><#-- body -->

<content tag="local_script">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script src="${rc.getContextUrl("/js/"+formatFileInclude("occurrence-portal",root.currentVersion!,root.useMinified,".js"))}"></script>
<script src="${rc.getContextUrl("/js/lib/json2.js")}"></script>
<script src="${rc.getContextUrl("/js/lib/underscore-min.js")}"></script>
<script src="${rc.getContextUrl("/js/lib/backbone-min.js")}"></script>
<script src="${rc.getContextUrl("/js/"+formatFileInclude("occurrence-utils",root.currentVersion!,root.useMinified,".js"))}"></script>
<script src="${rc.getContextUrl("/js/"+formatFileInclude("occurrence-backbone",root.currentVersion!,root.useMinified,".js"))}"></script>
<script src="${rc.getContextUrl("/js/lib/wax.g.min-6.2.0-touched.js")}"></script>
<script src="${rc.getContextUrl("/js/lib/cartodb-gmapsv3-min.js")}"></script>
<script src="${rc.getContextUrl("/js/"+formatFileInclude("map-view",root.currentVersion!,root.useMinified,".js"))}"></script>

<script>
$(function() {
	occurrenceMap.setupMap('occ_preview','map_canvas',"${root.embeddedMapQuery}");
	<@controlJavaScriptInit/> 
});
</script>
</content>
