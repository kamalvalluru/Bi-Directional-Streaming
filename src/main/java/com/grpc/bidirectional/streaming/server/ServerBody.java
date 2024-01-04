package com.grpc.bidirectional.streaming.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServerBody
{
	public static void main(String[] args) throws IOException, InterruptedException
	{
		int portNumber=8081;
		Server server=ServerBuilder
				.forPort(portNumber)
				.addService(new BidirectionalService())
				.build();
		
		server.start();
		System.out.println("Server is Started Successfully");
		
		Runtime.getRuntime().addShutdownHook(new Thread(()->
		{
			System.out.println("Server is Shutdowing");
			server.shutdown();
		}));
		server.awaitTermination();
	}

}
