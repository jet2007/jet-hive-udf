@ECHO OFF
ECHO *************************************************

set artifactId=jdo2-api
set groupId=javax.jdo
set version=2.3-ec
set file=lib\jdo2-api-2.3-ec.jar
call mvn install:install-file -Dfile=%file% -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%version% -Dpackaging=jar 
ECHO *************************************************%file%




set artifactId=devtools
set groupId=nl.basjes.parse.useragent
set version=5.11
set file=lib\jdo2-api-2.3-ec.jar
call mvn install:install-file -Dfile=%file% -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%version% -Dpackaging=jar 
ECHO *************************************************%file%
pause
