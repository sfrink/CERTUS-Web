package platform.service;

import java.rmi.RemoteException;

import platform.dto.HelloDto;
import rmi.Initializer;

public class HelloService {

	public HelloDto getHello(String name)
	{
		HelloDto helloDto = new HelloDto();
		
		try
		{
			String messageFromRmi = Initializer.rmi.sayHello(name) ; //"we should have got this message from RMI.";
			helloDto.setText("Hi " + name + ", RMI server says->" + messageFromRmi );
		}
		catch (RemoteException ex)
		{
			System.out.println("Error : " + ex.getMessage());
		}
		
		return helloDto;
	}
}
