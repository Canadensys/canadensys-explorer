
ALTER TABLE occurrence ADD COLUMN the_geom geometry;
ALTER TABLE occurrence ADD COLUMN the_geom_webmercator geometry;

ALTER TABLE occurrence
  ADD CONSTRAINT enforce_dims_the_geom CHECK (st_ndims(the_geom) = 2);
ALTER TABLE occurrence
  ADD CONSTRAINT enforce_dims_the_geom_webmercator CHECK (st_ndims(the_geom_webmercator) = 2);
ALTER TABLE occurrence
  ADD CONSTRAINT enforce_geotype_the_geom CHECK (geometrytype(the_geom) = 'POINT'::text OR the_geom IS NULL);
ALTER TABLE occurrence
  ADD CONSTRAINT enforce_geotype_the_geom_webmercator CHECK (geometrytype(the_geom_webmercator) = 'POINT'::text OR the_geom_webmercator IS NULL);
ALTER TABLE occurrence
  ADD CONSTRAINT enforce_srid_the_geom CHECK (st_srid(the_geom) = 4326);
ALTER TABLE occurrence
  ADD CONSTRAINT enforce_srid_the_geom_webmercator CHECK (st_srid(the_geom_webmercator) = 3857);
  
  CREATE INDEX occurrence_the_geom_idx ON occurrence USING gist (the_geom );
  CREATE INDEX occurrence_the_geom_webmercator_idx ON occurrence USING gist (the_geom_webmercator );