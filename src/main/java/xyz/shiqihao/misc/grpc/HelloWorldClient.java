package xyz.shiqihao.misc.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import xyz.shiqihao.misc.grpc.helloworld.GreeterGrpc;
import xyz.shiqihao.misc.grpc.helloworld.HelloReply;
import xyz.shiqihao.misc.grpc.helloworld.HelloRequest;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class HelloWorldClient {
    private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());
    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public HelloWorldClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public HelloWorldClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet(String name) {
        logger.info("Will try to greet " + name + "...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (Exception e) {
            logger.warning("RPC failed");
            return;
        }
        logger.info("Greeting: " + response.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
        HelloWorldClient client = new HelloWorldClient("localhost", 50051);
        try {
            client.greet("sqh");
        } finally {
            client.shutdown();
        }
    }
}
