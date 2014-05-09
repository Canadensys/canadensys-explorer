<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title}</title>
${head}
</head>
<body>
	<div id="skip-link">
		<a href="#main-content" class="skipnav">${rc.getMessage("header.skip")}</a>
	</div>
	
	${body}
	${page.getProperty("page.local_script")!}
</body>
</html>