<#include "inc/functions.ftl">
<#assign page={"title":rc.getMessage("page.search.title"),"cssList":[rc.getContextUrl('/styles/occportal.css')],"prefetchList":["http://tiles.canadensys.net"],
"javaScriptIncludeList":
["http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
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
"js/${formatFileInclude(\"map-view\",root.currentVersion?if_exists,root.useMinified,\".js\")}"]}>
<#include "inc/header.ftl">
<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${rc.getMessage("feedback.hover")}">&nbsp;</a></div>
<#include "inc/canadensys-header.ftl">
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
		<div id="map_canvas">
		</div>
	</div>
</div><#-- body -->
<#assign page = page + {"jQueryJavaScriptSetupCallList": page.jQueryJavaScriptSetupCallList + ["occurrenceMap.setupMap('occ_preview','map_canvas',\"${root.embeddedMapQuery}\")"]}>
<#include "inc/footer.ftl">
