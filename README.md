rendition
=========

Example Alfresco extension demonstrating deploying and accessing a custom rendition engine. 

The sample rendition engine defined in this extension will take any text file (text/plain) and create a rendition using the subsitution rules from the XKCD comic, [Substitutions](http://xkcd.com/1288/). 

This extension is built using the [Alfresco Maven SDK](https://artifacts.alfresco.com/nexus/content/groups/public/alfresco-lifecycle-aggregator/latest/archetypes/alfresco-amp-archetype/index.html). To test this extension, simply run:

    mvn integration-test -Pamp-to-war

Once Tomcat has started, log into Alfresco at http://localhost:8080/alfresco.

To test the engine:

1. Upload a text file to the repository
2. Copy its node ref
3. Navigate to http://localhost:8080/alfresco/service/xkcd?nodeRef={nodeRef}, replacing {nodeRef} with the text file's node ref from step 2.

The webscript at this URL will generate the rendition (if necessary) and return it as a downloadable file.

A detailed description of the classes and configuration involved in this project will be posted to productivist.com.
