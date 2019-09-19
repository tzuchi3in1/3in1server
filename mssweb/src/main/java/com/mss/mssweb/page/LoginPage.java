package com.mss.mssweb.page;

import com.mss.mssweb.RouterLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;

@StyleSheet("../main.css") // Relative to Servlet URL
@Route(value = "login", layout = RouterLayout.class)
public class LoginPage extends VerticalLayout {
	private static final long serialVersionUID = 5L;

	LoginForm comLogin = new LoginForm();
	
	public LoginPage() {
		comLogin.addLoginListener(e -> {
		    boolean isAuthenticated = authenticate(e);
		    if (isAuthenticated) {
		        navigateToMainPage();
		    } else {
		    	comLogin.setError(true);
		    }
		});
		
		add(comLogin);
	}
	
	protected boolean authenticate(LoginEvent loginInfo) {
		boolean isValid = true;
		
		return isValid;
	}
	
	protected void navigateToMainPage() {
		UI currUI = UI.getCurrent();
		
		currUI.navigate(HomePage.class);
	}
}
