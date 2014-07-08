<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("page.search.title")}</title>
<@cssAsset fileName="occportal" version=page.currentVersion! useMinified=false/>
</head>
<div id="body" class="fullscreen">
  <#include "inc/control.ftl">
  <div id="view" class="clear_fix">
    <div class="nav_container">

    <#if page.allRecordsTargeted >
      <h1>${rc.getMessage("view.header.results.all", [page.occurrenceCount])}</h1>
    <#else>
      <h1>${rc.getMessage("view.header.results",[page.occurrenceCount])}</h1>
    </#if>
      <ul class="buttons">
        <li><a href="<@searchViewUrl view="map"/>">${rc.getMessage("view.map.header.button")}</a></li>
        <li><a href="<@searchViewUrl view="table"/>">${rc.getMessage("view.table.header.button")}</a></li>
        <li><a href="<@searchViewUrl view="stats"/>" class="selected">${rc.getMessage("view.stats.header.button")}</a></li>
      </ul>
    </div>

    <a id="main-content"></a>
    <ul class="buttons sub_heading">
      <li><a ${getStatsViewButtonClass("classification", page.statsGroupKey)} href="?${URLHelper.replaceCurrentQueryParams(Request,["stat_group","classification","stat_sel",""])}">${rc.getMessage("view.stats.group.classification")}</a></li>
      <li><a ${getStatsViewButtonClass("location", page.statsGroupKey)} href="?${URLHelper.replaceCurrentQueryParams(Request,["stat_group","location","stat_sel",""])}">${rc.getMessage("view.stats.group.location")}</a></li>
      <li><a ${getStatsViewButtonClass("date", page.statsGroupKey)} href="?${URLHelper.replaceCurrentQueryParams(Request,["stat_group","date","stat_sel",""])}">${rc.getMessage("view.stats.group.date")}</a></li>
      <li><a ${getStatsViewButtonClass("altitude", page.statsGroupKey)} href="?${URLHelper.replaceCurrentQueryParams(Request,["stat_group","altitude","stat_sel",""])}">${rc.getMessage("view.stats.group.altitude")}</a></li>
    </ul>

    <#switch page.statsGroupKey?lower_case>
	    <#case "classification">
			<#include "fragment/stats-classification.ftl">
	    	<#break>
	    <#case "location">
			<#include "fragment/stats-location.ftl">
	    	<#break>
	    <#case "date">
			<#include "fragment/stats-date.ftl">
	    	<#break>
	    <#case "altitude">
			<#include "fragment/stats-altitude.ftl">
	    	<#break>
	    <#default>
	    	<#include "fragment/stats-classification.ftl">
    </#switch>
  </div>
</div><#-- body -->

<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//www.google.com/jsapi"></script>
<@jsLibAsset libName="jquery-ui-1.10.4.custom.min.js"/>
<@jsLibAsset libName="underscore-min.js"/>
<@jsLibAsset libName="backbone-min.js"/>
<@jsAsset fileName="explorer" version=page.currentVersion! useMinified=page.useMinified/>
<@jsAsset fileName="explorer.portal" version=page.currentVersion! useMinified=page.useMinified/>
<@jsAsset fileName="explorer.utils" version=page.currentVersion! useMinified=page.useMinified/>
<@jsAsset fileName="explorer.chart" version=page.currentVersion! useMinified=page.useMinified/>
<@jsAsset fileName="explorer.backbone" version=page.currentVersion! useMinified=page.useMinified/>

<script>
$(function() {
  <@controlJavaScriptSettings/>
  <@controlJavaScriptInit/>
  <@statsJavaScriptInit/>
});
google.load('visualization', '1', {'packages':['corechart']});
</script>
</content>
