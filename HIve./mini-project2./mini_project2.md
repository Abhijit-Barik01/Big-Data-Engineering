Download the dataset from the mentioned links:
```
https://data.cityofnewyork.us/browse?q=parking+tickets
```
```
create table parking_violations
(
Summons_Number bigint,
Plate_ID string,
Registration_State string,
Plate_Type string,
Issue_Date string,
Violation_Code int,
Vehicle_Body_Type string,
Vehicle_Make string,
Issuing_Agency string,
Street_Code1 int,
Street_Code2 int,
Street_Code3 int,
Vehicle_Expiration string,
Violation_Location int,
Violation_Precinct int,
Issuer_Precinct int,
Issuer_Code int,
Issuer_Command string,
Issuer_Squad string,
Violation_Time string,
Time_First_Observed string,
Violation_County string,
Violation_In_Front_Of_Or_Opposite string,
House_Number string,
Street_Name string,
Intersecting_Street string,
Date_First_Observed int,
Law_Section int,
Sub_Division string,
Violation_Legal_Code string,
Days_Parking_In_Effect string,
From_Hours_In_Effect string,
To_Hours_In_Effect string,
Vehicle_Color string,
Unregistered_Vehicle int,
Vehicle_Year string,
Meter_Number string,
Feet_From_Curb int,
Violation_Post_Code string,
Violation_Description string,
No_Standing_or_Stopping_Violation string,
Hydrant_Violation string,
Double_Parking_Violation string)
row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count" = "1");
```
```
load data local  inpath  file'///tmp/data/Parking_violation2017.csv' into table parking_violations;
```
as the above table uses string datatype for dates, so in order to correct that a new table is created with the required datatype...
```
create table violations_parking
(
Summons_Number bigint,
Plate_ID string,
Registration_State string,
Plate_Type string,
Issue_Date date,
Violation_Code int,
Vehicle_Body_Type string,
Vehicle_Make string,
Issuing_Agency string,
Street_Code1 int,
Street_Code2 int,
Street_Code3 int,
Vehicle_Expiration date,
Violation_Location int,
Violation_Precinct int,
Issuer_Precinct int,
Issuer_Code int,
Issuer_Command string,
Issuer_Squad string,
Violation_Time string,
Time_First_Observed string,
Violation_County string,
Violation_In_Front_Of_Or_Opposite string,
House_Number string,
Street_Name string,
Intersecting_Street string,
Date_First_Observed int,
Law_Section int,
Sub_Division string,
Violation_Legal_Code string,
Days_Parking_In_Effect string,
From_Hours_In_Effect string,
To_Hours_In_Effect string,
Vehicle_Color string,
Unregistered_Vehicle int,
Vehicle_Year string,
Meter_Number string,
Feet_From_Curb int,
Violation_Post_Code string,
Violation_Description string,
No_Standing_or_Stopping_Violation string,
Hydrant_Violation string,
Double_Parking_Violation string)
row format delimited
fields terminated by ',';
```
Insert the data from the parking_violations table into violations_parking table
 ```
 insert overwrite table violations_parking select
 Summons_Number bigint,
 Plate_ID string,
 Registration_State string,
 Plate_Type string,
 from_unixtime(unix_timestamp(Issue_Date,'MM/dd/YYYY'),'yyyy-MM-dd'),
 Violation_Code int,
 Vehicle_Body_Type string,
 Vehicle_Make string,
 Issuing_Agency string,
 Street_Code1 int,
 Street_Code2 int,
 Street_Code3 int,
 from_unixtime(unix_timestamp(Vehicle_Expiration,'YYYYMMdd'),'yyyy-MM-dd'),
 Violation_Location int,
 Violation_Precinct int,
 Issuer_Precinct int,
 Issuer_Code int,
 Issuer_Command string,
 Issuer_Squad string,
 Violation_Time string,
 Time_First_Observed string,
 Violation_County string,
 Violation_In_Front_Of_Or_Opposite string,
 House_Number string,
 Street_Name string,
 Intersecting_Street string,
 Date_First_Observed int,
 Law_Section int,
 Sub_Division string,
 Violation_Legal_Code string,
 Days_Parking_In_Effect string,
 From_Hours_In_Effect string,
 To_Hours_In_Effect string,
 Vehicle_Color string,
 Unregistered_Vehicle int,
 Vehicle_Year string,
 Meter_Number string,
 Feet_From_Curb int,
 Violation_Post_Code string,
 Violation_Description string,
 No_Standing_or_Stopping_Violation string,
 Hydrant_Violation string,
 Double_Parking_Violation string
 from parking_violations;
 ```
 Now create a partition and bucketing of a table as this improves the execution speed of queries as the data size is big...
