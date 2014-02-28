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

      <p class="details" id="spinner">(${rc.getMessage("view.stats.header.loading")})</p>
      <ul class="buttons">
        <li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","map")}">${rc.getMessage("view.map.header.button")}</a></li>
        <li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","table")}">${rc.getMessage("view.table.header.button")}</a></li>
        <li><a href="?${URLHelper.replaceCurrentQueryParam(Request,"view","stats")}" class="selected">${rc.getMessage("view.stats.header.button")}</a></li>
      </ul>
    </div>
    <a id="main-content"></a>
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
<@jsAsset fileName="explorer.backbone.stats" version=root.currentVersion! useMinified=root.useMinified/>

<script>
$(function() {
  EXPLORER.stats.initStatsView(EXPLORER.backbone.getInitialFilterParamMap());
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.kingdom},"${rc.getMessage("view.stats.kingdom")}",'classification');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.phylum},"${rc.getMessage("view.stats.phylum")}",'classification');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters._class},"${rc.getMessage("view.stats.class")}",'classification');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters._order},"${rc.getMessage("view.stats.order")}",'classification');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.family},"${rc.getMessage("view.stats.family")}",'classification');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.genus},"${rc.getMessage("view.stats.genus")}",'classification');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.scientificname},"${rc.getMessage("view.stats.scientificname")}",'classification');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.continent},"${rc.getMessage("view.stats.continent")}",'location');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.country},"${rc.getMessage("view.stats.country")}",'location');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.stateprovince},"${rc.getMessage("view.stats.stateprovince")}",'location');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.county},"${rc.getMessage("view.stats.county")}",'location');
  EXPLORER.stats.loadFieldUniqueCount(${root.availableFilters.municipality},"${rc.getMessage("view.stats.municipality")}",'location');
  EXPLORER.stats.loadStaticChart(${root.availableFilters.decade},'decade',"${rc.getMessage("view.stats.chart.decade.title")}");
  EXPLORER.stats.loadStaticChart(${root.availableFilters.averagealtituderounded},'altitude',"${rc.getMessage("view.stats.chart.altitude.title")}");
  EXPLORER.stats.selectDefaultChart(${root.availableFilters.family});
  EXPLORER.stats.selectDefaultChart(${root.availableFilters.stateprovince});

  <@controlJavaScriptInit/>
});
google.load('visualization', '1', {'packages':['corechart']});
</script>
</content>
