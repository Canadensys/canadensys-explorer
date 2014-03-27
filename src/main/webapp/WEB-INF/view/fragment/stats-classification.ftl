<h2>${rc.getMessage("view.stats.group.classification")}</h2>

${getStatsFieldTag("kingdom", root.kingdom_count!0, root.availableFilters.kingdom?c, root.statsFieldKey)} |
${getStatsFieldTag("phylum", root.phylum_count!0, root.availableFilters.phylum?c, root.statsFieldKey)} |
${getStatsFieldTag("_class", root._class_count!0, root.availableFilters._class?c, root.statsFieldKey)} |
${getStatsFieldTag("_order", root._order_count!0, root.availableFilters._order?c, root.statsFieldKey)} |
${getStatsFieldTag("family", root.family_count!0, root.availableFilters.family?c, root.statsFieldKey)} |
${getStatsFieldTag("genus", root.genus_count!0, root.availableFilters.genus?c, root.statsFieldKey)} |
${getStatsFieldTag("scientificname", root.scientificname_count!0, root.availableFilters.scientificname?c, root.statsFieldKey)}

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