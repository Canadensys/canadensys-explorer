<#include "inc/functions.ftl">
<#include "inc/paging.ftl">
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

      <#if ((root.occurrenceCount!0) >= root.pageSize)>
        <#assign totalPages=((root.occurrenceCount!0)/root.pageSize)?ceiling/>
      </#if>

      <#if root.allRecordsTargeted >
        <h1>${rc.getMessage("view.header.results.all", [root.occurrenceCount])}</h1>
      <#else>
        <#if ((root.pageNumber!0) > 1)>
          <h1>${rc.getMessage("view.header.results.paging",[root.occurrenceCount, root.pageNumber, totalPages])}</h1>
        <#else>
          <h1>${rc.getMessage("view.header.results",[root.occurrenceCount])}</h1>
        </#if>
      </#if>

      <ul class="buttons">
        <li><a href="<@searchViewUrl view="map"/>">${rc.getMessage("view.map.header.button")}</a></li>
        <li><a href="<@searchViewUrl view="table"/>" class="selected">${rc.getMessage("view.table.header.button")}</a></li>
        <li><a href="<@searchViewUrl view="stats"/>">${rc.getMessage("view.stats.header.button")}</a></li>
      </ul>
    </div>
    <a id="main-content"></a>
    <div id="table_wrapper">
      <table id="results">
      <thead>
        <tr>
          <th class="view_1 sorttable_alpha" scope="col"><a href="?${getTableHeaderSortingUrl("scientificname",root.sortBy!,root.sort!)}">${rc.getMessage("occ.scientificname")}${getTableHeaderSortingClass("scientificname",root.sortBy!,root.sort!)}</a></th>
          <th class="view_2 sorttable_alpha" scope="col"><a href="?${getTableHeaderSortingUrl("family",root.sortBy!,root.sort!)}">${rc.getMessage("occ.family")}${getTableHeaderSortingClass("family",root.sortBy!,root.sort!)}</a></th>
          <th class="view_1 sorttable_alpha" scope="col"><a href="?${getTableHeaderSortingUrl("country",root.sortBy!,root.sort!)}">${rc.getMessage("occ.country")}${getTableHeaderSortingClass("country",root.sortBy!,root.sort!)}</a></th>
          <th class="view_1 sorttable_alpha" scope="col"><a href="?${getTableHeaderSortingUrl("stateprovince",root.sortBy!,root.sort!)}">${rc.getMessage("occ.stateprovince")}${getTableHeaderSortingClass("stateprovince",root.sortBy!,root.sort!)}</a></th>
          <th class="view_3 sorttable_alpha" scope="col"><a href="?${getTableHeaderSortingUrl("locality",root.sortBy!,root.sort!)}">${rc.getMessage("occ.locality")}${getTableHeaderSortingClass("locality",root.sortBy!,root.sort!)}</a></th>
          <th class="view_3 sorttable_alpha" scope="col">${rc.getMessage("occ.habitat")}</th>
          <th class="view_2 sorttable_numeric" scope="col"><a href="?${getTableHeaderSortingUrl("syear",root.sortBy!,root.sort!)}">${rc.getMessage("occ.syear")}${getTableHeaderSortingClass("syear",root.sortBy!,root.sort!)}</a></th>
          <th class="view_1 sorttable_alpha" scope="col"><a href="?${getTableHeaderSortingUrl("collectioncode",root.sortBy!,root.sort!)}">${rc.getMessage("occ.collectioncode")}${getTableHeaderSortingClass("collectioncode",root.sortBy!,root.sort!)}</a></th>
          <th class="view_2 sorttable_numeric" scope="col"><a href="?${getTableHeaderSortingUrl("catalognumber",root.sortBy!,root.sort!)}">${rc.getMessage("occ.catalognumber")}${getTableHeaderSortingClass("catalognumber",root.sortBy!,root.sort!)}</a></th>
          <th class="view_1 sorttable_numeric extra" scope="col">${rc.getMessage("view.table.extra")}</th>
          <th class="view_1 persist last_column" scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <#list root.occurrenceList as currOccurrence>

        <#if currOccurrence.hascoordinates == "true" && currOccurrence.hasmedia == "true">
          <#assign iconsort = 1>
        <#elseif currOccurrence.hascoordinates == "true">
          <#assign iconsort = 2>
        <#elseif currOccurrence.hasmedia == "true">
          <#assign iconsort = 3>
        <#else>
          <#assign iconsort = 4>
        </#if>

        <tr id="${currOccurrence.auto_id}">
          <td>${currOccurrence.scientificname}</td>
          <td>${currOccurrence.family}</td>
          <td>${currOccurrence.country}</td>
          <td>${currOccurrence.stateprovince}</td>
          <td>${currOccurrence.locality}</td>
          <td>${currOccurrence.habitat}</td>
          <td>${currOccurrence.syear}</td>
          <td>${currOccurrence.collectioncode}</td>
          <td>${currOccurrence.catalognumber}</td>
          <td class="icon_column icon_${iconsort}" sorttable_customkey="${iconsort}"></td>
          <td class="last_column"></td>
        </tr>
      </#list>
      </tbody>
      </table>
    </div><#-- table_wrapper -->
  </div>
  <#if ((root.occurrenceCount!0) >= root.pageSize)>
    <@pages 1..totalPages root.pageNumber!1 />
  </#if>
</div><#-- body -->

<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<@jsLibAsset libName="jquery-ui-1.10.4.custom.min.js"/>
<@jsLibAsset libName="underscore-min.js"/>
<@jsLibAsset libName="backbone-min.js"/>
<@jsLibAsset libName="jquery.cookie.js"/>
<@jsLibAsset libName="keynavigator.min.js"/>
<@jsAsset fileName="explorer" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.utils" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.backbone" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.portal" version=root.currentVersion! useMinified=root.useMinified/>
<@jsLibAsset libName="rwd-table.js"/>
<@jsLibAsset libName="respond.js"/>

<script>
$(function() {
 <@controlJavaScriptSettings/>
 EXPLORER.control.restoreDisplay();
 <@controlJavaScriptInit/>
});
</script>
</content>
