rendition
=========

Example Alfresco extension demonstrating deploying and accessing a custom rendition engine.

This extension is built using the Alfresco Maven SDK. To test this extension, simply run:

  mvn integration-test -Pamp-to-war

Once Tomcat has started, log into Alfresco at http://localhost:8080/alfresco.

The sample rendition engine will take any text file (text/plain) and create a rendition using the subsitution rules from the XKCD comic here: http://xkcd.com/1288/. To test the engine, upload a file to the repository, grab its node ref, and open a new browser window pointed to the following URL:

http://localhost:8080/alfresco/service/xkcd?nodeRef={nodeRef}

The webscript at this URL will generate the rendition (if necessary) and return it as a downloadable file.