``` 
create table park_viol_part_buck
(
Summons_Number bigint,
Plate_ID string,
Registration_State string,
Plate_Type string,
Issue_Date date,
Violation_Code int,
Vehicle_Body_Type string,
Vehicle_Make string,
Issuing_Agency string,
Street_Code1 int,
Street_Code2 int,
Street_Code3 int,
Vehicle_Expiration date,
Violation_Location int,
Violation_Precinct int,
Issuer_Precinct int,
Issuer_Code int,
Issuer_Command string,
Issuer_Squad string,
Violation_Time string,
Time_First_Observed string,
Violation_In_Front_Of_Or_Opposite string,
House_Number string,
Street_Name string,
Intersecting_Street string,
Date_First_Observed int,
Law_Section int,
Sub_Division string,
Violation_Legal_Code string,
Days_Parking_In_Effect string,
From_Hours_In_Effect string,
To_Hours_In_Effect string,
Vehicle_Color string,
Unregistered_Vehicle int,
Vehicle_Year string,
Meter_Number string,
Feet_From_Curb int,
Violation_Post_Code string,
Violation_Description string,
No_Standing_or_Stopping_Violation string,
Hydrant_Violation string,
Double_Parking_Violation string)
partitioned by (Violation_County string)
clustered by (Violation_Code)
sorted by(Violation_Code) into 8 buckets
row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count" = "1");
```
Before loading the data into the above table some properties are needed to be set...
```
set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict; 
set hive.enforce.bucketing = true;
```
Now insert the data into the parted and bucketed table from the violations parking table
```
insert into park_viol_part_buck partition(Violation_County) select
Summons_Number,Plate_ID,Registration_State,Plate_Type,Issue_Date,Violation_Code,
Vehicle_Body_Type,Vehicle_Make,Issuing_Agency,Street_Code1,Street_Code2,
Street_Code3,Vehicle_Expiration,Violation_Location,Violation_Precinct,
Issuer_Precinct,Issuer_Code,Issuer_Command,Issuer_Squad,Violation_Time,
Time_First_Observed,Violation_In_Front_Of_Or_Opposite,House_Number,Street_Name,
Intersecting_Street,Date_First_Observed,Law_Section,Sub_Division,Violation_Legal_Code,
Days_Parking_In_Effect,From_Hours_In_Effect,To_Hours_In_Effect,Vehicle_Color,
Unregistered_Vehicle,Vehicle_Year,Meter_Number,Feet_From_Curb,Violation_Post_Code,
Violation_Description,No_Standing_or_Stopping_Violation,Hydrant_Violation,
Double_Parking_Violation,Violation_County from violations_parking
where year(Issue_Date) = '2017';
```
## Part-I: Examine the data

### 1. Find the total number of tickets for the year.
```
select count(distinct Summons_Number) Tickets_Total ,year(Issue_Date) as year from park_viol_part_buck group by year(Issue_Date);
```
5432898  &nbsp;&nbsp;&nbsp; 2017

### 2. Find out how many unique states the cars which got parking tickets came from.
```
select count(distinct Registration_State) as No_of_States from park_viol_part_buck;
select Registration_State, Count(1) as Number_of_Records from park_viol_part_buck  group by Registration_State order by Number_of_Records;
```
65

### 3. Some parking tickets don’t have addresses on them, which is cause for concern. Find out how many such tickets there are(i.e. tickets where either "Street Code 1" or "Street Code 2" or "Street Code 3" is empty )
```
select count(distinct summons_number) as No_Tickets_without_address from violations_parking  where Street_code1 = 0 or Street_code2 = 0 or Street_code3 = 0;
```
3667515

## Part-II: Aggregation tasks

### 1.How often does each violation code occur? (frequency of violation codes - find the top 5)
```
select count(Violation_Code) as frequency_of_violation,Violation_Code from park_viol_part_buck group by Violation_Code order by frequency_of_violation desc limit 5;
```
768276 &nbsp;&nbsp;&nbsp; 21<br>
662760 &nbsp;&nbsp;&nbsp; 36<br>
542088 &nbsp;&nbsp;&nbsp; 38<br>
476756 &nbsp;&nbsp;&nbsp; 14<br>
319720 &nbsp;&nbsp;&nbsp; 20<br>

