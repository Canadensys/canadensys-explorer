<h2>${rc.getMessage("view.stats.group.location")}</h2>
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.continent?c)?html}">${rc.getMessage("view.stats.continent")} (${root.continent_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.country?c)?html}">${rc.getMessage("view.stats.country")} (${root.country_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.stateprovince?c)?html}">${rc.getMessage("view.stats.stateprovince")} (${root.stateprovince_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.county?c)?html}">${rc.getMessage("view.stats.county")} (${root.county_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.municipality?c)?html}">${rc.getMessage("view.stats.municipality")} (${root.municipality_count!0}) </a>

<div class="stats_group chart_pie">
  <div class="chart_container" id="chart_pie"></div>
  <table class="stats_container" id="chart_table">
  <thead>
  	<tr>
     <th>${rc.getMessage("filter."+root.statsFieldKey)}</th>
     <th>${rc.getMessage("view.stats.count")}</th>
  	</tr>
  </thead>
  <tbody>
  <#list root.chartModel.rows as chartItem>
  	<tr>
  	<td>${chartItem[0]}</td>
  	<td>${chartItem[1]}</td>
  	</tr>
	</#list>
  </tbody> 
  </table>
</div>

<!-- JavaScript init call related to this statistic view -->
<#macro statsJavaScriptInit>
	EXPLORER.chart.loadPieChart('${root.statsFieldKey}',${root.chartRowsJSON});
</#macro>