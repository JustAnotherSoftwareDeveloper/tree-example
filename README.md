# tree-example
This was given to me as a coding assignment. It represents adding and removing tree nodes from a database. The nodes cannot be identical in name

## Technologies Used
* Spring Boot - This is a java framework centered around letting you bootstrap projects easily
* H2 - This is an in memory database, so that I don't need to rely on a clunky mysql
* Mockito - Common Java Testing Framework
* AssertJ - Common Java Testing Framework
## Endpoints
* "api/trees" - GET - This will retrieve the entire tree
* "api/trees" - POST - This is used for adding nodes to the root. The body is a list of names to add
* "api/trees" - DELETE - This is used for deleting the entire tree
* "api/trees/for-node/{nodeId}" - GET - This will retrieve all the children of the node id specified
* "api/trees/for-node" - POST - This will add nodes as children to a specific tree. 
* "api/trees/delete/{nodeId}" - This will delete a node and its children

## Testing

I do not normally add tests to side projects. However, I was asked to for this one. I wrote tests for TreeServiceImpl to give an example of what tests I write look like. Given that this is a small project, I did not bother writing tests for the other two service classes as I would in production-level application.

## Logging
Spring Boot defaults logging to stdout. I did not see the need to change that for a side project. If you want to add a specific logging file, please specify a logging path in the application.properties. Again, in a production environment I would log to a file so that it could be streamed by some log aggregator service.
## Important Notes

This application was written in Java 11 and therefore cannot be run in a Java 8 environment.


