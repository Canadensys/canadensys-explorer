<!DOCTYPE html>
<html>
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
	${body}
	<#include "include/footer.ftl">
	${page.getProperty("page.local_script")!}
</body>
</html>