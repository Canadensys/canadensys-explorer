<#-- Copyright 2011-2013 Canadensys -->

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

<#-- Safely return the full number or the defaut value if NULL (NULL is defined as an empty string)-->
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

<#macro i18nLanguageSwitch resourceName params=[]>
${URLHelper.getLanguageSwitchPath(Request,rc.getLocale().getLanguage(),resourceName,params)}
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