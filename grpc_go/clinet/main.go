package main

import (
	"context"
	"fmt"
	"google.golang.org/grpc"
	_ "grpc_go/proto"
	__ "grpc_go/proto"
	"log"
)

func main() {
	//1 配置grpc服务端的端口作为客户端的监听
	conn, err := grpc.Dial(":5555", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("正在监听服务端 : %v\n", err)
	}
	defer conn.Close()
	//2 实例化 UserInfoService 服务的客户端
	client := __.NewHelloServiceClient(conn)
	//3 调用grpc服务
	req := new (__.HelloRequest)
	req.Name = "YMX"
	resp, err := client.SayHello(context.Background(), req)
	if err != nil {
		log.Fatalf("请求错误 : %v\n", err)
	}
	fmt.Printf("响应内容 : %v\n", resp)
}