package com.mss.mssweb.page;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.login.LoginOverlay;

//import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.mss.mssweb.RouterLayout;
import com.mss.mssweb.bc.AuthenticationBC;
import com.mss.mssweb.dto.UserInfo;

@Route(value = "", layout = RouterLayout.class)	// Set value="" to become the default landing page.
public class HomePage extends VerticalLayout {
	private static final long serialVersionUID = 3L;
	
	public HomePage() {
		add(new H2("Home Page"), new H5("Home view"));  
		// Ask for credential if session is empty or expired.
		//checkLoginSession();
	}
	
	// Ask for credential if session is empty or expired.
	protected void checkLoginSession() {
		boolean challengeUser = true;
		VaadinSession session = VaadinSession.getCurrent();
		
		if (session != null) {
			if(session.getAttribute("sesUserID") != null) {
				challengeUser = false;
			}
		}
		
		if (challengeUser) {
			LoginOverlay component = new LoginOverlay();
			component.addLoginListener(e -> {
				
			    boolean isAuthenticated = AuthenticationBC.authenticate(e.getUsername(), e.getPassword());
			    
			    if (isAuthenticated) {
			    	UserInfo userInfo = AuthenticationBC.getUserInfo(e.getUsername());
			    	
			    	if (userInfo != null) {
			    		session.setAttribute("sesUserID", userInfo.getUserID());
			    		session.setAttribute("sesUserFullName", userInfo.getFullName());
			    		session.setAttribute("sesUserType", userInfo.getUserType());
			    		session.setAttribute("sesUserIsActive", userInfo.getIsActive());
			    	}
			        component.close();
			    } else {
			    	component.setError(true);
			    }
			    
			});
			component.setOpened(true);
		}
	}
	
}
