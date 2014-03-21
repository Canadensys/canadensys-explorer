<#ftl strip_whitespace=true>
<#include "../inc/functions.ftl">
<#include "../inc/global-functions.ftl">
<head>
<title>${rc.getMessage("cwt.error.title")}</title>
<@cssAsset fileName="occportal" version=(root.currentVersion)! useMinified=false/>
</head>
<content tag="lang_switch">
<@i18nLanguageSwitch resourceName="search"/>
</content>
<div id="body">
	<div id="content" class="no_side_bar">
		<h1>${rc.getMessage("cwt.error.title")}</h1>
		<p>${rc.getMessage("cwt.error.message")} <a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue\" target="_blank">${rc.getMessage("cwt.error.message.linkpart")}.</a></p>
	</div>
</div>
