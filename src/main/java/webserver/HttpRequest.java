package webserver;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	
	
	private String method;
	private String path;
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, String> params = new HashMap<String, String>();
	private RequestLine requestLine;
	
	public HttpRequest(InputStream in) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = br.readLine();
			if (line == null) {
				return;
			}
			
			requestLine = new RequestLine(line);
			
			
			line = br.readLine();
			System.out.println("line = " + line);
			while(!line.equals("")) {
				log.debug("header : {}", line);
				String tokens[]  = line.split(":");
				headers.put(tokens[0].trim(), tokens[1].trim());
				line = br.readLine();
			
				if(line == null || line.equals("")){
					break;
				}
			}
			
			if (getMethod().equals("POST")) {
				String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
				log.debug("now body = {}", body);
				params = HttpRequestUtils.parseQueryString(body);
			} else {
				params = requestLine.getParams();
			}
		} catch (IOException io) {
			log.error(io.getMessage());
		}
		
		
	}
	
	private void processRequestLine(String requestLine) {
		log.debug("request line : {}", requestLine);
		String[] tokens = requestLine.split(" ");
		method = tokens[0];
		
		if (method.equals("POST")) {
			path = tokens[1];
			return;
		}
		
		int index = tokens[1].indexOf("?");
		if (index == -1) {
			path = tokens[1];
		} else {
			path = tokens[1].substring(0, index);
			params = HttpRequestUtils.parseQueryString(tokens[1].substring(index+1));
		}
	}
	
	public String getMethod() {
		return requestLine.getMethod();
	}
	public String getPath() {
		return requestLine.getPath();
		
	}
	public String getHeader(String header) throws Exception {
		return headers.get(header);
	}
	public String getParameter(String param) {
		return params.get(param);
		
	}
}
