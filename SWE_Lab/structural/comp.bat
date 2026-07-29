if not exist out mkdir out

if exist sources.txt del sources.txt

for /R src %%f in (*.java) do @echo %%f >> sources.txt

javac -d out @sources.txt