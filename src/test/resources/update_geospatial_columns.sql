UPDATE occurrence SET the_geom = st_geometryfromtext('POINT('||decimallongitude||' '|| decimallatitude ||')',4326) WHERE decimallatitude IS NOT NULL AND decimallongitude IS NOT NULL;
UPDATE occurrence SET the_geom_webmercator = st_transform_null(the_geom,3857) WHERE the_geom IS NOT NULL;