### 2. How often does each vehicle body type get a parking ticket? How about the vehicle make? (find the top 5 for both)
```
select Vehicle_Body_Type,count(summons_number)as frequency_of_getting_parking_ticket  from park_viol_part_buck group by Vehicle_Body_Type order by frequency_of_getting_parking_ticket desc limit 5;
```
SUBN  &nbsp;&nbsp;&nbsp;  1884255<br>
4DSD  &nbsp;&nbsp;&nbsp;  1547293<br>
VAN   &nbsp;&nbsp;&nbsp;  724142<br>
DELV  &nbsp;&nbsp;&nbsp;  359069<br>
SDN   &nbsp;&nbsp;&nbsp;  194597<br>
```
select Vehicle_Make,count(summons_number)as frequency_of_getting_parking_ticket from park_viol_part_buck group by Vehicle_Make order by frequency_of_getting_parking_ticket desc limit 5; 
```
FORD  &nbsp;&nbsp;&nbsp;  636948<br>
TOYOT  &nbsp;&nbsp;&nbsp; 605395<br>
HONDA  &nbsp;&nbsp;&nbsp; 538987<br>
NISSA  &nbsp;&nbsp;&nbsp; 462108<br>
CHEVR  &nbsp;&nbsp;&nbsp; 356095<br>

### 3. A precinct is a police station that has a certain zone of the city under its command. Find the (5 highest) frequencies of:

#### a. Violating Precincts (this is the precinct of the zone where the violation occurred)
```
select Violation_Precinct,count(*) as IssuedTicket from violations_parking group by  Violation_Precinct order by IssuedTicket desc limit 5;
```
0    &nbsp;&nbsp;&nbsp;   2072400<br>
19   &nbsp;&nbsp;&nbsp;   535671<br>
14   &nbsp;&nbsp;&nbsp;   352450<br>
1    &nbsp;&nbsp;&nbsp;   331810<br>
18   &nbsp;&nbsp;&nbsp;   306920<br>

#### b. Issuer Precincts (this is the precinct that issued the ticket)
```
select Issuer_Precinct,count(*) as IssuedTicket from violations_parking group by Issuer_Precinct order by IssuedTicket desc limit 5;
```
0    &nbsp;&nbsp;&nbsp;   2388475<br>
19   &nbsp;&nbsp;&nbsp;   521513<br>
14   &nbsp;&nbsp;&nbsp;   344977<br>
1    &nbsp;&nbsp;&nbsp;   321170<br>
18   &nbsp;&nbsp;&nbsp;   296554<br>

### 4. Find the violation code frequency across 3 precincts which have issued the most number of tickets - do these precinct zones have an exceptionally high frequency of certain violation codes?

```
select Issuer_Precinct,Violation_Code, count(*) as TicketsIssued from park_viol_part_buck  group by Issuer_Precinct, Violation_Code order by TicketsIssued desc limit 7;
```
0    &nbsp;&nbsp;&nbsp;   36   &nbsp;&nbsp;&nbsp;   662760<br>
0    &nbsp;&nbsp;&nbsp;   7    &nbsp;&nbsp;&nbsp;   210171<br>
0    &nbsp;&nbsp;&nbsp;   21   &nbsp;&nbsp;&nbsp;   126218<br>
18   &nbsp;&nbsp;&nbsp;   14   &nbsp;&nbsp;&nbsp;   50159<br>
19   &nbsp;&nbsp;&nbsp;   46   &nbsp;&nbsp;&nbsp;   48451<br>
0    &nbsp;&nbsp;&nbsp;   5    &nbsp;&nbsp;&nbsp;   48072<br>
14   &nbsp;&nbsp;&nbsp;   14   &nbsp;&nbsp;&nbsp;   45041<br>

It will not make any sense to consider '0', therefore 18,19 and 14 are the three issuer precincts which have the maximum number of violations. 

##### Issuer Precinct 18:

```
select Violation_Code, count(*) as TicketsIssued from park_viol_part_buck where Issuer_Precinct=18 group by Violation_Code order by TicketsIssued desc limit 7;
```
14   &nbsp;&nbsp;&nbsp;   50159<br>
69   &nbsp;&nbsp;&nbsp;   20189<br>
47   &nbsp;&nbsp;&nbsp;   14107<br>
31   &nbsp;&nbsp;&nbsp;   11894<br>
46   &nbsp;&nbsp;&nbsp;   7872<br>
42   &nbsp;&nbsp;&nbsp;   6190<br>
38   &nbsp;&nbsp;&nbsp;   6176<br>

##### Issuer Precinct 19:

```
select Violation_Code, count(*) as TicketsIssued from park_viol_part_buck where Issuer_Precinct=19 group by Violation_Code order by TicketsIssued desc limit 7;
```
46   &nbsp;&nbsp;&nbsp;   48451<br>
38   &nbsp;&nbsp;&nbsp;   36386<br>
37   &nbsp;&nbsp;&nbsp;   36056<br>
14   &nbsp;&nbsp;&nbsp;   29797<br>
21   &nbsp;&nbsp;&nbsp;   28413<br>
20   &nbsp;&nbsp;&nbsp;   14629<br>
40   &nbsp;&nbsp;&nbsp;   11416<br>

