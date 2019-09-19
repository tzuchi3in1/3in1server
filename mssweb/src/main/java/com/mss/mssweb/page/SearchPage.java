package com.mss.mssweb.page;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.IntStream;

//import javax.servlet.http.HttpSession;

import com.mss.mssweb.RouterLayout;
//import com.mss.mssweb.helper.dbservice.*;
import com.mss.mssweb.bc.*;
import com.mss.mssweb.dto.ImageFile;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
//import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
//import com.vaadin.flow.component.html.Span;
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

//import de.kaesdingeling.hybridmenu.components.Notification;
//import de.kaesdingeling.hybridmenu.components.NotificationCenter;

@StyleSheet("../main.css") // Relative to Servlet URL
@Route(value = "search", layout = RouterLayout.class)
public class SearchPage extends VerticalLayout {
	private static final long serialVersionUID = 5L;
		
	VaadinSession session = VaadinSession.getCurrent();
	private Grid<Integer> grid = new Grid<>();
	private Label message;
	private Div divMessage;
	private TextField txtSearchBox;
	private TextField txtPhotoName;
	private TextField txtEventName; 
	private TextField txtPlaceName; 
	private DatePicker startDatePicker;
	private DatePicker endDatePicker;
	private TextField txtPhotographerName; 
	private TextField txtPhotographerCode;
	private TextField txtCommOri;
	private TextField txtKeyword;
	private ComboBox<String> cbMissionCode = new ComboBox<String>("Mission / 志业");
	private Checkbox chkEventDate;
	
	private VerticalLayout gridParent = new VerticalLayout();
	private int pageSize = 10;
	
	// JB(20190620): For keeping search criteria.
	private int srcGridPageNo = 1;
	private String srcSearchText = "";
	private Date srcEventDateFrom = null;
	private Date srcEventDateTo = null;
	
	/*
	private String srcFileName = "";
	private String srcEventName = "";
	private String srcPlaceName = "";
	private String srcCommOriginal = "";
	private String srcPhotographerCode = "";
	private String srcPhotographerName = "";
	private String srcMissionCode = "";
	private String srcKeyword = "";
	private Date srcEventDateFrom = null;
	private Date srcEventDateTo = null;
	*/
	
