CREATE DATABASE db;
use db;
CREATE table num(
    data int
);
insert INTO num VALUES(8),(8),(3),(3),(1),(4),(5),(6);

with temp  as (select data,COUNT(data) as cnt from num GROUP BY data)

SELECT case when count(*) >0 then max(data)
        else null
        end as result
 from  temp where cnt=1;
