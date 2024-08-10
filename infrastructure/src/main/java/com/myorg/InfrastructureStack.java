package com.myorg;

import software.amazon.awscdk.Duration;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.apigateway.ResourceOptions;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.Resource;

import java.util.List;
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
                .endpointTypes(List.of(EndpointType.EDGE))
                .build();
        // /hello Endpoint
        Resource helloResource = api.getRoot().addResource("hello", ResourceOptions.builder()
                .defaultIntegration(new LambdaIntegration(lambdaHandler))
                .build());
        helloResource.addMethod("GET");
    }
}
