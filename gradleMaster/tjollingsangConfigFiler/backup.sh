#!/bin/bash

service_path=/tjolling-notearkiv-test
app_root=/notearkiv/tjolling/test

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
service_root=http://localhost:8080
service_url=$service_root$service_path
log_file=$script_dir/backuplog.txt
files_dir=$app_root/files
db_file=$app_root/database/Notearkiv.db
backup_files_dir=/backup$app_root/files
backup_db_dir=/backup$app_root/database

echo backup started $(date +"%x %r %Z") > $log_file 

#chown -R ubuntu $app_root

curl -X POST $service_url/rest/adminservice/createcsvexport &>> $log_file
echo >> $log_file
if ! grep -q "sheetdata exported ok!" $log_file; then
   echo Create CSV export failed >> $log_file
   exit 1
fi

mkdir -p $backup_files_dir &>> $log_file
mkdir -p $backup_db_dir &>> $log_file


echo >> $log_file
echo stopping tomcat >> $log_file
systemctl stop tomcat10 &>> $log_file
if [ $? -ne 0 ]; then 
 echo Error stopping tomcat >> $log_file
 exit 1
fi


echo >> $log_file
rsync -azvv  $db_file $backup_db_dir &>> $log_file
if [ $? -ne 0 ]; then
 echo Error copying database >> $log_file
 exit 1
fi


echo >> $log_file
rsync -azv $files_dir/ $backup_files_dir &>> $log_file
if [ $? -ne 0 ]; then
 echo Error copying files >> $log_file
 exit 1
fi

echo >> $log_file
echo starting tomcat >> $log_file
systemctl start tomcat10 &>> $log_file
if [ $? -ne 0 ]; then
 echo Error starting tomcat >> $log_file
 exit 1
fi

chown -R ubuntu /backup$app_root

echo >> $log_file
echo Backup executed ok  $(date +"%x %r %Z") >> $log_file
echo Backup executed ok




