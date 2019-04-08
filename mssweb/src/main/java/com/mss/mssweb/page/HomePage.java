package com.mss.mssweb.page;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
//import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import com.mss.mssweb.RouterLayout;

@Route(value = "", layout = RouterLayout.class)	// Set value="" to become the default landing page.
public class HomePage extends VerticalLayout {
	private static final long serialVersionUID = 3L;
	
	public HomePage() {
		add(new H2("Home Page"), new H5("Home view"));  
		
	}
}
