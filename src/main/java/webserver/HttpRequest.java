package webserver;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import util.HttpRequestUtils;

import java.io.BufferedReader;

public class HttpRequest {
	BufferedReader br;
	String tokens[];
	String line;
	public HttpRequest(InputStream in) throws Exception {
		this.br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		this.line = br.readLine();
		this.tokens = line.split(" ");
	}
	
	public String getMethod() {
		return this.tokens[0];
	}
	public String getPath() {
		String urlToken =  this.tokens[1];
		String token[] = urlToken.split("[?]");
		return token[0];
	}
	public String getHeader(String header) throws Exception {
		String line = this.br.readLine();
		while(!line.equals("")) {
			if (line.startsWith(header)) {
				String token[] = line.split(":");
				return token[1].trim();
			}
			line = br.readLine();
		}
		return line;
	}
	public String getParameter(String param) {
		String paramtoken[] = this.tokens[1].split("[?]");
		Map<String, String> params = HttpRequestUtils.parseQueryString(paramtoken[1]);
		return params.get(param);
	}
}
