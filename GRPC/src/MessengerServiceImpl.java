import io.grpc.*;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessengerServiceImpl extends JMessengerServiceGrpc.JMessengerServiceImplBase {
    Map<String, Map<String, List<Message>>> userMessages = new ConcurrentHashMap<>();

    @Override
    public void sendMessage(Message request, StreamObserver<Confirmation> responseObserver) {
        try{
        userMessages.putIfAbsent(request.getSender(), new ConcurrentHashMap<>());
        userMessages.get(request.getSender()).putIfAbsent(request.getReceiver(), new ArrayList<>());
        userMessages.get(request.getSender()).get(request.getReceiver()).add(request);

        Confirmation confirmation = Confirmation.newBuilder().setSuccess(true).build();
        responseObserver.onNext(confirmation);
        responseObserver.onCompleted();
    }
    catch(Exception e){
            Confirmation confirmation=Confirmation.newBuilder().setSuccess(false).build();
            responseObserver.onNext(confirmation);
            responseObserver.onCompleted();
    }
    }
    @Override
    public void receiveMessage(Inbox request, StreamObserver<Message> responseObserver) {
        if (!userMessages.containsKey(request.getSender()))
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("Sender not found")));
        else if (!userMessages.get(request.getSender()).containsKey(request.getReceiver()))
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("Receiver not found")));
        else {
            List<Message> messages = userMessages.get(request.getSender()).get(request.getReceiver());
            if (messages.isEmpty()) {
                responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("No messages found")));
                return;
            }
            StringBuilder concatenatedMessages = new StringBuilder();
            for (Message message : messages) {
                concatenatedMessages.append(message.getSender()).append(": ").append(message.getText()).append("\n");
            }
            Message responseMessage = Message.newBuilder()
                    .setSender(request.getSender())
                    .setReceiver(request.getReceiver())
                    .setText(concatenatedMessages.toString())
                    .build();

            responseObserver.onNext(responseMessage);
            responseObserver.onCompleted();
        }
    }
}
