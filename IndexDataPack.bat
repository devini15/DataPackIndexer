@ECHO OFF
reg add HKEY_CURRENT_USER\Console /v VirtualTerminalLevel /t REG_DWORD /d 0x00000001 /f
cls
java -jar DataPackIndexer.jar
pause