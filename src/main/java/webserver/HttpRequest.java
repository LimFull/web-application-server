package webserver;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

import java.io.BufferedReader;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	BufferedReader br;
	//String tokens[];
	String requestline[];
	String method;
	String content;
	Map<String, String> header = new HashMap<String, String>();
	public HttpRequest(InputStream in) throws Exception {
		String line;
		this.br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		line = br.readLine();
		this.requestline = line.split(" ");
		this.method = requestline[0];
		while(!line.equals("")) {
			
			line = br.readLine();
			log.debug(line);
			if (line == null) {
				break;
			}
			String tokens[]  = line.split(": ");
			if (!tokens[0].isEmpty()) {
				this.header.put(tokens[0], tokens[1].trim());
			}
		}
		
		if (method.equals("POST")) {
			this.content = br.readLine();
		}
	}
	
	public String getMethod() {
		return this.method;
	}
	public String getPath() {
		if (method.equals("GET")) {
			String urlToken =  this.requestline[1];
			String token[] = urlToken.split("[?]");
			return token[0];	
		} else if (method.equals("POST")) {
			String urlToken = this.requestline[1];
			return urlToken;
		}
		return "";
		
	}
	public String getHeader(String header) throws Exception {
		return this.header.get(header);
	}
	public String getParameter(String param) {
		if (method.equals("GET")) {
			String paramtoken[] = this.requestline[1].split("[?]");
			Map<String, String> params = HttpRequestUtils.parseQueryString(paramtoken[1]);
			return params.get(param);
		} else if (method.equals("POST")) {
			Map<String, String> params = HttpRequestUtils.parseQueryString(this.content);
			return params.get(param);
		}
		return "";
		
	}
}
