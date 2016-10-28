echo on
REM SET HISTORIAN_PATH="C:\Users\jilm\Documents\hist"

:: Get default historian path
SET hp=%CD%
IF DEFINED HISTORIAN_PATH SET hp=%HISTORIAN_PATH%

:: Command line argument parsing
:cmd_parse
IF "%1"=="" (
  GOTO end_cmd_parse
) ELSE IF "%1"=="-s" (
  SET cmd="-s"
) ELSE IF "%1"=="-hp" (
  SET hp=%2
  SHIFT
)
SHIFT
GOTO cmd_parse
:end_cmd_parse

java -DHISTORIAN_PATH=%hp% -cp C:\Users\jilm\Documents\java\control4j\dist\control4j.jar cz.control4j.resources.historian.LS %cmd%
