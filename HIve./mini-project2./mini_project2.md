
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
load data inpath '/tmp/assignments/' into table parking_violations;
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
