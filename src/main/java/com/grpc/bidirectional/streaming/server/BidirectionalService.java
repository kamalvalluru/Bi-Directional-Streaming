package com.grpc.bidirectional.streaming.server;

import com.example.bidirectional.BiDirectionalServiceGrpc.BiDirectionalServiceImplBase;
import com.example.bidirectional.Request;
import com.example.bidirectional.Response;

import io.grpc.stub.StreamObserver;

public class BidirectionalService extends BiDirectionalServiceImplBase
{

	@Override
	public StreamObserver<Request> dataExchange(StreamObserver<Response> responseObserver) {
		// TODO Auto-generated method stub
		return new StreamObserver<Request>() 
		{
			
			@Override
			public void onNext(Request request) 
			{
				//Receving the Message to Server from the Client 
				System.out.println("Message recived from the server"+" "+request.getMessage());
				
				//Sending the reply to the client that i have recived the message
				Response response=Response.
						newBuilder()
						.setResult("The data Received from The Client is "+" "+request.getMessage())
						.build();
				
				responseObserver.onNext(response);
				
				
			}
			
			@Override
			public void onError(Throwable throwable)
			{
				throwable.printStackTrace();	
			}
			
			@Override
			public void onCompleted() {
				System.out.println("Data Receving From The Client To server Is Completed");
				responseObserver.onCompleted();
			}
		};
	}
	

}
