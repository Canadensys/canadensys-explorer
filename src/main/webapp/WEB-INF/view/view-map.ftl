<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("page.search.title")}</title>
<@cssAsset fileName="occportal" version=root.currentVersion! useMinified=false/>
<link rel="dns-prefetch" href="http://tiles.canadensys.net"/>
<link rel="prefetch" href="http://tiles.canadensys.net"/>
</head>
<content tag="lang_switch">
<@i18nLanguageSwitch resourceName="search"/>
</content>
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
				<li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","map")}" class="selected">${rc.getMessage("view.map.header.button")}</a></li>
				<li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","table")}">${rc.getMessage("view.table.header.button")}</a></li>
				<li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","stats")}">${rc.getMessage("view.stats.header.button")}</a></li>
			</ul>
		</div>
		<a id="main-content"></a>
		<div id="map_canvas">
		</div>
	</div>
</div><#-- body -->

<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<script src="//maps.googleapis.com/maps/api/js?sensor=false"></script>
<@jsLibAsset libName="json2.js"/>
<@jsLibAsset libName="underscore-min.js"/>
<@jsLibAsset libName="backbone-min.js"/>
<@jsLibAsset libName="wax.g.min-6.2.0-touched.js"/>
<@jsLibAsset libName="cartodb-gmapsv3-min.js"/>
<@jsAsset fileName="explorer" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.utils" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.portal" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.backbone" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.map" version=root.currentVersion! useMinified=root.useMinified/>

<script>
$(function() {
	EXPLORER.map.setupMap('occ_preview','map_canvas',"${root.embeddedMapQuery}");
	<@controlJavaScriptInit/> 
});
</script>
</content>
