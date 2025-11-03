@echo off

REM
REM FskEncoder start file
REM

set INSTALL_PATH=H:\Git_repository\fskencoder\FskEncoder\deploy\bin

rem set JAVA_HOME=...
rem set PATH=...

set owd=%cd%

cd %INSTALL_PATH%

set CLASS_PATH=.;./FskEncoder.jar

rem
rem   L I B R A R Y    s e c t i o n
rem
set CLASS_PATH=%CLASS_PATH%;../lib/args4j-2.37.jar;
set CLASS_PATH=%CLASS_PATH%;../lib/classgraph-4.8.184.jar
set CLASS_PATH=%CLASS_PATH%;../lib/log4j-api-2.11.2.jar;
set CLASS_PATH=%CLASS_PATH%;../lib/log4j-core-2.11.2.jar;
set CLASS_PATH=%CLASS_PATH%;../lib/miglayout-3.7.4.jar;

rem
rem   E X T E N S I O N    s e c t i o n
rem
set CLASS_PATH=%CLASS_PATH%;../extensions/BinReaderExtension.jar;
set CLASS_PATH=%CLASS_PATH%;../extensions/Ihx8ReaderExtension.jar;
set CLASS_PATH=%CLASS_PATH%;../extensions/Mpf1Extension.jar;
set CLASS_PATH=%CLASS_PATH%;../extensions/Z80TrainerExtension.jar;

set L4J_PATH=../cfg/log4j2.xml
set L4J_CONF="log4j.configurationFile=%L4J_PATH%"

echo.

echo OWD       : %owd%
echo CWD       : %cd%
echo CLASS_PATH: %CLASS_PATH%

echo.

echo Launching Application ...
java -cp %CLASS_PATH% -D%L4J_CONF% application.Application -c ../cfg

cd %owd%
