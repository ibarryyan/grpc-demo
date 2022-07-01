package org.ymx;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.ymx.proto.HelloReply;
import org.ymx.proto.HelloRequest;
import org.ymx.proto.HelloServiceGrpc;

import java.io.IOException;

/**
 * @desc: grpc服务端
 * @author: YanMingXin
 * @create: 2021/12/18-14:52
 **/
public class Server {

    private final static int port = 5555;
    private io.grpc.Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new HelloServiceImpl())
                .build()
                .start();
        System.out.println("service start...");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                Server.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Server server = new Server();
        server.start();
        server.blockUntilShutdown();
    }

    /**
     * 实现 定义一个实现服务接口的类
     */
    private class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

        @Override
        public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
            System.out.println("service:" + req.getName());
            HelloReply reply = HelloReply.newBuilder().setMessage(("Hello: " + req.getName())).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
