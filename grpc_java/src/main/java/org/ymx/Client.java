package org.ymx;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.ymx.proto.HelloReply;
import org.ymx.proto.HelloRequest;
import org.ymx.proto.HelloServiceGrpc;

import java.util.concurrent.TimeUnit;

/**
 * @desc: grpc客户端
 * @author: YanMingXin
 * @create: 2021/12/18-14:52
 **/
public class Client {

    private final ManagedChannel channel;
    private final HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    public Client(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = HelloServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void hello(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response = blockingStub.sayHello(request);
        System.out.println(response.getMessage());
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 6666);
        for (int i = 0; i < 5; i++) {
            if (i < 3) {
                client.hello("ZS");
            } else {
                client.hello("YMX");
            }
        }
    }
}
