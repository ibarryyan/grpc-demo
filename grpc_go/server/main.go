package main

import (
	"context"
	"fmt"
	"google.golang.org/grpc"
	__ "grpc_go/proto"
	"log"
	"net"
)

//定义服务端 实现 约定的接口
type HelloServiceServer struct{}

var u = HelloServiceServer{}

//实现 interface
func (s *HelloServiceServer) SayHello(ctx context.Context, req *__.HelloRequest) (resp *__.HelloReply, err error) {
	name := req.Name
	if name == "YMX" {
		resp = &__.HelloReply{Message: "Hello YMX"}
	} else {
		resp = &__.HelloReply{Message: "Hi NoYMX"}
	}
	err = nil
	return resp, nil
}

//启动服务
func main() {
	//1 添加监听的端口
	port := ":6666"
	l, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("端口监听错误 : %v\n", err)
	}
	fmt.Printf("正在监听： %s 端口\n", port)
	//2 启动grpc服务
	s := grpc.NewServer()
	//3 将UserInfoService服务注册到gRPC中，注意第二个参数是接口类型的变量，需要取地址传参
	__.RegisterHelloServiceServer(s, &u)
	s.Serve(l)
}
