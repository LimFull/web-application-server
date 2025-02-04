package webserver;
import static org.junit.Assert.assertEquals;

import java.io.*;

import org.junit.Test;

import webserver.HttpRequest;
import webserver.RequestLine.HttpMethod;

public class HttpRequestTest {
	private String testDirectory = "./src/test/resources/";
	
	@Test
	public void request_Get() throws Exception{
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
		HttpRequest request = new HttpRequest(in);
		
		assertEquals(HttpMethod.GET, request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("full7002", request.getParameter("userId"));
	}
	
	@Test
	public void request_Post() throws Exception{
		InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
		HttpRequest request = new HttpRequest(in);
		
		assertEquals(HttpMethod.POST, request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("full7002", request.getParameter("userId"));
	}

}
