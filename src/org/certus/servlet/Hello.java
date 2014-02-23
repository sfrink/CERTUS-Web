package org.certus.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.certus.dto.HelloDto;
import org.certus.service.HelloService;


@WebServlet("/Hello")
public class Hello extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("servlet hit.");
	
		String name = request.getParameter("testHello");
		System.out.println(name);
		
		
		HelloService helloService = new HelloService();
		
		HelloDto helloDto = helloService.getHello(name);
		
		System.out.println(helloDto.getText());
		request.getSession().setAttribute("serviceOutput", helloDto);
		response.sendRedirect("greeting.jsp");
		
		return;
		
	}

}
