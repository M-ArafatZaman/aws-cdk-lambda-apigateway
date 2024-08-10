package com.myorg;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.Stage;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.ProxyResourceOptions;
import software.amazon.awscdk.services.apigateway.LambdaIntegrationOptions;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here
        // Define the function infra
        Function lambdaHandler = Function.Builder.create(this, "RestLambdaFunction")
                .functionName("RestLambdaFunction")
                .runtime(Runtime.JAVA_21)
                .code(Code.fromAsset("../lambda/target/Handler.jar"))
                .handler("org.awsarafat.Handler")
                .timeout(Duration.seconds(30))
                .build();

        // Define the Rest API
        LambdaRestApi api = LambdaRestApi.Builder.create(this, "RestLamdaFuncAPI")
                .handler(lambdaHandler)
                .proxy(false)
                .integrationOptions(LambdaIntegrationOptions.builder()
                        .allowTestInvoke(true)
                        .timeout(Duration.seconds(29))
                        .build()
                )
                .build();
        // /hello Endpoint
        Resource helloResource = api.getRoot().addResource("hello");
        helloResource.addMethod("GET");
    }
}
