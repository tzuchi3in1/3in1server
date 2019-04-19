package com.mss.mssweb.page;

import com.mss.mssweb.RouterLayout;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@StyleSheet("../main.css") // Relative to Servlet URL
@Route(value = "fileDetailPage", layout = RouterLayout.class)
public class FileDetailPage extends VerticalLayout implements HasUrlParameter<String> { 
	private static final long serialVersionUID = 7L;
	
	private String fileName = "";
	
	public FileDetailPage() {
		add(new Label("Detail Page"));
	}

	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		// TODO Auto-generated method stub
		fileName = parameter;
		getImageFile();
	}
	
	public void getImageFile() {
		add(new Label("File Name: " + fileName));
	}
}
