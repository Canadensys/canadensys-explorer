<h2>${rc.getMessage("view.stats.group.location")}</h2>

${getStatsFieldTag("continent", page.continent_count!0, page.availableFilters.continent?c, page.statsFieldKey)} |
${getStatsFieldTag("country", page.country_count!0, page.availableFilters.country?c, page.statsFieldKey)} |
${getStatsFieldTag("stateprovince", page.stateprovince_count!0, page.availableFilters.stateprovince?c, page.statsFieldKey)} |
${getStatsFieldTag("county", page.county_count!0, page.availableFilters.county?c, page.statsFieldKey)} |
${getStatsFieldTag("municipality", page.municipality_count!0, page.availableFilters.municipality?c, page.statsFieldKey)}

<div class="stats_group chart_pie">
  <div class="chart_container" id="chart_pie"></div>
  <table class="stats_container" id="chart_table">
  <thead>
  	<tr>
     <th scope="col">${rc.getMessage("filter."+page.statsFieldKey)}</th>
     <th scope="col">${rc.getMessage("view.stats.count")}</th>
     <th scope="col">%</th>
  	</tr>
  </thead>
  <tbody>
  <#list page.statsData?keys as currKey>
  	<tr>
  	  <td>${currKey}</td>
  	  <td>${page.statsData[currKey]}</td>
  	  <td>${(page.statsData[currKey]/page.occurrenceCount)?string.percent}</td>
  	</tr>
  </#list>
  </tbody> 
  </table>
</div>

<!-- JavaScript init call related to this statistic view -->
<#macro statsJavaScriptInit>
	EXPLORER.chart.loadPieChart('${rc.getMessage("view.stats.chart.piechart.title")+ " " + rc.getMessage("filter."+page.statsFieldKey) }','${page.statsFieldKey}',${page.statsDataJSON});
</#macro>