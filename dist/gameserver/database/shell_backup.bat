@echo off
title L2Studio - Essence Vanguard (Shell Backup - Windows)
SETLOCAL

set "MySQLPath=C:\Program Files\MariaDB 10.6"
set "BackupPath=D:\Servidor"

set "MySQLDB=essence"
set "MySQLUser=root"
set "MySQLPwd=root"
set "MySQLPort=3306"

set "BackupDate=%date:~-4%%date:~-7,2%%date:~-10,2%"
set "BackupTime=%time:~-11,2%%time:~-8,2%%time:~-5,2%"

set "BackupFile=%BackupDate%_%BackupTime%_barionBKP.sql"

%MySQLPath:~0,2%

cd %MySQLPath%\bin

echo Server database backup is being created ... [database: %MySQLDB%]

mysqldump -u%MySQLUser% -p%MySQLPwd% %MySQLDB% --port=%MySQLPort%> "%BackupPath%\%BackupFile%"