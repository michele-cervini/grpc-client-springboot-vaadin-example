package com.example.grpc.client;

import com.example.grpc.ChatMessage;
import com.example.grpc.ChatMessageFromServer;
import com.example.grpc.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatServiceGrpc.ChatServiceStub chatServiceStub;

    public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessageFromServer> streamObserver) {
        return chatServiceStub.chat(streamObserver);
    }
}
