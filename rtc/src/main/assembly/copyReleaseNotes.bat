@echo "copyReleaseNotes.bat start copy ......"
set srcfile=%1
set dsttile=%2

set srcfile=%srcfile:/=\%
set dsttile=%dsttile:/=\%

copy /y %srcfile% %dsttile%
@if %errorlevel% == 0 (
    @echo "copyReleaseNotes.bat copy releaseNotes success!"
) else (
    @echo "copyReleaseNotes.bat copy releaseNotes failed!"
    exit 2
)