package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import util.HttpRequestUtils;
import util.IOUtils;
import model.User;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	HttpRequest request = new HttpRequest(in);
        	String path = getDefaultPath(request.getPath());
        	log.debug("만들어진 path = {}", path);
        	HttpResponse response = new HttpResponse(out);
        	
        	
        	if (path.equals("/user/create")) {
        		User user = new User(
        				request.getParameter("userId"),
        				request.getParameter("password"),
        				request.getParameter("name"),
        				request.getParameter("email"));
        		
        		log.debug("User : {}", user);
        		DataBase.addUser(user);
        		DataOutputStream dos = new DataOutputStream(out);
        		response.sendRedirect("/index.html");
        		
        		
        	}else if (path.equals("/user/login")){
        		User user = DataBase.findUserById(request.getParameter("userId"));
        		if (user == null) {
        			response.sendRedirect("/user/login_failed.html");
        			return;
        		}
        		
        		if (user.getPassword().equals(request.getParameter("password"))){
        			DataOutputStream dos = new DataOutputStream(out);
        	
        			response.setHeader("Set-Cookie", "logined = true");
        			response.sendRedirect("/index.html");
        		}else {
        			response.sendRedirect("/user/login_failed.html");
        		}
        		
        	}else if (path.equals("/user/list")) {
        		if (!isLogin(request.getHeader("Cookie"))) {
        			response.sendRedirect("/user/login.html");
        			
        			return;
        		}
        		Collection<User> users = DataBase.findAll();
        		StringBuilder sb = new StringBuilder();
        		sb.append("<table border='1'>");
        		for (User user : users) {
        			sb.append("<tr>");
        			sb.append("<td>" + user.getUserId() + "</td>");
        			sb.append("<td>" + user.getName() +  "</td>");
        			sb.append("<td>" + user.getEmail() +  "</td>");
        			sb.append("</tr>");
        		}
        		sb.append("</table>");
        		response.forwardBody(sb.toString());
        		
        	}
        	else{
        		response.forward(path);
        	}
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    private String getDefaultPath(String path) {
    	if (path.equals("/")) {
    		return "/index.html";
    	}
    	return path;
    }
    
    private boolean isLogin(String cookieValue) {
    	Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
    	String value = cookies.get("logined");
    	
    	if (value == null) {
    		return false;
    	}else {
    		return Boolean.parseBoolean(value);
    	}
    	
    }
  
    
  
 
}
