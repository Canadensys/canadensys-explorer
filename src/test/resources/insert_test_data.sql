INSERT INTO occurrence(
auto_id,datasetname,sourcefileid,dwcaid,associatedmedia,scientificname,taxonrank,country,stateprovince)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-1','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg','Acer pseudoplatanus','species','Canada','Nova Scotia'
);

INSERT INTO occurrence_raw(
auto_id,datasetname,sourcefileid,dwcaid,associatedmedia,scientificname,taxonrank,country,stateprovince)
VALUES (
1,'E. C. Smith Herbarium (ACAD)','acad-specimens',
'ACAD-1','http://procyon.acadiau.ca/ecsmith/cgi-bin/image.cgi?ECS019597,jpeg','Acer pseudoplatanus','species','Canada','Nova Scotia'
);


INSERT INTO resource_contact(
            id, sourcefileid, resource_name, name, position_name, organization_name, 
            address, city, administrative_area, country, postal_code, 
            email)
VALUES (1, 'acad-specimens', 'E. C. Smith Herbarium (ACAD)', 'Greedo', 'Manager', 'Sith', '1212 Death Star Road', 'Jawas County', 'Dunes', 'Tatooine', 'R1-D2', 'greedo@tatooine.org');