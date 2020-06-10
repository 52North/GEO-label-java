# GEOLabel Lambda

TODO: document

See https://github.com/nuest/aws-lambda-kvp-minimal-java


#### With handler function

Use own simple handler function, see module `/lambda`

The file `lambda/target/glblambda.jar` is a "fat jar" [created with `maven-shade-plugin`](https://docs.aws.amazon.com/lambda/latest/dg/java-create-jar-pkg-maven-no-ide.html) that can be deployed as an AWS Lambda function.

The _Handler_ is the function `org.n52.geolabel.lambda.Hello::myHandler`.

- TODO: HTTP API Gateway not working yet
  - create a route `/hello`
  - attach integration `geolabel-lambda` somehow with https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-integrations.html (probably "custom" to mirror the GEO Label API) so that https://dlttwho6d0.execute-api.eu-central-1.amazonaws.com/default/hello?name=Tester works

#### ~~With Jersey~~

**Note:** This approach is not feasible, because of version conflicts between old libraries used in the GEO Label service and newer Glassfisch libraries. If the GEO-label-java code was to be updated, this could work. The approach is still part of the codebase.

Files:

- `server/pom.xml` has a profile for activating the generation of a "fat jar" for deployment on AWS Lambda. Activating the profile requires to update the `NOTICE` file because other libraries are used
- `server/java.org.n52.geolabel.server.lambda.StreamLambdaHandler.java.dev` is the draft of an handler implementation to connect AWS Lambda events with the existing Jersey annotations, see https://aws.amazon.com/blogs/opensource/java-apis-aws-lambda/ and https://github.com/awslabs/aws-serverless-java-container/tree/master/samples/jersey/pet-store

```bash
mvn notice:generate -P lambda-jar
mvn clean package -P lambda-jar
```

