@echo off
setLocal EnableDelayedExpansion

REM  This wrapper file is based on the work done by the ant project in "ant.cmd".  The Apache
REM  License is included below:

REM  contributor license agreements.  See the NOTICE file distributed with
REM  this work for additional information regarding copyright ownership.
REM  The ASF licenses this file to You under the Apache License, Version 2.0
REM  (the "License"); you may not use this file except in compliance with
REM  the License.  You may obtain a copy of the License at
REM
REM      http://www.apache.org/licenses/LICENSE-2.0
REM
REM  Unless required by applicable law or agreed to in writing, software
REM  distributed under the License is distributed on an "AS IS" BASIS,  
REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM  See the License for the specific language governing permissions and
REM  limitations under the License.

set _G_ROOT_DIR=%~dp0
set _G_CLASSPATH=

FOR /R %_G_ROOT_DIR%\..\jars %%G IN (*.jar) DO set _G_CLASSPATH=!_G_CLASSPATH!;%%G

set _G_CLASSPATH=%_G_CLASSPATH%;%CLASSPATH%
set _G_CLASSPATH="%_G_CLASSPATH%"

set DEBUG=
if ""%1""==""debug"" set DEBUG=-Xdebug -Xrunjdwp:transport=dt_shmem,address=gosu,server=y,suspend=y
if ""%1""==""debug"" shift

REM Slurp the command line arguments. This loop allows for an unlimited number
REM of arguments (up to the command line limit, anyway).

if ""%1""=="""" goto doneStart

:setupArgs
  if ""%1""=="""" goto doneStart
  set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
  shift
  goto setupArgs

REM This label provides a place for the argument list loop to break out
REM and for NT handling to skip to.
:doneStart

:checkJava
set _JAVACMD=%JAVACMD%

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=%JAVA_HOME%\bin\java.exe

:noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=java.exe

:runGosu
"%_JAVACMD%" %DEBUG% %GOSU_OPTS% -classpath %_G_CLASSPATH% gw.lang.shell.Gosu %CMD_LINE_ARGS%
goto end

:end

rem Check the error code of the Ant build
set GOSU_ERROR=%ERRORLEVEL%

set _JAVACMD=
set CMD_LINE_ARGS=
set DEBUG=
set _G_CLASSPATH=
set _G_ROOT_DIR=
set GOSU_ERROR=
