java -version
ECHO Starting Mail Client ...
ECHO Current directory to search for application.properties: %CD%\
java -d64-Xms1G -Xmx5G -XX:MaxMetaspaceSize=1G -XX:NewSize=2G -XX:MaxNewSize=3G -jar mail-client-1.0.0.jar --spring.config.location=%CD%\
ECHO Press any key to stop Mail Client ...
PAUSE