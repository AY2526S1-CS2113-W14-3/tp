@echo off
setlocal enableextensions
REM Optional but helpful for debugging:
REM setlocal enabledelayedexpansion

REM Always start from repo root for predictable paths
pushd "%~dp0\.."

REM 1) Build fat jar, fail fast
call gradlew.bat --no-daemon --console=plain clean shadowJar
if errorlevel 1 (
  echo Gradle build failed.
  exit /b 1
)

REM 2) Find the Shadow 'all' jar deterministically
pushd "build\libs"
set "jarloc="
for %%F in (*-all.jar) do (
  set "jarloc=%%~fF"
  goto :foundJar
)
echo Could not find *-all.jar in build\libs. Available jars:
dir /b *.jar
exit /b 1

:foundJar
echo Using JAR: "%jarloc%"

REM 3) Run program with redirected IO
REM    Make sure input.txt contains the command that exits your app.
REM    Example: last line should be /exit (or your app's quit command).
REM    Use stdbuf on *nix; on Windows we just ensure output is produced.
popd
set "INPUT=.\text-ui-test\input.txt"
set "ACTUAL=.\text-ui-test\ACTUAL.TXT"
set "EXPECTED=.\text-ui-test\EXPECTED.TXT"

if not exist "%INPUT%" (
  echo Missing input file: "%INPUT%"
  exit /b 1
)

REM Run and capture output; fail if Java returns non-zero
pushd "build\libs"
java -jar "%jarloc%" < "%INPUT%" > "%ACTUAL%"
if errorlevel 1 (
  echo Application exited with non-zero code.
  popd
  exit /b 1
)
popd

REM 4) Compare outputs; make the step fail on mismatch
fc /n "%ACTUAL%" "%EXPECTED%" >nul
if errorlevel 1 (
  echo Test failed!
  exit /b 1
) else (
  echo Test passed!
)

popd
exit /b 0
