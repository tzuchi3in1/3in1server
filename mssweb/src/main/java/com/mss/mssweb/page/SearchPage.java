package com.mss.mssweb.page;

import java.util.ArrayList;
import java.util.stream.IntStream;

import com.mss.mssweb.RouterLayout;
import com.mss.mssweb.helper.dbservice.*;
import com.mss.mssweb.bc.*;
import com.mss.mssweb.dto.ImageFile;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

@StyleSheet("../main.css") // Relative to Servlet URL
@Route(value = "search", layout = RouterLayout.class)
public class SearchPage extends VerticalLayout {
	private static final long serialVersionUID = 5L;
		
	private Grid<Integer> grid = new Grid<>();
	
	public SearchPage() {
		add(new H2("Search Photo"));  
		
		TextField txtPhotoName = new TextField("File Name","","search by file name"); 	
		txtPhotoName.setWidth("400px");
		TextField txtEvent = new TextField("Event","","search by event");
		txtEvent.setWidth("400px");  
		TextField txtLocation = new TextField("Location","","search by location");
		txtLocation.setWidth("400px");
		TextField txtPhotographerName = new TextField("Photographer's Name","","search by photographer's name");
		txtPhotographerName.setWidth("400px");  
		
		ComboBox<String> cbCategory = new ComboBox<String>("Category"); 
		cbCategory.setItemLabelGenerator((i) -> i);
		cbCategory.setItems("Recycle","Donation","Disaster Recovery");
		cbCategory.setWidth("400px");
		
		Label lblSpacing1 = new Label("");
		lblSpacing1.setWidth("720px"); 
		Button btnSearch = new Button("Search", event -> searchRecord());
		
		HorizontalLayout hlRow1 = new HorizontalLayout(txtPhotoName,txtEvent);
		HorizontalLayout hlRow2 = new HorizontalLayout(txtLocation,txtPhotographerName);
		HorizontalLayout hlRow3 = new HorizontalLayout(cbCategory); 
		HorizontalLayout hlRow4 = new HorizontalLayout(lblSpacing1, btnSearch);
		 
		add(hlRow1, hlRow2, hlRow3, hlRow4);
		
		//DBService objDS = new DBService(); 
		//Label lblTestDB = new Label(objDS.getData());
		//add(lblTestDB);
		
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		//Label lblTestResult1 = new Label("");
		
		lstImageFile = FileBC.searchImage("", "");
		initImageGrid(lstImageFile);
		
		//lblTestResult1.setText("Count: " + String.valueOf(lstImageFile.size()));
		//add(lblTestResult1);
		/*
		if (lstImageFile.size() > 0) {
			
			ImageFile objFile = lstImageFile.get(0);
			lblTestResult1.setText(objFile.getFileName());
		}
		*/
		/* flow grid */
		/*
		grid.addColumn(i -> i).setHeader("Number");
		grid.addColumn(i -> new Image("theme/ajax-loader.gif", "alt text")).setHeader("Preview");
		add(grid); 		 
		*/
	}
	
	public void initImageGrid(ArrayList<ImageFile> lstImageFile) {
		
		VerticalLayout gridParent = new VerticalLayout();
		int maxCol = 5;
		int maxRecords = lstImageFile.size();
		int maxRow = 0;
		String rootPath = "D:\\Development\\eclipse-workspace\\";
		String childPath = "files\\P001";
		
		if (maxRecords % maxCol > 0) {
			maxRow = (maxRecords / maxCol) + 1;
		}
		else {
			maxRow = maxRecords / maxCol;
		}
		
		if (lstImageFile.size() > 0) {
			
			
			// Try using button. https://vaadin.com/components/vaadin-button/java-examples
			/*
			Button button = new Button(
			        new Image("img/vaadin-logo.svg", "Vaadin logo"));
			button.setAutofocus(true);

			button.addClickListener(this::showButtonClickedMessage);
			*/
			
			/* Render Grid Paging DDL. */
			HorizontalLayout gridPagingRow = new HorizontalLayout();
			
			//Label gridTitle = new Label("Search Result");
			//gridTitle.setWidth("800px");
			Div divGridTitle = new Div();
			divGridTitle.add(new H4(""));
			//divGridTitle.setWidth("900px");
			divGridTitle.setClassName("gridTitle");
			
			Div divGridPaging = new Div();
			H4 h4GridPaging = new H4("Page: ");
			
			ComboBox<String> cbGridPaging = new ComboBox<String>(""); 
			cbGridPaging.setItemLabelGenerator((i) -> i);
			cbGridPaging.setItems("1","2","3");
			
			divGridPaging.setClassName("gridPagingCB");
			h4GridPaging.add(cbGridPaging);
			divGridPaging.add(h4GridPaging);
			
			gridPagingRow.add(divGridTitle, divGridPaging);
			//gridParent.add(gridPagingRow);
			add(gridPagingRow);
			
			/* Render Grid Table. */
			HorizontalLayout hlRow = new HorizontalLayout();
			
			for (int i = 1; i <= lstImageFile.size(); i++) {
				ImageFile objFile = lstImageFile.get(i - 1);
				InputStreamFactory isf = objFile;
				StreamResource imgStream = new StreamResource(objFile.getFileName(), isf);
				Image imgFile  = new Image(imgStream, "");
				imgFile.setClassName("gridImage");
				//imgFile.setWidth(objFile.getWidth());
				//imgFile.setHeight(objFile.getHeight());
				
				//Button button = new Button(imgFile, event -> viewDetails(objFile.getFileName()));
				
				Anchor imgA = new Anchor();
				imgA.add(imgFile);
				imgA.setHref("javascript:window.open('http://www.yahoo.com')");
				imgA.setTarget("_blank");
				
				Div imgDiv = new Div();
				imgDiv.add(imgA);
				imgDiv.setClassName("gridImageContainer");
				
				hlRow.add(imgDiv);
				
				if (i % maxCol == 0) {
					gridParent.add(hlRow);
					hlRow = new HorizontalLayout();
				}
			}
			/*
			for (int i = 1; i <= lstImageFile.size(); i++) {
				// stop here. How to load image.
				//Image imgFile = new Image(rootPath + childPath + "\\" + lstImageFile.get(i).getFileName() + "." + lstImageFile.get(i).getFileExt(), "");
				ImageFile objFile = lstImageFile.get(i - 1);
				InputStreamFactory isf = objFile;
				StreamResource imgStream = new StreamResource(objFile.getFileName(), isf);
				Image imgFile  = new Image(imgStream, "");
				imgFile.setWidth(objFile.getWidth());
				imgFile.setHeight(objFile.getHeight());
				hlRow.add(imgFile);
				
				if (i % maxCol == 0) {
					gridParent.add(hlRow);
					hlRow = new HorizontalLayout();
				}
			}
			*/
			gridParent.add(hlRow);
			gridParent.setClassName("grid");
		}
		add(gridParent);
	}
	
	public void viewDetails(String strFileName) {
		
	}
	
	public void searchRecord() {
		grid.setItems(IntStream.range(1,10).boxed()); 
	}
}
