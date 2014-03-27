<h2>${rc.getMessage("view.stats.group.location")}</h2>

${getStatsFieldTag("continent", root.continent_count!0, root.availableFilters.continent?c, root.statsFieldKey)} |
${getStatsFieldTag("country", root.country_count!0, root.availableFilters.country?c, root.statsFieldKey)} |
${getStatsFieldTag("stateprovince", root.stateprovince_count!0, root.availableFilters.stateprovince?c, root.statsFieldKey)} |
${getStatsFieldTag("county", root.county_count!0, root.availableFilters.county?c, root.statsFieldKey)} |
${getStatsFieldTag("municipality", root.municipality_count!0, root.availableFilters.municipality?c, root.statsFieldKey)}

<div class="stats_group chart_pie">
  <div class="chart_container" id="chart_pie"></div>
  <table class="stats_container" id="chart_table">
  <thead>
  	<tr>
     <th scope="col">${rc.getMessage("filter."+root.statsFieldKey)}</th>
     <th scope="col">${rc.getMessage("view.stats.count")}</th>
     <th scope="col">%</th>
  	</tr>
  </thead>
  <tbody>
  <#list root.statsData?keys as currKey>
  	<tr>
  	  <td>${currKey}</td>
  	  <td>${root.statsData[currKey]}</td>
  	  <td>${(root.statsData[currKey]/root.occurrenceCount)?string.percent}</td>
  	</tr>
  </#list>
  </tbody> 
  </table>
</div>

<!-- JavaScript init call related to this statistic view -->
<#macro statsJavaScriptInit>
	EXPLORER.chart.loadPieChart('${rc.getMessage("view.stats.chart.piechart.title")+ " " + rc.getMessage("filter."+root.statsFieldKey) }','${root.statsFieldKey}',${root.statsDataJSON});
</#macro>