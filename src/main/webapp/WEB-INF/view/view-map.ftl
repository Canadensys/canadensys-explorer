<#include "inc/functions.ftl">
<#include "inc/global-functions.ftl">
<head>
<title>${rc.getMessage("page.search.title")}</title>
<@cssAsset fileName="occportal" version=root.currentVersion! useMinified=false/>
<link rel="dns-prefetch" href="${tilerProtocol}://${tilerDomain}"/>
<link rel="prefetch" href="${tilerProtocol}://${tilerDomain}"/>
</head>
<content tag="lang_switch">
<@i18nLanguageSwitch resourceName="search"/>
</content>
<div id="body" class="fullscreen full_height">
  <#include "inc/control.ftl">
  <div id="view" class="clear_fix">
    <div class="nav_container">

    <#if root.allRecordsTargeted >
      <h1>${rc.getMessage("view.header.results.all", [root.occurrenceCount])}</h1>
    <#else>
      <h1>${rc.getMessage("view.header.results",[root.occurrenceCount])}</h1>
    </#if>
      
      <p class="details">(${rc.getMessage("view.map.header.details")}: ${root.georeferencedOccurrenceCount})</p>
      <ul class="buttons">
        <li><a href="<@searchViewUrl view="map"/>" class="selected">${rc.getMessage("view.map.header.button")}</a></li>
        <li><a href="<@searchViewUrl view="table"/>">${rc.getMessage("view.table.header.button")}</a></li>
        <li><a href="<@searchViewUrl view="stats"/>">${rc.getMessage("view.stats.header.button")}</a></li>
      </ul>
    </div>
    <a id="main-content"></a>
    <div id="map_canvas">
    </div>
  </div>
</div><#-- body -->

<content tag="local_script">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="//maps.googleapis.com/maps/api/js?sensor=false&amp;libraries=drawing"></script>
<@jsLibAsset libName="jquery-ui-1.10.4.custom.min.js"/>
<@jsLibAsset libName="underscore-min.js"/>
<@jsLibAsset libName="backbone-min.js"/>
<@jsLibAsset libName="wax.g.min-6.2.0-touched.js"/>
<@jsLibAsset libName="cartodb-gmapsv3-min.js"/>
<@jsAsset fileName="explorer" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.utils" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.portal" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.backbone" version=root.currentVersion! useMinified=root.useMinified/>
<@jsAsset fileName="explorer.map" version=root.currentVersion! useMinified=root.useMinified/>

<script>
$(function() {
  EXPLORER.map.setupMap({
    mapCanvasId : "map_canvas",
    tilerProtocol : "${tilerProtocol}",
    tilerDomain : "${tilerDomain}",
    tilerPort : ${tilerPort},
    mapQuery : "${root.embeddedMapQuery}"
  });
  //EXPLORER.map.createDrawingOverlay({type:"geoellipse", data : { coords : [57.51582286553882,-113.02734375], radius : 720694.4406783357 }});
 // EXPLORER.map.createDrawingOverlay({type:"georectangle", data : { coords : [[47.754097979680026,-122.16796875], [55.578344672182055,-105.29296875]] }});
  //EXPLORER.map.createDrawingOverlay({type:"geopolygon", data : { coords : [[64.19681461100494, -108.984375], [54.610254981579146,-123.75], [41.02964338716638,-120.5859375], [41.02964338716638,-98.0859375], [48.73445537176822,-82.96875], [56.98091142454479,-82.96875], [49.196064000723794,-106.171875], [60.27251459483243,-90.3515625], [60.27251459483243,-105.1171875], [64.19681461100494,-108.984375]] }});
<@controlJavaScriptInit/>
});
</script>
</content>
