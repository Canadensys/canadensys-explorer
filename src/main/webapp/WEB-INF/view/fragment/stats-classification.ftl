<h2>${rc.getMessage("view.stats.group.classification")}</h2>

${getStatsFieldTag("kingdom", page.kingdom_count!0, page.availableFilters.kingdom?c, page.statsFieldKey)} |
${getStatsFieldTag("phylum", page.phylum_count!0, page.availableFilters.phylum?c, page.statsFieldKey)} |
${getStatsFieldTag("_class", page._class_count!0, page.availableFilters._class?c, page.statsFieldKey)} |
${getStatsFieldTag("_order", page._order_count!0, page.availableFilters._order?c, page.statsFieldKey)} |
${getStatsFieldTag("family", page.family_count!0, page.availableFilters.family?c, page.statsFieldKey)} |
${getStatsFieldTag("genus", page.genus_count!0, page.availableFilters.genus?c, page.statsFieldKey)} |
${getStatsFieldTag("scientificname", page.scientificname_count!0, page.availableFilters.scientificname?c, page.statsFieldKey)}

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