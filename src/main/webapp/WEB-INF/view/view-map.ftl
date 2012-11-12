<!DOCTYPE html>
<html>
<#include "inc/functions.ftl">
<#assign cssList =["http://code.google.com/apis/maps/documentation/javascript/examples/default.css","styles/cartodb_gv3.css"]>

<#assign javaScriptIncludeList = [
"http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js",
"js/lib/json2.js",
"js/lib/underscore-min.js",
"js/lib/backbone-min.js",
"js/${formatFileInclude(\"occurrence-utils\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/${formatFileInclude(\"occurrence-backbone\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"http://maps.googleapis.com/maps/api/js?sensor=false",
"js/lib/wax.g.min-6.2.0-touched.js",
"js/lib/cartodb-gmapsv3-min.js",
"js/${formatFileInclude(\"occurrence-portal\",root.currentVersion?if_exists,root.useMinified,\".js\")}",
"js/${formatFileInclude(\"map-view\",root.currentVersion?if_exists,root.useMinified,\".js\")}"]>

<#assign javaScriptSetupCallList = []>
<#include "inc/header.ftl">
<body>
<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${ltext("feedback.hover")}">&nbsp;</a></div>
<#include "inc/canadensys-header.ftl">
<div id="body" class="fullscreen full_height">
	<#include "inc/control.ftl">
	<div id="view" class="clear_fix">
		<div class="nav_container">
			<#if root.allRecordsTargeted >
				<h1>${ltext("view.header.all")} ${root.occurrenceCount} ${ltext("view.header.records")}</h1>
			<#else>
				<h1>${root.occurrenceCount} ${ltext("view.header.results")}</h1>
			</#if>
			<p class="details">(${ltext("view.map.header.details")}: ${root.georeferencedOccurrenceCount})</p>
			<ul class="buttons">
				<li><a href="${root.mapViewURL}" class="selected">${ltext("view.map.header.button")}</a></li>
				<li><a href="${root.tableViewURL}">${ltext("view.table.header.button")}</a></li>
				<li><a href="${root.statsViewURL}">${ltext("view.stats.header.button")}</a></li>
			</ul>
		</div>
		<div id="map_canvas">
		</div>
	</div>
</div><#-- body -->
<#assign javaScriptSetupCallList = javaScriptSetupCallList + ["occurrenceMap.setupMap('occ_preview','map_canvas',\"${root.embeddedMapQuery}\")"]>
<#include "inc/footer.ftl">
</body>
</html>