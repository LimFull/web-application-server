package webserver;

import db.DataBase;
import model.User;

public class LoginController implements Controller{

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		User user = DataBase.findUserById(request.getParameter("userId"));
		if (user == null) {
			response.sendRedirect("/user/login_failed.html");
			return;
		}
		
		if (user.getPassword().equals(request.getParameter("password"))){
			response.setHeader("Set-Cookie", "logined = true");
			response.sendRedirect("/index.html");
		}else {
			response.sendRedirect("/user/login_failed.html");
		}
		
	}

}