	public SearchPage() {
		add(new H2("Search Photo"));  
		
		//message = new Label("");
		
		//divMessage = new Div();
		//divMessage.setClassName("alertMessage");
		//divMessage.add(message);
		//add(divMessage);
		
		/*########## Search Criteria Start ##########*/
		/* Row #1 */
		txtSearchBox = new TextField("Search","","search by file name, event, place name etc."); 	
		txtSearchBox.setClassName("srcTextbox1");
		
		Button btnSearch = new Button("Search", event -> searchRecord());
		btnSearch.setClassName("srcButton1");
		btnSearch.setIcon(VaadinIcon.SEARCH.create());
		
		Button btnReset = new Button("Reset", event -> resetSearch());
		btnReset.setClassName("srcButton1");
		btnReset.setIcon(VaadinIcon.REFRESH.create());
		
		HorizontalLayout hlRow1 = new HorizontalLayout(txtSearchBox,btnSearch, btnReset);
		
		/* Row #2 */
		Label lblEventDate = new Label();
		lblEventDate.setText("Date / 日期");
		lblEventDate.setClassName("srcLabel1");
		
		chkEventDate = new Checkbox();
		chkEventDate.addValueChangeListener(event -> {
			if (event.getValue()) {
				startDatePicker.setEnabled(true);
				endDatePicker.setEnabled(true);
			}
			else {
				startDatePicker.setEnabled(false);
				endDatePicker.setEnabled(false);
			}
		});
		
		HorizontalLayout hlRow2 = new HorizontalLayout(lblEventDate, chkEventDate);
		//hlRow2.setClassName("srcHorizontalLayout1");
		
		/* Row #3 */
		startDatePicker = new DatePicker();
		//startDatePicker.setLabel("Date / 日期");
		startDatePicker.setPlaceholder("event date from");
		startDatePicker.setEnabled(false);
		
		endDatePicker = new DatePicker();
		//endDatePicker.setLabel("To");
		endDatePicker.setPlaceholder("event date to");
		endDatePicker.setEnabled(false);
		
		startDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    LocalDate endDate = endDatePicker.getValue();
		    if (selectedDate != null) {
		        endDatePicker.setMin(selectedDate);
		        if (endDate == null) {
		            endDatePicker.setOpened(true);
		        } 
		    } else {
		        endDatePicker.setMin(null);
		    }
		});
		
		endDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    //LocalDate startDate = startDatePicker.getValue();
		    if (selectedDate != null) {
		        startDatePicker.setMax(selectedDate);
		    } else {
		        startDatePicker.setMax(null);
		    }
		});
		
		HorizontalLayout hlRow3 = new HorizontalLayout(startDatePicker, endDatePicker);
		hlRow3.setClassName("srcHorizontalLayout1");
		
		add(hlRow1, hlRow2, hlRow3);
		/*########## Search Criteria End ##########*/
		
		
		/* Row #2 */
		/*
		txtPlaceName = new TextField("Place / 地点","","search by place");
		txtPlaceName.setClassName("srcTextbox1");
		
		cbMissionCode = new ComboBox<String>("Mission / 志业"); 
		HashMap<String,String> missionCodeList = FileBC.GetMissionCode();
		cbMissionCode.setItemLabelGenerator((i) -> i);
		cbMissionCode.setItems(missionCodeList.values());
		cbMissionCode.setClassName("srcComboBox1");
		cbMissionCode.setPlaceholder("search by mission code");
		
		HorizontalLayout hlRow2 = new HorizontalLayout(txtPlaceName, cbMissionCode);
		hlRow2.setClassName("srcHorizontalLayout1");
		*/
		
		/* Row #3 */
		/*
		startDatePicker = new DatePicker();
		startDatePicker.setLabel("Date / 日期");
		startDatePicker.setPlaceholder("event date from");
		
		endDatePicker = new DatePicker();
		endDatePicker.setLabel("To");
		endDatePicker.setPlaceholder("event date to");
		
		startDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    LocalDate endDate = endDatePicker.getValue();
		    if (selectedDate != null) {
		        endDatePicker.setMin(selectedDate);
		        if (endDate == null) {
		            endDatePicker.setOpened(true);
		            //message.setText("Select the ending date");
		        } 
		        /*else {
		            message.setText(
		                    "Selected period:\nFrom " + selectedDate.toString()
		                            + " to " + endDate.toString());
		        }*/
		/*
		    } else {
		        endDatePicker.setMin(null);
		        //message.setText("Select the starting date");
		    }
		});
		
		endDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    //LocalDate startDate = startDatePicker.getValue();
		    if (selectedDate != null) {
		        startDatePicker.setMax(selectedDate);
		        /*
		        if (startDate != null) {
		            message.setText(
		                    "Selected period:\nFrom " + startDate.toString()
		                            + " to " + selectedDate.toString());
		        } else {
		            message.setText("Select the starting date");
		        }
		        */
		/*
		    } else {
		        startDatePicker.setMax(null);
		        /*
		        if (startDate != null) {
		            message.setText("Select the ending date");
		        } else {
		            message.setText("No date is selected");
		        }
		        */
		/*
		    }
		});
		
		txtPhotographerName = new TextField("Photographer / 图像员","","search by photographer name");
		txtPhotographerName.setClassName("srcTextbox1");
		
		HorizontalLayout hlRow3 = new HorizontalLayout(startDatePicker, endDatePicker, txtPhotographerName);
		hlRow3.setClassName("srcHorizontalLayout1");
		*/
		
		/* Row #4 */
		/*
		txtPhotographerCode = new TextField("Photographer Code","","search by photographer code");
		txtPhotographerCode.setClassName("srcTextbox1");
		
		txtCommOri = new TextField("Captions / 图解","","search by original comment");
		txtCommOri.setClassName("srcTextbox1");
		
		HorizontalLayout hlRow4 = new HorizontalLayout(txtPhotographerCode, txtCommOri);
		hlRow4.setClassName("srcHorizontalLayout1");
		*/
		
		/* Row #5 */
		/*
		txtKeyword = new TextField("Tag / 类别","","search by keyword");
		txtKeyword.setClassName("srcTextbox1");
		
		HorizontalLayout hlRow5 = new HorizontalLayout(txtKeyword);
		hlRow5.setClassName("srcHorizontalLayout1");
		*/
		
		//ComboBox<String> cbCategory = new ComboBox<String>("Category"); 
		//HashMap<String,String> categoryList = FileBC.getCategory("");
		//cbCategory.setItemLabelGenerator((i) -> i);
		//cbCategory.setItems("Recycle","Donation","Disaster Recovery");
		//cbCategory.setItems(categoryList.values());
		//cbCategory.setWidth("400px");
		//HorizontalLayout hlRow3 = new HorizontalLayout(cbCategory); 
		
		 
		
		// stop here
		//Label lblSpacing1 = new Label("");
		//lblSpacing1.setWidth("720px"); 
		
		//HorizontalLayout hlRow4 = new HorizontalLayout(lblSpacing1, btnSearch);
		 
		//add(hlRow1, hlRow2, hlRow3, hlRow4, hlRow5);
		
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
	
	/*
	public SearchPage_bk() {
		add(new H2("Search Photo"));  
		*/
		//message = new Label("");
		
		//divMessage = new Div();
		//divMessage.setClassName("alertMessage");
		//divMessage.add(message);
		//add(divMessage);
		
		/*########## Search Criteria Start ##########*/
		/* Row #1 */
		/*
		txtPhotoName = new TextField("File / 文件","","search by file name"); 	
		txtPhotoName.setClassName("srcTextbox1");
		
		txtEventName = new TextField("Event / 活动名称","","search by event");
		txtEventName.setClassName("srcTextbox1");
		
		Button btnSearch = new Button("Search", event -> searchRecord());
		btnSearch.setClassName("srcButton1");
		
		Button btnReset = new Button("Reset", event -> resetSearch());
		btnReset.setClassName("srcButton1");
		
		HorizontalLayout hlRow1 = new HorizontalLayout(txtPhotoName,txtEventName, btnSearch, btnReset);
		*/
		
		/* Row #2 */
		/*
		txtPlaceName = new TextField("Place / 地点","","search by place");
		txtPlaceName.setClassName("srcTextbox1");
		
		cbMissionCode = new ComboBox<String>("Mission / 志业"); 
		HashMap<String,String> missionCodeList = FileBC.GetMissionCode();
		cbMissionCode.setItemLabelGenerator((i) -> i);
		cbMissionCode.setItems(missionCodeList.values());
		cbMissionCode.setClassName("srcComboBox1");
		cbMissionCode.setPlaceholder("search by mission code");
		
		HorizontalLayout hlRow2 = new HorizontalLayout(txtPlaceName, cbMissionCode);
		hlRow2.setClassName("srcHorizontalLayout1");
		*/
		
		/* Row #3 */
		/*
		startDatePicker = new DatePicker();
		startDatePicker.setLabel("Date / 日期");
		startDatePicker.setPlaceholder("event date from");
		
		endDatePicker = new DatePicker();
		endDatePicker.setLabel("To");
		endDatePicker.setPlaceholder("event date to");
		
		startDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    LocalDate endDate = endDatePicker.getValue();
		    if (selectedDate != null) {
		        endDatePicker.setMin(selectedDate);
		        if (endDate == null) {
		            endDatePicker.setOpened(true);
		            //message.setText("Select the ending date");
		        } 
		        /*else {
		            message.setText(
		                    "Selected period:\nFrom " + selectedDate.toString()
		                            + " to " + endDate.toString());
		        }*/
		/*
		    } else {
		        endDatePicker.setMin(null);
		        //message.setText("Select the starting date");
		    }
		});
		
		endDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    //LocalDate startDate = startDatePicker.getValue();
		    if (selectedDate != null) {
		        startDatePicker.setMax(selectedDate);
		        /*
		        if (startDate != null) {
		            message.setText(
		                    "Selected period:\nFrom " + startDate.toString()
		                            + " to " + selectedDate.toString());
		        } else {
		            message.setText("Select the starting date");
		        }
		        */
		/*
		    } else {
		        startDatePicker.setMax(null);
		        /*
		        if (startDate != null) {
		            message.setText("Select the ending date");
		        } else {
		            message.setText("No date is selected");
		        }
		        */
		/*
		    }
		});
		
		txtPhotographerName = new TextField("Photographer / 图像员","","search by photographer name");
		txtPhotographerName.setClassName("srcTextbox1");
		
		HorizontalLayout hlRow3 = new HorizontalLayout(startDatePicker, endDatePicker, txtPhotographerName);
		hlRow3.setClassName("srcHorizontalLayout1");
		*/
		
		/* Row #4 */
		/*
		txtPhotographerCode = new TextField("Photographer Code","","search by photographer code");
		txtPhotographerCode.setClassName("srcTextbox1");
		
		txtCommOri = new TextField("Captions / 图解","","search by original comment");
		txtCommOri.setClassName("srcTextbox1");
		
		HorizontalLayout hlRow4 = new HorizontalLayout(txtPhotographerCode, txtCommOri);
		hlRow4.setClassName("srcHorizontalLayout1");
		*/
		
		/* Row #5 */
		/*
		txtKeyword = new TextField("Tag / 类别","","search by keyword");
		txtKeyword.setClassName("srcTextbox1");
		
		HorizontalLayout hlRow5 = new HorizontalLayout(txtKeyword);
		hlRow5.setClassName("srcHorizontalLayout1");
		*/
		
		//ComboBox<String> cbCategory = new ComboBox<String>("Category"); 
		//HashMap<String,String> categoryList = FileBC.getCategory("");
		//cbCategory.setItemLabelGenerator((i) -> i);
		//cbCategory.setItems("Recycle","Donation","Disaster Recovery");
		//cbCategory.setItems(categoryList.values());
		//cbCategory.setWidth("400px");
		//HorizontalLayout hlRow3 = new HorizontalLayout(cbCategory); 
		
		 
		
		// stop here
		//Label lblSpacing1 = new Label("");
		//lblSpacing1.setWidth("720px"); 
		
		//HorizontalLayout hlRow4 = new HorizontalLayout(lblSpacing1, btnSearch);
		 
		//add(hlRow1, hlRow2, hlRow3, hlRow4, hlRow5);
		
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
/*	
}
*/
	
	public void initImageGrid(int currentPageNo, int totalRecord, ArrayList<ImageFile> lstImageFile) {
		
		int maxCol = 5;
		//int maxRecords = lstImageFile.size();
		int maxRow = 1;
		//String rootPath = "D:\\Development\\eclipse-workspace\\";
		//String rootPath = "D:\\Development\\eclipse-workspace\\files\\";
		//String childPath = "files\\P001";
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
				//imgA.setHref("javascript:window.open('fileDetailPage/" + objFile.getFileName() + "')");
				imgA.setHref("fileDetailPage/" + objFile.getFileName());
				//imgA.setTarget("_blank");
				
				VerticalLayout imgFrame = new VerticalLayout();
				imgFrame.setClassName("gridDataImgFrame");
				imgFrame.add(imgA);
				
				VerticalLayout imgEditedCommFrame = new VerticalLayout();
				imgEditedCommFrame.setClassName("gridDataImgCommFrame");
				
				//String editedComm = objFile.getEditedComment();
				String editedComm = objFile.getOriginalComment();
				if (editedComm.length() > 30) {
					editedComm = editedComm.substring(0,30) + "...";
				}
				Label imgEditedComm = new Label(editedComm);
				imgEditedCommFrame.add(imgEditedComm);
				
				VerticalLayout imgPhotographerCodeFrame = new VerticalLayout();
				imgPhotographerCodeFrame.setClassName("gridDataImgPCFrame");
				
				//String photographerCode = objFile.getPhotographerCode();
				String photographerCode = objFile.getPhotographerName();
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
	
	public String getMissionCode(String missionDescription) {
		String strMissionCode = "";
		if (missionDescription != null) {
			missionDescription = missionDescription.toUpperCase();
			//missionDescription = missionDescription.toUpperCase().substring(0, missionDescription.indexOf('/'));
			//System.out.println("missionDescription: " + missionDescription);
			if (missionDescription.equals("CHARITY/慈善")) {
				strMissionCode = "CS";
			}
			else if(missionDescription.equals("MEDICINE/醫療")) {
				strMissionCode = "YL";
			}
			else if(missionDescription.equals("EDUCATION/教育")) {
				strMissionCode = "JY";
			}
			else if(missionDescription.equals("HUMANISTIC CULTURE/人文")) {
				strMissionCode = "RW";
			}
			else {
				strMissionCode = "";
			}
		}
		else {
			strMissionCode = "";
		}
		return strMissionCode;
	}
	
	public void resetSearch() {
		// Reset search criteria.
		txtSearchBox.clear();
		startDatePicker.clear();
		endDatePicker.clear();
		chkEventDate.clear();
		
		/*
		txtPhotoName.clear();
		txtEventName.clear();
		txtPlaceName.clear();
		cbMissionCode.clear();
		startDatePicker.clear();
		endDatePicker.clear();
		txtPhotographerName.clear();
		txtPhotographerCode.clear();
		txtCommOri.clear();
		txtKeyword.clear();
		*/
	}
	
	public void searchRecord() {
		// Get search criteria from UI. 
		srcSearchText = txtSearchBox.getValue();
		
		//String srcPhotoName = txtPhotoName.getValue();
		/*
		srcFileName = txtPhotoName.getValue();
		srcPlaceName = txtPlaceName.getValue();
		srcEventName = txtEventName.getValue();
		srcPhotographerName = txtPhotographerName.getValue();
		srcPhotographerCode = txtPhotographerCode.getValue();
		srcCommOriginal = txtCommOri.getValue();
		srcKeyword = txtKeyword.getValue();
		srcMissionCode = getMissionCode(cbMissionCode.getValue());
		*/
		
		srcEventDateFrom = null;
		if (startDatePicker.getValue() != null) {
			// This is to convert LocalDate to Date.
			srcEventDateFrom = Date.from(startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		
		srcEventDateTo = null;
		if (endDatePicker.getValue() != null) {
			// This is to convert LocalDate to Date.
			srcEventDateTo = Date.from(endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		
		/* Store search criteria to session. */
		//session.setAttribute("srcPhotoName", srcFileName);
		//session.setAttribute("srcPageNo", 1);
		srcGridPageNo = 1;
		
		// Reset alert message.
		//message.setText("");
		//divMessage.setVisible(false);
		this.remove(gridParent);
		gridParent = new VerticalLayout();
		
		// Get Data.
		int totalRecord = FileBC.getTotalRecordMin(srcSearchText, srcEventDateFrom, srcEventDateTo);
		
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		lstImageFile = FileBC.searchImageMin(1, pageSize, srcSearchText, srcEventDateFrom, srcEventDateTo);

		/*
		int totalRecord = FileBC.getTotalRecord(srcFileName, srcEventName, 
												srcPlaceName, srcCommOriginal, srcPhotographerCode, srcPhotographerName, 
												srcKeyword, srcMissionCode, srcEventDateFrom, srcEventDateTo);
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		lstImageFile = FileBC.searchImage(1, pageSize, srcFileName, srcEventName, 
										  srcPlaceName, srcCommOriginal, srcPhotographerCode, srcPhotographerName, 
										  srcKeyword, srcMissionCode, srcEventDateFrom, srcEventDateTo);
										  */
		initImageGrid(1, totalRecord, lstImageFile);
		
		
	}
	
	public void getRecord(String pageNo) {
		// Get search criteria from Session.
		//String srcPhotoName = (String) session.getAttribute("srcPhotoName");
		//int srcPageNo = (int) session.getAttribute("srcPageNo");
		//int srcPageNo = Integer.parseInt(pageNo);
		
		//session.setAttribute("srcPageNo", srcPageNo);
		//System.out.println(pageNo);
		//System.out.println("********** srcFileName: " + srcFileName);
		
		/*
		int totalRecord = FileBC.getTotalRecord(srcFileName, srcEventName, 
												srcPlaceName, srcCommOriginal, srcPhotographerCode, srcPhotographerName, 
												srcKeyword, srcMissionCode, srcEventDateFrom, srcEventDateTo);
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		
		//lstImageFile = FileBC.searchImage(srcGridPageNo, pageSize, srcFileName, "", "", "", "", "", "", "");
		lstImageFile = FileBC.searchImage(srcGridPageNo, pageSize, srcFileName, srcEventName, 
										  srcPlaceName, srcCommOriginal, srcPhotographerCode, srcPhotographerName, 
										  srcKeyword, srcMissionCode, srcEventDateFrom, srcEventDateTo);
										  */
		
		srcGridPageNo  = Integer.parseInt(pageNo);
		
		int totalRecord = FileBC.getTotalRecordMin(srcSearchText, srcEventDateFrom, srcEventDateTo);
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();

		lstImageFile = FileBC.searchImageMin(srcGridPageNo, pageSize, srcSearchText, srcEventDateFrom, srcEventDateTo);
		
		this.remove(gridParent);
		initImageGrid(srcGridPageNo, totalRecord, lstImageFile);
	}
}
