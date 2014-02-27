<link rel="stylesheet" href="http://data.canadensys.net/common/styles/common-1.0.min.css" media="screen,print"/>
<link rel="shortcut icon" href="http://data.canadensys.net/common/images/favicon.png"/>

<#if gaAccount?? && gaAccount?has_content>
	<meta name="google-site-verification" content="${gaSiteVerification}"/>
  <script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', '${gaAccount}', 'auto');
  ga('send', 'pageview');

  </script>
</#if>