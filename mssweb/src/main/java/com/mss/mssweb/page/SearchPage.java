package com.mss.mssweb.page;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;

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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import de.kaesdingeling.hybridmenu.components.Notification;
import de.kaesdingeling.hybridmenu.components.NotificationCenter;

@StyleSheet("../main.css") // Relative to Servlet URL
@Route(value = "search", layout = RouterLayout.class)
public class SearchPage extends VerticalLayout {
	private static final long serialVersionUID = 5L;
		
	VaadinSession session = VaadinSession.getCurrent();
	private Grid<Integer> grid = new Grid<>();
	private Label message;
	private Div divMessage;
	private TextField txtPhotoName; 
	private VerticalLayout gridParent = new VerticalLayout();
	private int pageSize = 10;
	
	public SearchPage() {
		add(new H2("Search Photo"));  
		
		message = new Label("");
		
		divMessage = new Div();
		divMessage.setClassName("alertMessage");
		divMessage.add(message);
		add(divMessage);
		
		txtPhotoName = new TextField("File Name","","search by file name"); 	
		txtPhotoName.setWidth("400px");
		TextField txtEvent = new TextField("Event","","search by event");
		txtEvent.setWidth("400px");  
		TextField txtLocation = new TextField("Location","","search by location");
		txtLocation.setWidth("400px");
		TextField txtPhotographerName = new TextField("Photographer's Name","","search by photographer's name");
		txtPhotographerName.setWidth("400px");  
		
		ComboBox<String> cbCategory = new ComboBox<String>("Category"); 
		HashMap<String,String> categoryList = FileBC.getCategory("");
		cbCategory.setItemLabelGenerator((i) -> i);
		//cbCategory.setItems("Recycle","Donation","Disaster Recovery");
		cbCategory.setItems(categoryList.values());
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
	
	public void initImageGrid(int currentPageNo, int totalRecord, ArrayList<ImageFile> lstImageFile) {
		
		int maxCol = 5;
		//int maxRecords = lstImageFile.size();
		int maxRow = 1;
		String rootPath = "D:\\Development\\eclipse-workspace\\";
		String childPath = "files\\P001";
		int totalPages = 1;
		
		// Re-initialize grid.
		gridParent = new VerticalLayout();
		
		/*
		if (maxRecords % maxCol > 0) {
			maxRow = (maxRecords / maxCol) + 1;
		}
		else {
			maxRow = maxRecords / maxCol;
		}
		*/
		
		if (pageSize % maxCol > 0) {
			maxRow = (pageSize / maxCol) + 1;
		}
		else {
			maxRow = pageSize / maxCol;
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
			//divGridTitle.add(new H4("Search Result"));
			divGridTitle.add(new H4("Total Record(s): " + totalRecord));
			//divGridTitle.setWidth("900px");
			divGridTitle.setClassName("gridTitle");
			
			/* Construct Paging ComboBox. */
			Div divGridPaging = new Div();
			H4 h4GridPaging = new H4("Page: ");
			
			ComboBox<String> cbGridPaging = new ComboBox<String>(""); 
			//cbGridPaging.setItemLabelGenerator((i) -> i);
			//cbGridPaging.setItems("1","2","3");
			if (totalRecord % (maxCol * maxRow) > 0) {
				totalPages = (totalRecord / (maxCol * maxRow)) + 1;
			}
			else {
				totalPages = (totalRecord / (maxCol * maxRow));
			}
			//System.out.println("total Record: " + totalRecord);
			//System.out.println("Total Pages: " + totalPages);
			
			ArrayList<String> pageNo = new ArrayList<String>();
			for (int i = 1; i <= totalPages; i++) {
				pageNo.add(String.valueOf(i));
			}
			
			cbGridPaging.setItems(pageNo);
			
			if (currentPageNo <= pageNo.size()) {
				cbGridPaging.setValue(String.valueOf(currentPageNo));
			}
			else {
				cbGridPaging.setValue(String.valueOf(1));
			}
			
			cbGridPaging.addValueChangeListener(event -> {
				
				
			    if (event.getSource().isEmpty()) {
			    	divMessage.setVisible(true);
			        message.setText("No page number selected.");
			        /*
			        NotificationCenter notificationCenter = VaadinSession.getCurrent().getAttribute(NotificationCenter.class);
					Notification notification = Notification.get()
							.withTitle("Alert")
							.withContent("No page number selected.")
							.withIcon(VaadinIcon.EXCLAMATION);
					notificationCenter.add(notification);
					*/
			    } else {
			    	// stop here.
			        //message.setText("Selected browser: " + event.getValue());
			    	getRecord(event.getValue());
			    }
			});
			
			
			
			divGridPaging.setClassName("gridPagingComboBox");
			h4GridPaging.add(cbGridPaging);
			divGridPaging.add(h4GridPaging);
			
			gridPagingRow.add(divGridTitle, divGridPaging);
			gridPagingRow.setClassName("gridPagingRow");
			//gridParent.add(gridPagingRow);
			gridParent.add(gridPagingRow);
			
			
			/* Render Grid Table. */
			HorizontalLayout hlRow = new HorizontalLayout();
			int rowCount = 0;
			
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
				imgA.setClassName("gridImageAnchor");
				imgA.add(imgFile);
				imgA.setHref("javascript:window.open('fileDetailPage/" + objFile.getFileName() + "')");
				imgA.setTarget("_blank");
				
				VerticalLayout imgFrame = new VerticalLayout();
				imgFrame.setClassName("gridDataImgFrame");
				imgFrame.add(imgA);
				
				VerticalLayout imgEditedCommFrame = new VerticalLayout();
				imgEditedCommFrame.setClassName("gridDataImgCommFrame");
				
				String editedComm = objFile.getEditedComment();
				if (editedComm.length() > 30) {
					editedComm = editedComm.substring(0,30) + "...";
				}
				Label imgEditedComm = new Label(editedComm);
				imgEditedCommFrame.add(imgEditedComm);
				
				VerticalLayout imgPhotographerCodeFrame = new VerticalLayout();
				imgPhotographerCodeFrame.setClassName("gridDataImgPCFrame");
				
				String photographerCode = objFile.getPhotographerCode();
				Label imgPhotographerCode = new Label(photographerCode);
				imgPhotographerCodeFrame.add(imgPhotographerCode);
				
				VerticalLayout imgTile = new VerticalLayout();
				imgTile.setClassName("gridDataCol");
				imgTile.add(imgFrame, imgEditedCommFrame, imgPhotographerCodeFrame);
				hlRow.add(imgTile);
				
				//Div imgDiv = new Div();
				//imgDiv.add(imgA);
				//imgDiv.setClassName("gridImageContainer");
				//hlRow.add(imgDiv);
				
				if (i % maxCol == 0) {
					hlRow.setClassName("gridDataRow");
					gridParent.add(hlRow);
					hlRow = new HorizontalLayout();
					
					rowCount++;
					if (rowCount >= maxRow) {
						break;
					}
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
		// Get search criteria from UI. 
		String srcPhotoName = txtPhotoName.getValue();
		
		/* Store search criteria to session. */
		session.setAttribute("srcPhotoName", srcPhotoName);
		session.setAttribute("srcPageNo", 1);
		
		// Reset alert message.
		message.setText("");
		divMessage.setVisible(false);
		this.remove(gridParent);
		
		// Get Data.
		int totalRecord = FileBC.getTotalRecord(srcPhotoName, "");
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		lstImageFile = FileBC.searchImage(1, pageSize, srcPhotoName, "");
		initImageGrid(1, totalRecord, lstImageFile);
		
		
	}
	
	public void getRecord(String pageNo) {
		// Get search criteria from Session.
		String srcPhotoName = (String) session.getAttribute("srcPhotoName");
		//int srcPageNo = (int) session.getAttribute("srcPageNo");
		int srcPageNo = Integer.parseInt(pageNo);
		session.setAttribute("srcPageNo", srcPageNo);
		System.out.println(pageNo);
		
		int totalRecord = FileBC.getTotalRecord(srcPhotoName, "");
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		
		lstImageFile = FileBC.searchImage(srcPageNo, pageSize, srcPhotoName, "");
		
		this.remove(gridParent);
		initImageGrid(srcPageNo, totalRecord, lstImageFile);
	}
}