##### Issuer Precinct 14:

```
select Violation_Code, count(*) as TicketsIssued from park_viol_part_buck where Issuer_Precinct=14 group by Violation_Code order by TicketsIssued desc limit 7;
```
14   &nbsp;&nbsp;&nbsp;   45041<br>
69   &nbsp;&nbsp;&nbsp;   30464<br>
31   &nbsp;&nbsp;&nbsp;   22555<br>
47   &nbsp;&nbsp;&nbsp;   18364<br>
42   &nbsp;&nbsp;&nbsp;   10027<br>
46   &nbsp;&nbsp;&nbsp;   7686<br>
19   &nbsp;&nbsp;&nbsp;   7030<br>

### 5. Find out the properties of parking violations across different times of the day: The Violation Time field is specified in a strange format. Find a way to make this into a time attribute that you can use to divide into groups.

```
select from_unixtime(unix_timestamp(regexp_extract(violation_time,'(.*)[A-Z]',1),'HHmm'),"HH:mm") as data from violations_parking limit 7;
```
01:43<br>
04:00<br>
12:11<br>
12:17<br>
12:07<br>
10:37<br>
01:01<br>

```
select from_unixtime(unix_timestamp(concat(violation_time,'M'), 'HHmmaaa'),"HH:mmaaa") as data from violations_parking limit 7;
```
01:43AM<br>
04:00AM<br>
12:11PM<br>
12:17PM<br>
12:07PM<br>
10:37AM<br>
01:01AM<br>

### 6. Divide 24 hours into 6 equal discrete bins of time. The intervals you choose are at your discretion. For each of these groups, find the 3 most commonly occurring violations

Create a Partitioned View as views take less space as compared to tables:
```
create view park_viol_part_view partitioned on (Violation_Code) as
select Summons_Number, Violation_Time, Issuer_Precinct,
case
when substring(Violation_Time,1,2) in ('00','01','02','03','12') and upper(substring(Violation_Time,-1))='A' then 1
when substring(Violation_Time,1,2) in ('04','05','06','07') and upper(substring(Violation_Time,-1))='A' then 2
when substring(Violation_Time,1,2) in ('08','09','10','11') and upper(substring(Violation_Time,-1))='A' then 3
when substring(Violation_Time,1,2) in ('12','00','01','02','03') and upper(substring(Violation_Time,-1))='P' then 4
when substring(Violation_Time,1,2) in ('04','05','06','07') and upper(substring(Violation_Time,-1))='P' then 5
when substring(Violation_Time,1,2) in ('08','09','10','11') and upper(substring(Violation_Time,-1))='P'then 6
else null end as Violation_Time_bin,Violation_Code
from park_viol_part_buck
where Violation_Time is not null or (length(Violation_Time)=5 and upper(substring(Violation_Time,-1))in ('A','P')
and substring(Violation_Time,1,2) in ('00','01','02','03','04','05','06','07', '08','09','10','11','12'));
```
#### Bin = 1
```
select Violation_Code,count(*) TicketsIssued from park_viol_part_view where Violation_Time_bin == 1 group by Violation_Code order by TicketsIssued desc limit 3;
```
21   &nbsp;&nbsp;&nbsp;   36976<br>
40   &nbsp;&nbsp;&nbsp;   25897<br>
78   &nbsp;&nbsp;&nbsp;   15550<br>

#### Bin = 2
```
select Violation_Code,count(*) TicketsIssued from park_viol_part_view where Violation_Time_bin == 2 group by Violation_Code order by TicketsIssued desc limit 3;
```
14   &nbsp;&nbsp;&nbsp;   74121<br>
40   &nbsp;&nbsp;&nbsp;   60662<br>
21   &nbsp;&nbsp;&nbsp;   57928<br>

#### Bin = 3
```
select Violation_Code,count(*) TicketsIssued from park_viol_part_view where Violation_Time_bin == 3 group by Violation_Code order by TicketsIssued desc limit 3;
```
21   &nbsp;&nbsp;&nbsp;   598183<br>
36   &nbsp;&nbsp;&nbsp;   348161<br>
38   &nbsp;&nbsp;&nbsp;   176573<br>

