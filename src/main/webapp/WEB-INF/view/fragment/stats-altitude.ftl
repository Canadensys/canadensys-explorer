<h2>${rc.getMessage("view.stats.group.altitude")}</h2>
<div class="stats_group chart_histogram">
  <div class="chart_container" id="chart_bar"></div>
  <table class="stats_container" id="chart_table">
  <thead>
  	<tr>
     <th scope="col">${rc.getMessage("view.stats."+root.statsFieldKey)}</th>
     <th scope="col">${rc.getMessage("view.stats.count")}</th>
  	</tr>
  </thead>
  <tbody>
  <#list root.statsData?keys as currKey>
  	<#if (root.statsData[currKey] > 0)>
  	  <tr>
  	    <td>${currKey}</td>
  	    <td>${root.statsData[currKey]}</td>
  	  </tr>
  	</#if>
  </#list>
  </tbody> 
  </table>
</div>

<!-- JavaScript init call related to this statistic view -->
<#macro statsJavaScriptInit>
	EXPLORER.chart.loadBarChart('${rc.getMessage("view.stats.chart.altitude.title")}','${root.statsFieldKey}',${root.statsDataJSON});
</#macro>