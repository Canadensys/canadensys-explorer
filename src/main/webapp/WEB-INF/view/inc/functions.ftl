<#-- Copyright 2011 Canadensys -->

<#-- Language text function. This function simply makes the syntax easier to read. -->
<#function ltext t>
  <#return root.locale[t]>
</#function>

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

<#-- Format the include string for files (js,css) to handle optional minifying and versioning -->
<#-- Produce output like file-1.3.2.min.js -->
 <#function formatFileInclude fileName version useMinified ext>
 	<#if useMinified?has_content && useMinified>
		<#local ext=".min"+ext>
	</#if>
	
 	<#if version?has_content>
		<#local formatted=fileName+"-"+version+ext>
	<#else>
		<#local formatted=fileName+ext>
	</#if>
	<#return formatted>
 </#function>