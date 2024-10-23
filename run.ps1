$dir = ".\target\appassembler\bin"
# temporarily change to the correct folder
Push-Location $dir

# do stuff, call ant, etc
#
.\SendISOMessage.bat

# now back to previous directory
Pop-Location
