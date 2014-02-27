<#include "/WEB-INF/view/inc/global-functions.ftl">
<!DOCTYPE html>
<html lang="${rc.getLocale().getLanguage()}">
<head>
<meta charset="UTF-8">
<title>${title}</title>
${head}
<#include "include/header.ftl">
</head>
<body>
	<div id="skip-link">
		<a href="#main-content" class="skipnav">${rc.getMessage("header.skip")}</a>
	</div>
	<div id="feedback_bar"><a href="http://code.google.com/p/canadensys/issues/entry?template=Explorer%20-%20Interface%20issue" target="_blank" title="${rc.getMessage("feedback.hover")}">&nbsp;</a></div>
	<#include "include/header-div.ftl">
	
	${body}
	<#include "include/footer.ftl">
	${page.getProperty("page.local_script")!}
</body>
</html>