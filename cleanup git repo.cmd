@echo off
REM ==========================================================
REM Cleanup script for removing all Maven target directories
REM from Git history (index) while keeping them locally.
REM Run this in the root folder of your local repository.
REM ==========================================================

echo Searching for tracked 'target' directories ...

REM Step 1: find all tracked files in target/ folders and remove from index
for /f "delims=" %%i in ('git ls-files ^| findstr /r /c:"target/"') do (
    git rm --cached "%%i"
)

REM Step 2: commit the cleanup
git commit -m "remove target directories from repository"

REM Step 3: push changes to remote
git push

echo Done! All 'target' directories are now ignored by Git.
pause