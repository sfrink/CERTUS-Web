package platform.service;

import platform.dto.HelloDto;

public class HelloService {

	public HelloDto getHello(String name)
	{
		HelloDto helloDto = new HelloDto();
		String messageFromRmi = "we should have got this message from RMI.";
		helloDto.setText("Hi " + name + ", " + messageFromRmi);
		
		return helloDto;
	}
}
