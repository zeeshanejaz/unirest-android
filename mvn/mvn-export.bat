@echo off
set path=%path%;..\..\..\..\tools\apache-maven-3.1.1\bin
mvn deploy:deploy-file^
 -DgroupId=com.mashape.unirest^
 -DartifactId=unirest-android^
 -Dversion=1.0.1^
 -Dpackaging=aar^
 -Dfile=..\unirest-android\build\libs\unirest-android.aar^
 -Durl=file:..\..\unirest-android-mvn-repo\