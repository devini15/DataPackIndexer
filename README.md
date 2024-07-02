# DataPackIndexer
Allows you to use find/replace operations on every file in a directory.

## Zip Contents
### DataPackIndexer.jar
 - All that funny code
 - Put this in the parent directory of functions
### DataPackIndexer.bat
 - This is a command line application, so you should use this batch to run it
 - Put this in the same place as the JAR file or it won't work!
### SetUpConsoleColors.bat
 - If you see weird symbols in the output, and/or all the text is the same color, running this as administrator will set up your console colors permanant
 - You can also manually fix the console colors by navigating to HKEY_CURRENT_USER\Console in RegEdit, making a new DWORD called "VirtualTerminalLevel" and setting the value to "1" (this is what the batch does)
 - If you don't want to do either of those, the program will still work, it will just look a little weird because the escape sequences I used for text colors will render as nonsense characters.
## Other
- You can copy/edit my code however you want, I'd love to know what you use it for!
- If you want me to fix/add something, feel free to reach out to me on [Reddit](reddit.com/u/devini15) or Discord.