#### Bin = 4
```
select Violation_Code,count(*) TicketsIssued from park_viol_part_view where Violation_Time_bin == 4 group by Violation_Code order by TicketsIssued desc limit 3;
```
36   &nbsp;&nbsp;&nbsp;   286283<br>
38   &nbsp;&nbsp;&nbsp;   240721<br>
37   &nbsp;&nbsp;&nbsp;   167026<br>

#### Bin = 5
```
select Violation_Code,count(*) TicketsIssued from park_viol_part_view where Violation_Time_bin == 5 group by Violation_Code order by TicketsIssued desc limit 3;
```
38   &nbsp;&nbsp;&nbsp;   102858<br>
14   &nbsp;&nbsp;&nbsp;   75919<br>
37   &nbsp;&nbsp;&nbsp;   70345<br>

#### Bin = 6
```
select Violation_Code,count(*) TicketsIssued from park_viol_part_view where Violation_Time_bin == 6 group by Violation_Code order by TicketsIssued desc limit 3;
```
7   &nbsp;&nbsp;&nbsp;    26292<br>
40   &nbsp;&nbsp;&nbsp;   22361<br>
14   &nbsp;&nbsp;&nbsp;   21055<br>

### 7. Now, try another direction. For the 3 most commonly occurring violation codes, find the most common times of day (in terms of the bins from the previous part)
```
select Violation_Time_bin, count(*) TicketsIssued from park_viol_part_view where Violation_Code in (21,36,37,38) group by Violation_Time_bin order by TicketsIssued desc limit 3;
```
3    &nbsp;&nbsp;&nbsp;   1173323<br>
4    &nbsp;&nbsp;&nbsp;   768743<br>
5    &nbsp;&nbsp;&nbsp;   186997<br>

### 8. Let’s try and find some seasonality in this data
#### a. First, divide the year into some number of seasons, and find frequencies of tickets for each season. (Hint: A quick Google search reveals the following seasons in NYC: Spring(March, April, March); Summer(June, July, August); Fall(September, October, November); Winter(December, January, February))
```
create view tickets_issued_view as
select Violation_Code , Issuer_Precinct,
case
when MONTH(Issue_Date) between 03 and 05 then 'spring'
when MONTH(Issue_Date) between 06 and 08 then 'summer'
when MONTH(Issue_Date) between 09 and 11 then 'autumn'
when MONTH(Issue_Date) in (1,2,12) then 'winter'
else 'unknown' end  as season 
from violations_parking;
```	
Create a partitioned view of the above view, again to increase the query execution performance...
```	
create view tickets_issued_view_part partitioned on (Violation_Code) as
select Issuer_Precinct,
case
when MONTH(Issue_Date) between 03 and 05 then 'spring'
when MONTH(Issue_Date) between 06 and 08 then 'summer'
when MONTH(Issue_Date) between 09 and 11 then 'autumn'
when MONTH(Issue_Date) in (1,2,12) then 'winter'
else 'unknown' end  as season,Violation_Code from
violations_parking;
```	
```	
select season, count(*) as TicketsIssued from tickets_issued_view_part group by season order by TicketsIssued desc;
```
Spring &nbsp;&nbsp;&nbsp;	2958420<br>
Winter &nbsp;&nbsp;&nbsp;	1562390<br>
Summer &nbsp;&nbsp;&nbsp;	855690<br>
autumn &nbsp;&nbsp;&nbsp;	0<br>


#### b. Then, find the 3 most common violations for each of these seasons.
```
select Violation_Code, count(*) as TicketsIssued from tickets_issued_view_part where season = 'spring' group by Violation_Code order by TicketsIssued desc limit 3;
```
21 &nbsp;&nbsp;&nbsp;	401250<br>
36 &nbsp;&nbsp;&nbsp;	365231<br>
38 &nbsp;&nbsp;&nbsp;	265859<br>

```
select Violation_Code, count(*) as TicketsIssued from tickets_issued_view_part where season = 'summer' group by Violation_Code order by TicketsIssued desc limit 3;
```
21 &nbsp;&nbsp;&nbsp;	234562<br>
36 &nbsp;&nbsp;&nbsp;	222369<br>
38 &nbsp;&nbsp;&nbsp;	181258<br>

```
select Violation_Code, count(*) as TicketsIssued from tickets_issued_view_part where season = 'autumn' group by Violation_Code order by TicketsIssued desc limit 3;
```
21 &nbsp;&nbsp;&nbsp;	136521<br>
36 &nbsp;&nbsp;&nbsp;	95365<br>
38 &nbsp;&nbsp;&nbsp;	83321<br>

```
select Violation_Code, count(*) as TicketsIssued from tickets_issued_view_part where season = 'winter' group by Violation_Code order by TicketsIssued desc limit 3;
```
No Output
