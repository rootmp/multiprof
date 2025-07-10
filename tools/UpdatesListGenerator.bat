@echo off
:start
echo Generate list...
echo.

javac UpdatesListGenerator.java
java -Xmx1G UpdatesListGenerator

pause
