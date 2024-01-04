package com.grpc.bidirectional.streaming.client;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.threads.LimitLatch;

import com.example.bidirectional.BiDirectionalServiceGrpc;
import com.example.bidirectional.BiDirectionalServiceGrpc.BiDirectionalServiceStub;
import com.example.bidirectional.Request;
import com.example.bidirectional.Response;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class BiDirectionalClient 
{

	public static void main(String[] args)
	{
		//Creating the Channel
		int portNumber=8081;
		String address="localhost";
		ManagedChannel channel=ManagedChannelBuilder
				.forAddress(address, portNumber)
				.usePlaintext()
				.build();
		BiDirectionalServiceGrpc.BiDirectionalServiceStub stub=BiDirectionalServiceGrpc.newStub(channel);
		
		CountDownLatch latch=new CountDownLatch(1);
		
		StreamObserver<Request> streamObserver=stub.dataExchange(new StreamObserver<Response>() 
		{
			
			@Override
			public void onNext(Response response) {
				System.out.println("Server Result:::"+""+response.getResult());
			}
			
			@Override
			public void onError(Throwable throwable)
			{
				throwable.printStackTrace();
				latch.countDown();
			}
			
			@Override
			public void onCompleted()
			{
				System.out.println("Server is completed the Receving of the data");	
				latch.countDown();
			}
		});
		
		List<String> messages=Arrays.asList("Kamal","Vamsi","Chakri","Nikhil");
		for(String message:messages)
		{
			System.out.println("===============");
			Request request=Request.newBuilder().setMessage(message).build();
			streamObserver.onNext(request);
		}
		streamObserver.onCompleted();
        try {
            latch.await();
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException | RuntimeException e) {
            e.printStackTrace();
        }
	}
}
