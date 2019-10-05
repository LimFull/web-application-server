package webserver;

import java.util.Collection;
import java.util.Map;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;

public class ListUserController extends AbstractController{
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		HttpSession session = request.getSession();
		if (!isLogin(session)) {
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
	
    private boolean isLogin(HttpSession session) {
    	//Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
    	Object user = session.getAttribute("user");
    			
    	if (user == null) {
    		return false;
    	}else {
    		return true;
    	}
    	
    }
}
