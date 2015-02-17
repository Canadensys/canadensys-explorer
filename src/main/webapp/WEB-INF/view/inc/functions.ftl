<#-- Copyright 2011-2014 Canadensys -->
<#-- Relies on variables: URLHelper,rc,Request  -->

<#function formatdate y m d>
	<#assign formattedDate = "">
	<#if (y > 0)>
		<#assign formattedDate = y?c + "-">
	</#if>
	<#if (m > 0)>
		<#assign formattedDate = formattedDate + m?string?left_pad(2, "0") + "-">
	</#if>
	<#if (d > 0)>
		<#assign formattedDate = formattedDate + d?string?left_pad(2, "0")>
	</#if>
	<#-- remove trailing dash -->
	<#if formattedDate?last_index_of("-") == formattedDate?length-1 && formattedDate?length != 0>
		<#assign formattedDate = formattedDate?substring(0,formattedDate?last_index_of("-"))>
	</#if>
	<#return formattedDate>
</#function>

<#-- Safely return the full number or the default value if NULL (NULL is defined as an empty string)-->
<#-- numvalue must be a number or NULL -->
 <#function safeNumber numvalue defaultvalue>
	<#local value=defaultvalue>
	<#if numvalue?has_content>
	<#local value=numvalue?c>
	</#if>
  <#return value>
</#function>
 
<#function getI18nContextUrl uri>
	<#return rc.getContextUrl(URLHelper.getUriWithLanguage(uri,rc.getLocale().getLanguage()))>
</#function>

<#macro i18nResource resourceName params=[]>
${rc.getContextUrl(URLHelper.toI18nResource(rc.getLocale().getLanguage(),resourceName,params))}
</#macro>

<#-- page.searchParameters should exists-->
<#macro searchViewUrl view>
${URLHelper.newQueryStringBuilder().add("view",view).add(page.searchParameters!).toQueryString()}
</#macro>

<#function isImageMimeType url>
	<#return URLHelper.getMimeFileType(url)?contains("image")>
</#function>

<#function getTableHeaderSortingUrl sortBy currentSortBy currentSort>
	<#local sortValue="asc">
	<#-- check if the sort is already on this 'sortBy' -->
	<#if currentSortBy?has_content && (currentSortBy == sortBy)>
		<#if (!(currentSort?has_content)) || (currentSort?lower_case == "asc")>
			<#local sortValue="desc">
		</#if>
	</#if>
	<#return URLHelper.replaceCurrentQueryParams(Request, "sortby",sortBy,"sort",sortValue)>
</#function>

<#function getTableHeaderSortingClass sortBy currentSortBy currentSort>
  <#local sortSpan="">
  <#if currentSortBy?has_content && (currentSortBy == sortBy)>
    <#if currentSort?lower_case == "asc">
      <#local sortSpan = "<span>&nbsp;&#x25B4;</span>">
    <#elseif currentSort?lower_case == "desc">
      <#local sortSpan = "<span>&nbsp;&#x25BE;</span>">
    </#if>
  </#if>
  <#return sortSpan>
</#function>

<#function getStatsViewButtonClass stat currentStat>
  <#local selected="">
  <#if currentStat?has_content && (currentStat?lower_case == stat)>
    <#local selected="class=\"selected\"">
  </#if>
  <#return selected>
</#function>

<#function getStatsFieldTag statFieldName statFieldCount statFieldId currentStatField>
  <#local tag = rc.getMessage("view.stats." + statFieldName) + " (" + statFieldCount + ")">
  <#if currentStatField?lower_case != statFieldName>
     <#local tag = "<a href=\"?" + URLHelper.replaceCurrentQueryParam(Request,"stat_sel",statFieldId)?html + "\">" + tag + "</a>">
  </#if>
  <#return tag>
</#function>

<#-- Print license div block-->
<#macro licenseDiv license licenseShortname="" creator="">
<div class="attribution">
<#if licenseShortname?has_content>
  <a href="${license}" rel="license">
  <img src="${rc.getContextUrl("/assets/images/"+licenseShortname+".png")}" alt="CC ${licenseShortname?upper_case}" />
  </a><br>
<#else>
  <@printIfNotEmpty text=rc.getMessage("occ.multimedia.license")+": " variable=license/><br>
</#if>
<#if creator?has_content>
  ${rc.getMessage("occ.multimedia.creator")}: <span property="author">${creator}</span>
</#if>
</div>
</#macro>