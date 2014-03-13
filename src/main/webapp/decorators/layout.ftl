<#-- some /WEB-INF/view/inc/.ftl can come from canadensys-web-core project -->
<#include "/WEB-INF/view/inc/global-functions.ftl">
<!DOCTYPE html>
<html lang="${rc.getLocale().getLanguage()}">
<head>
<meta charset="UTF-8">
<title><sitemesh:write property='title'>Title goes here</sitemesh:write></title>
<link rel="stylesheet" href="http://data.canadensys.net/common/styles/common-1.0.min.css" media="screen,print"/>
<link rel="shortcut icon" href="http://data.canadensys.net/common/images/favicon.png"/>
<sitemesh:write property='head'/>
<#include "/WEB-INF/view/inc/ga.ftl">
</head>
<body>
	<div id="skip-link">
		<a href="#main-content" class="skipnav">${rc.getMessage("header.skip")}</a>
	</div>
	<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${rc.getMessage("feedback.hover")}">&nbsp;</a></div>
	<#include "include/header-div.ftl">
	
	<sitemesh:write property='body'/>
	<#include "include/footer.ftl">
	<sitemesh:write property='page.local_script'/>
</body>
</html>