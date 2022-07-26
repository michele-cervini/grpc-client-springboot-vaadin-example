package com.example.grpc;

import com.example.grpc.client.ChatService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@GrpcClientBean(
    clazz = ChatServiceGrpc.ChatServiceStub.class,
    beanName = "chatServiceStub",
    client = @GrpcClient("chatServiceClient"))
public class AppConfiguration {

    public ChatService chatService(@Autowired ChatServiceGrpc.ChatServiceStub chatServiceStub) {
        return new ChatService(chatServiceStub);
    }
}
