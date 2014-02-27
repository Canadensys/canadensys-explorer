<#ftl strip_whitespace=true>
<#include "../inc/functions.ftl">
<#include "../inc/global-functions.ftl">
<head>
<title>${rc.getMessage("viewinclude.http404.title")}</title>
<@cssAsset fileName="occportal" version=root.currentVersion! useMinified=false/>
</head>
<content tag="lang_switch">
<@i18nLanguageSwitch resourceName="search"/>
</content>
<div id="body">
	<div id="content" class="no_side_bar">
		<h1>${rc.getMessage("viewinclude.http404.title")}</h1>
		<p>${rc.getMessage("viewinclude.http404.message")}</p>
	</div>
</div>
