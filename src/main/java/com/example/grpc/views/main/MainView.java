package com.example.grpc.views.main;

import com.example.grpc.ChatMessage;
import com.example.grpc.ChatMessageFromServer;
import com.example.grpc.client.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@PageTitle("gRPC Vaadin Client - Chat")
@Route(value = "")
public class MainView extends VerticalLayout {

    private TextField name;
    private TextField message;
    private Button sendButton;

    public MainView(@Autowired ChatService chatService) {

        name = new TextField("Name");
        message = new TextField("Message");
        sendButton = new Button("Send");

        MessageList list = new MessageList();
        setDefaultHorizontalComponentAlignment(Alignment.START);

        add(name, message, sendButton, list);

        final StreamObserver<ChatMessage> observer = chatService.chat(new StreamObserver<ChatMessageFromServer>() {
            @Override
            public void onNext(ChatMessageFromServer chatMessageFromServer) {
                getUI().get().access(() -> {

                    Instant instant = Instant.ofEpochSecond(
                            chatMessageFromServer.getTimestamp().getSeconds(),
                            chatMessageFromServer.getTimestamp().getNanos());
                    List<MessageListItem> listItems = new ArrayList<>();
                    listItems.addAll(list.getItems());
                    MessageListItem message = new MessageListItem(
                            chatMessageFromServer.getMessage().getMessage(),
                            instant, chatMessageFromServer.getMessage().getFrom());
                    message.setUserColorIndex(isEmpty(listItems) ? 1 : listItems.get(listItems.size()-1).getUserColorIndex() + 1);
                    listItems.add(message);
                    list.setItems(listItems);
                });
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("gRPC Call Completed");
            }
        });

        sendButton.addClickListener(event  -> {
            observer.onNext(ChatMessage.newBuilder()
                    .setFrom(name.getValue())
                    .setMessage(message.getValue())
                    .build());
        });

    }

}