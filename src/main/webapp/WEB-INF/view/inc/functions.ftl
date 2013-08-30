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