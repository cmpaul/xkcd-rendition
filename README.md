rendition
=========

Example Alfresco extension demonstrating deploying and accessing a custom rendition engine. The sample rendition engine defined in this extension will take any text file (text/plain) and create a rendition using the subsitution rules from the XKCD comic here: http://xkcd.com/1288/. 

This extension is built using the Alfresco Maven SDK. To test this extension, simply run:

    mvn integration-test -Pamp-to-war

Once Tomcat has started, log into Alfresco at http://localhost:8080/alfresco.

To test the engine:

1. Upload a text file to the repository
2. Note its node ref
3. Open a new browser window pointed to: http://localhost:8080/alfresco/service/xkcd?nodeRef={nodeRef}

The webscript at this URL will generate the rendition (if necessary) and return it as a downloadable file.

A detailed description of the classes and configuration involved in this project will be posted to productivist.com.
