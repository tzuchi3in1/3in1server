package com.mss.mssweb.page;

import java.io.IOException;
import java.io.InputStream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;

import com.mss.mssweb.RouterLayout;
import com.mss.mssweb.bc.FileBC;

@Route(value = "upload", layout = RouterLayout.class)
public class UploadPage extends VerticalLayout {
	
	private static final long serialVersionUID = 4L;
	
	private HorizontalLayout hlLayout = new HorizontalLayout();
	private VerticalLayout vlMsgPanel = new VerticalLayout();

	public UploadPage() {
		add(new H2("Upload Page"), new H5("Click Upload Files to select your photos or drag and drop them here."));  
		
		MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
		Upload upload = new Upload(buffer);
		upload.setAcceptedFileTypes("image/jpeg", "image/jpg", "image/png", "image/gif");
		//upload.setAutoUpload(false);
		upload.setWidth("600px");
		
		//upload.addSucceededListener(event -> showOutput(event.getFileName(), buffer.getInputStream(event.getFileName())));
		upload.addSucceededListener(event -> uploadFile(event.getFileName(), buffer.getInputStream(event.getFileName())));
		upload.addFailedListener(event -> showError(event.getFileName()));
		
		//Button btnUpload = new Button("Upload All Files");
		//upload.setUploadButton(btnUpload);
		/*
		upload.addSucceededListener(event -> {
		    Component component = createComponent(event.getMIMEType(),
		            event.getFileName(),
		            buffer.getInputStream(event.getFileName()));
		    showOutput(event.getFileName(), component, output);
		});
		*/
		
		hlLayout.add(upload, vlMsgPanel);
		add(hlLayout);
		
	}
	
	public void uploadFile(String fileName, InputStream fileContent) {
		
		if (FileBC.saveFile(fileName,  fileContent) ) {
			//Label lblFileName = new Label(fileName + " uploaded successfully.");
			
			//vlMsgPanel.add(lblFileName);
		}
		//Label lblFileName = new Label(FileBC.getFileExtension(fileName));
		//add(lblFileName);
	}
	
	public void showError(String fileName) {
		Label lblFileName = new Label("File: " + fileName + " failed to upload.");
		vlMsgPanel.add(lblFileName);
	}
	
	public void showOutput(String fileName, InputStream fileContent) {
		Label lblFileName = new Label(fileName);
		add(lblFileName);
		
	}
}
