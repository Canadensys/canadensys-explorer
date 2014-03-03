INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,associatedmedia,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,minimumelevationinmeters,maximumelevationinmeters,syear,habitat)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-1','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg; http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597.jpeg',
'Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597',100,200,1980,'Under rocks'
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,associatedmedia,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-1','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg; http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597.jpeg',
'Acer pseudoplatanus','species','Canada','Nova Scotia','ACAD','ECS019597',1980,'Under rocks'
);

INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,syear,habitat)
VALUES (
2,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-2','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','10440',2013,'On a lobster claw'
);
INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,scientificname,taxonrank,country,stateprovince,collectioncode,catalognumber,year,habitat)
VALUES (
2,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-2','Myosotis arvensis','species','Canada','Nova Scotia','ACAD','10440',2013, 'On a lobster claw'
);


INSERT INTO resource_contact(
            id, sourcefileid, resource_name, name, position_name, organization_name, 
            address, city, administrative_area, country, postal_code, 
            email)
VALUES (1, 'acad-specimens', 'E. C. Smith Herbarium (ACAD)', 'Greedo', 'Manager', 'Sith', '1212 Death Star Road', 'Jawas County', 'Dunes', 'Tatooine', 'R1-D2', 'greedo@tatooine.org');