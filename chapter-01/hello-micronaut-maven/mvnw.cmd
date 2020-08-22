IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B 
	echo Downloading from: %DOWNLOAD_URL%
    echo Couldn't find %WRAPPER_JAR%, downloading it ...
    echo Finished downloading %WRAPPER_JAR%
    echo Found %WRAPPER_JAR%
    powershell -Command "(New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')"
%MAVEN_JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
)
) else (
:OkJHome
:baseDirFound
:baseDirNotFound
:end
:endDetectBaseDir
:endReadAdditionalConfig
:error
:findBaseDir
:init
:skipRcPost
:skipRcPre
@REM
@REM     e.g. to debug Maven itself, use
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM "License"); you may not use this file except in compliance
@REM ----------------------------------------------------------------------------
@REM ==== END VALIDATION ====
@REM ==== START VALIDATION ====
@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@REM End of extension
@REM Execute a user defined script before this one
@REM Extension to allow automatically downloading the maven-wrapper.jar from Maven-central
@REM Fallback to current working directory if not found.
@REM Find the project base dir, i.e. the directory that contains the folder ".mvn".
@REM JAVA_HOME - location of a JDK home dir
@REM KIND, either express or implied.  See the License for the
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM M2_HOME - location of maven2's installed home dir
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM Maven2 Start Up Batch script
@REM Optional ENV vars
@REM Required ENV vars:
@REM This allows using the maven wrapper in projects that prohibit checking in binary data.
@REM To isolate internal variables from possible post scripts, we use another setlocal
@REM Unless required by applicable law or agreed to in writing,
@REM check for post script, once with legacy .bat ending and once with .cmd ending
@REM check for pre script, once with legacy .bat ending and once with .cmd ending
@REM distributed with this work for additional information
@REM enable echoing my setting MAVEN_BATCH_ECHO to 'on'
@REM or more contributor license agreements.  See the NOTICE file
@REM pause the script if MAVEN_BATCH_PAUSE is set to 'on'
@REM regarding copyright ownership.  The ASF licenses this file
@REM set %HOME% to equivalent of $HOME
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM set title of command window
@REM software distributed under the License is distributed on an
@REM specific language governing permissions and limitations
@REM to you under the Apache License, Version 2.0 (the
@REM under the License.
@REM with the License.  You may obtain a copy of the License at
@echo off
@endlocal & set ERROR_CODE=%ERROR_CODE%
@endlocal & set JVM_CONFIG_MAVEN_PROPS=%JVM_CONFIG_MAVEN_PROPS%
@if "%MAVEN_BATCH_ECHO%" == "on"  echo %MAVEN_BATCH_ECHO%
@setlocal
@setlocal EnableExtensions EnableDelayedExpansion
FOR /F "tokens=1,2 delims==" %%A IN (%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties) DO (
IF "%WDIR%"=="%CD%" goto baseDirNotFound
IF EXIST "%WDIR%"\.mvn goto baseDirFound
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir
IF NOT EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\jvm.config" goto endReadAdditionalConfig
SET MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
cd "%EXEC_DIR%"
cd ..
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo Error: JAVA_HOME not found in your environment. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
exit /B %ERROR_CODE%
for /F "usebackq delims=" %%a in ("%MAVEN_PROJECTBASEDIR%\.mvn\jvm.config") do set JVM_CONFIG_MAVEN_PROPS=!JVM_CONFIG_MAVEN_PROPS! %%a
goto end
goto endDetectBaseDir
goto error
goto findBaseDir
if "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")
if "%MAVEN_BATCH_PAUSE%" == "on" pause
if "%MAVEN_TERMINATE_CMD%" == "on" exit %ERROR_CODE%
if ERRORLEVEL 1 goto error
if exist "%HOME%\mavenrc_post.bat" call "%HOME%\mavenrc_post.bat"
if exist "%HOME%\mavenrc_post.cmd" call "%HOME%\mavenrc_post.cmd"
if exist "%HOME%\mavenrc_pre.bat" call "%HOME%\mavenrc_pre.bat"
if exist "%HOME%\mavenrc_pre.cmd" call "%HOME%\mavenrc_pre.cmd"
if exist "%JAVA_HOME%\bin\java.exe" goto init
if exist %WRAPPER_JAR% (
if not "%JAVA_HOME%" == "" goto OkJHome
if not "%MAVEN_SKIP_RC%" == "" goto skipRcPost
if not "%MAVEN_SKIP_RC%" == "" goto skipRcPre
set DOWNLOAD_URL="https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.0/maven-wrapper-0.4.0.jar"
set ERROR_CODE=0
set ERROR_CODE=1
set EXEC_DIR=%CD%
set MAVEN_PROJECTBASEDIR=%EXEC_DIR%
set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
set MAVEN_PROJECTBASEDIR=%WDIR%
set WDIR=%CD%
set WDIR=%EXEC_DIR%
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
title %0