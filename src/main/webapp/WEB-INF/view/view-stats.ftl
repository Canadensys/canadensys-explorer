<#include "inc/functions.ftl">
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
      <h1>${rc.getMessage("view.header.results.all", [root.occurrenceCount])}</h1>
    <#else>
      <h1>${rc.getMessage("view.header.results",[root.occurrenceCount])}</h1>
    </#if>

      <ul class="buttons">
        <li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","map")}">${rc.getMessage("view.map.header.button")}</a></li>
        <li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","table")}">${rc.getMessage("view.table.header.button")}</a></li>
        <li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","stats")}" class="selected">${rc.getMessage("view.stats.header.button")}</a></li>
      </ul>
    </div>
    <a id="main-content"></a>
    <#-- TODO includes the right fragment based on ? -->
    <#include "fragment/stats-classification.ftl">
  </div>
</div><#-- body -->

<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//www.google.com/jsapi"></script>
<@jsLibAsset libName="jquery-ui-1.10.4.custom.min.js"/>
<@jsLibAsset libName="underscore-min.js"/>
<@jsLibAsset libName="backbone-min.js"/>
<@jsAsset fileName="explorer" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.portal" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.utils" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.chart" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.backbone" version=root.currentVersion! useMinified=root.useMinified/>

<script>
$(function() {
  <@controlJavaScriptInit/>
  EXPLORER.chart.loadPieChart('${root.statsFieldKey}',${root.chartRowsJSON});
});
google.load('visualization', '1', {'packages':['corechart']});
</script>
</content>
