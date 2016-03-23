select datetime(registered_date/1000, 'unixepoch'), * from notes;
select date(registered_date/1000, 'unixepoch'), * from notes;

select  * from notes;
select datetime(registered_date, 'unixepoch' ), * from note_files;

select datetime(registered_date/1000, 'unixepoch'), * 
from notes 
where datetime(registered_date/1000, 'unixepoch') >= '2016-03-15'