select datetime(registered_date/1000, 'unixepoch'), * from notes;
select date(registered_date/1000, 'unixepoch'), * from notes;

select  * from notes;
select datetime(registered_date, 'unixepoch' ), * from note_files;

select datetime(registered_date/1000, 'unixepoch'), * 
from notes 
where datetime(registered_date/1000, 'unixepoch') >= '2016-03-15'
delete from notes
where note_id = 2;
update notes set registered_date = '2016-02-21 14:27:10'
where note_id = 3;

select * from note_files

ALTER TABLE notes RENAME TO notes_old;

CREATE TABLE IF NOT EXISTS notes 
             ( 
                          note_id integer PRIMARY KEY autoincrement, 
                          title text, 
                          composed_by text, 
                          arranged_by text, 
                          tags text, 
                          voices text, 
                          description text, 
                          registered_date text, 
                          registered_by text 
             );

insert into notes select note_id, title, composed_by, arranged_by, tags, voices, description,  datetime(registered_date /1000, 'unixepoch' ), registered_by  from notes_old


ALTER TABLE note_files RENAME TO note_files_old;
CREATE TABLE IF NOT EXISTS note_files 
             ( 
                          note_file_id integer PRIMARY KEY autoincrement, 
                          note_id      integer, 
                          file_name text, 
                          description text, 
                          file_size integer, 
                          registered_date text, 
                          registered_by text 
             );
insert into note_files select note_file_id, note_id, file_name, description, file_size, datetime(registered_date /1000, 'unixepoch' ), registered_by  from note_files_old
