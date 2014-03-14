<h2>${rc.getMessage("view.stats.group.classification")}</h2>
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.kingdom?c)?html}">${rc.getMessage("view.stats.kingdom")} (${root.kingdom_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.phylum?c)?html}">${rc.getMessage("view.stats.phylum")} (${root.phylum_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters._class?c)?html}">${rc.getMessage("view.stats.class")} (${root._class_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters._order?c)?html}">${rc.getMessage("view.stats.order")} (${root._order_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.family?c)?html}">${rc.getMessage("view.stats.family")} (${root.family_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.genus?c)?html}">${rc.getMessage("view.stats.genus")} (${root.genus_count!0}) </a>|
<a href="?${URLHelper.replaceCurrentQueryParam(Request,"stat_sel",root.availableFilters.scientificname?c)?html}">${rc.getMessage("view.stats.scientificname")} (${root.scientificname_count!0})</a>

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