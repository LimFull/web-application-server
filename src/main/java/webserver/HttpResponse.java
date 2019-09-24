package webserver;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import db.DataBase;
import model.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	DataOutputStream dos;
	Map<String, String> headers = new HashMap<String, String>();

	public HttpResponse(OutputStream out) {
		dos = new DataOutputStream(out);
	}
	
	public void forward(String url) {
		try {
			byte[] body = Files.readAllBytes(new File("./webapp"+url).toPath());
			if (url.endsWith(".css")) {
				headers.put("Content-Type", "text/css");
			}
			else if (url.endsWith(".js")) {
				headers.put("Content-Type", "application/javascript");
			}
			else {
				headers.put("Content-Type", "text/html;charset=utf-8");
			}
			headers.put("Content-Length", Integer.toString(body.length));
			response200Header();
			responseBody(body);
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	
	}
	private void processHeaders() {
		try {
			for (String key: headers.keySet()) {  // Set<String> keys = headers.keySet();
				dos.writeBytes(key + ": " + headers.get(key)+" /r/n");
			}
		}
		catch (IOException e) {
			log.debug(e.getMessage());
		}
	}
	private void response200Header() {
    	try {
	    	
    		dos.writeBytes("HTTP/1.1 200 OK \r\n");
    		processHeaders();
    		dos.writeBytes("/r/n");
    	
	    }catch (IOException e) {
    		log.error(e.getMessage());
    	}
    }
    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("/r/n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
	public void sendRedirect(String url) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found\r\n");
			processHeaders();
            dos.writeBytes("Location: "+url+" \r\n");
            dos.writeBytes("\r\n");
		}catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
