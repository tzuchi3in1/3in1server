package com.mss.mssweb.page;

import java.util.ArrayList;
//import java.util.Set;

import com.mss.mssweb.RouterLayout;
import com.mss.mssweb.bc.FileBC;
import com.mss.mssweb.dto.Category;
import com.mss.mssweb.dto.ImageFile;
//import com.mss.mssweb.page.SelectCategory.Person;
//import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
//import com.vaadin.flow.component.dialog.Dialog;
//import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextArea;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
//import com.vaadin.flow.server.VaadinSession;

//import de.kaesdingeling.hybridmenu.components.HMLabel;

//import org.tzuchi.syslib.metadata.*;

@StyleSheet("../file_detail.css") // Relative to Servlet URL
@Route(value = "fileDetailPage", layout = RouterLayout.class)
public class FileDetailPage extends VerticalLayout implements HasUrlParameter<String> { 
	private static final long serialVersionUID = 7L;
	
	//private String fileName = "";
	//private TreeGrid<Category> grid = new TreeGrid<>();
	//private TextArea txtCatSelected = new TextArea();
	private Label lblFileName = new Label();
	//private TextField txtEditedComm = new TextField();
	//private Dialog diMsgBox = new Dialog();
	
	public FileDetailPage() {
		add(new Label("Detail Page"));
	}

	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		// TODO Auto-generated method stub
		//fileName = parameter;
		//getImageFile(parameter);
		//getImageFileEdit(parameter);
		getImageFileReadOnly(parameter);
	}
	
	// To be removed.
	/*
	public void getImageFile(String fileName) {
		add(new Label("File Name: " + fileName));
		
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		lstImageFile = FileBC.searchImage(1, 1, fileName, "");
		
		if (lstImageFile.size() > 0) {
			
			ImageFile objFile = lstImageFile.get(0);
			InputStreamFactory isf = objFile;
			StreamResource imgStream = new StreamResource(objFile.getFileName(), isf);
			Image imgFile  = new Image(imgStream, "");
			imgFile.setClassName("fileDetailsImage");
			
			VerticalLayout imgFrame = new VerticalLayout();
			imgFrame.setClassName("fileDetailsImgFrame");
			imgFrame.add(imgFile);
			
			String editedComm = objFile.getEditedComment();
			
			add(imgFrame, new Label(editedComm));
			
			
		}
	}
	*/
	
	// Keep for future.
	/*
	public void getImageFileEdit(String fileName) {
		// stop here.
		// JB(20190604): ToDo:
		// 
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		//lstImageFile = FileBC.searchImage(1, 1, fileName, "");
		lstImageFile = FileBC.getImageDetails(fileName);
		
		if (lstImageFile.size() > 0) {
			
			ImageFile objFile = lstImageFile.get(0);
			InputStreamFactory isf = objFile;
			StreamResource imgStream = new StreamResource(objFile.getFileName(), isf);
			Image imgFile  = new Image(imgStream, "");
			imgFile.setClassName("fileDetailsEditImage");
			
			VerticalLayout imgFrame = new VerticalLayout();
			imgFrame.setClassName("fileDetailsEditImgFrame");
			imgFrame.add(imgFile);
			
			//***** Read Only Panel Start *****
			//String editedComm = objFile.getEditedComment();
			objFile.fetchMetadata();
			
			// File Name.
			Label lblFileNameCaption = new Label("File Name");
			lblFileNameCaption.setClassName("fileDetailsLabelCaption");
			lblFileName = new Label(": " + objFile.getFileName());
			HorizontalLayout hlFileName = new HorizontalLayout(lblFileNameCaption, lblFileName);

			// Title.
			Label lblTitleCaption = new Label("Title");
			lblTitleCaption.setClassName("fileDetailsLabelCaption");
			Label lblTitle = new Label(": " + objFile.getFileTitle());
			HorizontalLayout hlFileTitle = new HorizontalLayout(lblTitleCaption, lblTitle);
			
			// Subject
			Label lblSubjectCaption = new Label("Subject");
			lblSubjectCaption.setClassName("fileDetailsLabelCaption");
			Label lblSubject = new Label(": " + objFile.getFileSubject());
			HorizontalLayout hlFileSubject = new HorizontalLayout(lblSubjectCaption, lblSubject);
			
			// Event name
			Label lblEventCaption = new Label("Event");
			lblEventCaption.setClassName("fileDetailsLabelCaption");
			Label lblEvent = new Label(": " + objFile.getEventName());
			HorizontalLayout hlFileEvent = new HorizontalLayout(lblEventCaption, lblEvent);
			
			// Event name
			Label lblEventDateCaption = new Label("Event Date");
			lblEventDateCaption.setClassName("fileDetailsLabelCaption");
			Label lblEventDate = new Label(": " + objFile.getDateFromToString());
			HorizontalLayout hlFileEventDate = new HorizontalLayout(lblEventDateCaption, lblEventDate);
			
			// Country
			Label lblCountryCaption = new Label("Country");
			lblCountryCaption.setClassName("fileDetailsLabelCaption");
			Label lblCountry = new Label(": " + objFile.getFileCountry());
			HorizontalLayout hlFileCountry = new HorizontalLayout(lblCountryCaption, lblCountry);
			
			// Original Comment
			Label lblCommOriCaption = new Label("Original Comment");
			lblCommOriCaption.setClassName("fileDetailsLabelCaption");
			Label lblCommOri = new Label(": " + objFile.getOriginalComment());
			HorizontalLayout hlFileCommOri = new HorizontalLayout(lblCommOriCaption, lblCommOri);
			
			// Photographer Name
			Label lblPhotographerNameCaption = new Label("Photographer Name");
			lblPhotographerNameCaption.setClassName("fileDetailsLabelCaption");
			Label lblPhotographerName = new Label(": " + objFile.getPhotographerName());
			HorizontalLayout hlPhotographerName = new HorizontalLayout(lblPhotographerNameCaption, lblPhotographerName);
			
			VerticalLayout vlRightPanel = new VerticalLayout(hlFileName, hlFileTitle, hlFileSubject, hlFileEvent, hlFileEventDate, hlFileCountry, hlFileCommOri, hlPhotographerName);
			
			HorizontalLayout row1 = new HorizontalLayout(imgFrame, vlRightPanel);
			row1.setClassName("fileDetailsEditRow");
			//***** Read Only Panel End *****
			
			
			
			//***** Bottom Left Panel Start *****
			// Edited Comment.
			txtEditedComm = new TextField("Edited Comment", objFile.getEditedComment(), "edited comment");
			txtEditedComm.setClassName("fileDetailsEditTextBox");
			
			//TextField txtPhotographerName = new TextField("Photographer Name", objFile.getPhotographerCode(), "photographer name");
			//txtPhotographerName.setClassName("fileDetailsEditTextBox");
			
			// Category Caption
			Label lblCategory = new Label("Category");
			lblCategory.setClassName("fileDetailsEditLabel");
			
			// Category Tree Grid
			ArrayList<Category> alCategory = FileBC.getAllCategory();
			//System.out.println("Parent Code: " + alCategory.get(1).getParentCategory().getCode());
			//System.out.println("Parent Description: " + alCategory.get(1).getParentCategory().getDescription());
			
			//TreeGrid<Category> grid = new TreeGrid<>();
			grid.addHierarchyColumn(Category::getDescription).setHeader("Category").setSortable(false);
			alCategory.forEach(p -> grid.getTreeData().addItem(p.getParentCategory(), p));
			grid.setSelectionMode(SelectionMode.MULTI);
			GridSelectionModel<Category> sel = grid.getSelectionModel();
			//sel.select(alCategory.get(1));
			//grid.expand(alCategory.get(0));
			//grid.expand(alCategory.get(1));
			
			// Loop through all category to set the selected category.
			//System.out.println("********* START ***********");
			ArrayList<Category> alImageCat = objFile.getCategory();
			for (int imageCatIndex = 0; imageCatIndex < alImageCat.size(); imageCatIndex++) {
				//System.out.println("imageCatIndex: " + imageCatIndex);
				for (int allCatIndex = 0; allCatIndex < alCategory.size(); allCatIndex++) {
					
					System.out.print("alCategory: " + alCategory.get(allCatIndex).getCode());
					System.out.println("|alImageCat: " + alImageCat.get(imageCatIndex).getCode());
					
					if (alCategory.get(allCatIndex).getCode().equalsIgnoreCase(alImageCat.get(imageCatIndex).getCode())) {
						sel.select(alCategory.get(allCatIndex));
						System.out.println("sel.select: " + alCategory.get(allCatIndex).getCode());
						break;
					}
					
				}
			}
			//System.out.println("********** END **********");
			// Expand all items.
			grid.expand(alCategory);
			
			HorizontalLayout row2 = new HorizontalLayout(grid);
			row2.setClassName("fileDetailsEditRow2");
			//add(grid);
			
			// This does not work.
			//Category cat = new Category();
			//cat.setCode("110000");
			//cat.setDescription("教育组");
			//cat.setParentCode("100000");
			//sel.select(cat);
		
			//VerticalLayout vlEditLeftPanel = new VerticalLayout(grid);
			//vlEditLeftPanel.setClassName("fileDetailsEditPanel");
			//***** Bottom Left Panel End *****
			
			
			//***** Bottom Right Panel Start *****
			//Button btnSelectCategory = new Button("Select", event -> selectCategory());
			//txtCatSelected.setReadOnly(true);
			//TextArea txtCatSelected = new TextArea();
			
			//VerticalLayout vlEditRightPanel = new VerticalLayout(txtPhotographerName, btnSelectCategory, txtCatSelected);
			//vlEditRightPanel.setClassName("fileDetailsEditPanel");
			//***** Bottom Right Panel End *****
			
			
			
			//TextField txtTextBox2 = new TextField("Photographer Name", objFile.getPhotographerCode(), "photographer name");
			//txtTextBox2.setClassName("fileDetailsEditTextBox");
			
			//********** Set dialog Starts **********
			Dialog dialog = new Dialog();

			dialog.setCloseOnEsc(true);
			dialog.setCloseOnOutsideClick(true);
			dialog.setWidth("200px");
	
			//Label messageLabel = new Label();
	
			Button confirmButton = new Button("Yes", event -> {
			    //messageLabel.setText("Confirmed!");
			    dialog.close();
			    
			    if (isInputValid()) {
			    	saveImageDetails();
			    }
			});
			Button cancelButton = new Button("No", event -> {
			    //messageLabel.setText("Cancelled...");
			    dialog.close();
			});
			 
			HorizontalLayout hlDialogButtons = new HorizontalLayout();
			hlDialogButtons.add(confirmButton, cancelButton);
			hlDialogButtons.setWidth("100%");
			
			VerticalLayout vlDialog = new VerticalLayout();
			vlDialog.add(new Label("Are you sure you want to save?"), hlDialogButtons);
			vlDialog.setWidth("100%");
			
			dialog.add(vlDialog);
			
			Button btnSave = new Button("Save", event -> dialog.open());
			
			//Button openDialog = new Button("Alert", event -> dialog.open());
			//add(openDialog);
			//********** Set dialog Ends **********
			
			add(row1, txtEditedComm, lblCategory, row2, btnSave);
			
			VaadinSession vaadinSession = VaadinSession.getCurrent();
			String sesFileName = "agent47_" + lblFileName.getText();
			vaadinSession.setAttribute(sesFileName, objFile.getFileName());
			//httpSession.setAttribute("user", user);

		}
		
		
	}
	*/
	
	public void getImageFileReadOnly(String fileName) {
		// stop here.
		// JB(20190604): ToDo:
		// 
		ArrayList<ImageFile> lstImageFile = new ArrayList<ImageFile>();
		//lstImageFile = FileBC.searchImage(1, 1, fileName, "");
		lstImageFile = FileBC.getImageDetails(fileName);
		
		if (lstImageFile.size() > 0) {
			
			ImageFile objFile = lstImageFile.get(0);
			objFile.setFileSizeType(1); // Set to original size.
			InputStreamFactory isf = objFile;
			StreamResource imgStream = new StreamResource(objFile.getFileName(), isf);
			Image imgFile  = new Image(imgStream, "");
			imgFile.setClassName("fileDetailsEditImage");
			
			VerticalLayout imgFrame = new VerticalLayout();
			imgFrame.setClassName("fileDetailsEditImgFrame");
			imgFrame.add(imgFile);
			
			//***** Read Only Panel Start *****
			//String editedComm = objFile.getEditedComment();
			// JB(20190619): Get data from DB instead.
			//objFile.fetchMetadata();
			
			// File Name.
			Label lblFileNameCaption = new Label("File / 文件");
			lblFileNameCaption.setClassName("fileDetailsLabelCaption");
			lblFileName = new Label(": " + objFile.getFileName());
			HorizontalLayout hlFileName = new HorizontalLayout(lblFileNameCaption, lblFileName);

			// Title.
			//Label lblTitleCaption = new Label("Title");
			//lblTitleCaption.setClassName("fileDetailsLabelCaption");
			//Label lblTitle = new Label(": " + objFile.getFileTitle());
			//HorizontalLayout hlFileTitle = new HorizontalLayout(lblTitleCaption, lblTitle);
			
			// Subject
			//Label lblSubjectCaption = new Label("Subject");
			//lblSubjectCaption.setClassName("fileDetailsLabelCaption");
			//Label lblSubject = new Label(": " + objFile.getFileSubject());
			//HorizontalLayout hlFileSubject = new HorizontalLayout(lblSubjectCaption, lblSubject);
			
			// Event
			Label lblEventCaption = new Label("Event / 活动名称");
			lblEventCaption.setClassName("fileDetailsLabelCaption");
			Label lblEvent = new Label(": " + objFile.getEventName());
			HorizontalLayout hlFileEvent = new HorizontalLayout(lblEventCaption, lblEvent);
			
			// Place.
			Label lblPlaceCaption = new Label("Place / 地点");
			lblPlaceCaption.setClassName("fileDetailsLabelCaption");
			Label lblPlace = new Label(": " + objFile.getFilePlace());
			HorizontalLayout hlFilePlace = new HorizontalLayout(lblPlaceCaption, lblPlace);
			
			// Mission.
			Label lblMissionCaption = new Label("Mission / 志业");
			lblMissionCaption.setClassName("fileDetailsLabelCaption");
			Label lblMission = new Label(": " + objFile.getMissionDescription());
			HorizontalLayout hlFileMission = new HorizontalLayout(lblMissionCaption, lblMission);
			
			// Event Date
			Label lblEventDateCaption = new Label("Date / 日期");
			lblEventDateCaption.setClassName("fileDetailsLabelCaption");
			Label lblEventDate = new Label(": " + objFile.getDateFromToString());
			HorizontalLayout hlFileEventDate = new HorizontalLayout(lblEventDateCaption, lblEventDate);
			
			// Country
			//Label lblCountryCaption = new Label("Country");
			//lblCountryCaption.setClassName("fileDetailsLabelCaption");
			//Label lblCountry = new Label(": " + objFile.getFileCountry());
			//HorizontalLayout hlFileCountry = new HorizontalLayout(lblCountryCaption, lblCountry);
			
			// Photographer Name
			Label lblPhotographerNameCaption = new Label("Photographer / 图像员");
			lblPhotographerNameCaption.setClassName("fileDetailsLabelCaption");
			Label lblPhotographerName = new Label(": " + objFile.getPhotographerName());
			// Photographer Code.
			Label lblPhotographerCodeCaption = new Label("Code");
			lblPhotographerCodeCaption.setClassName("fileDetailsLabelCaptionShort");
			Label lblPhotographerCode = new Label(": " + objFile.getPhotographerCode());
			HorizontalLayout hlPhotographerName = new HorizontalLayout(lblPhotographerNameCaption, lblPhotographerName, lblPhotographerCodeCaption, lblPhotographerCode);
			
			// Remarks.
			Label lblRemarksCaption = new Label("Remarks / 备注");
			lblRemarksCaption.setClassName("fileDetailsLabelCaption");
			Label lblRemarks = new Label(": " + objFile.getRemarks());
			HorizontalLayout hlFileRemarks = new HorizontalLayout(lblRemarksCaption, lblRemarks);
			
			// Original Comment
			Label lblCommOriCaption = new Label("Captions / 图解");
			lblCommOriCaption.setClassName("fileDetailsLabelCaption");
			Label lblCommOri = new Label(": " + objFile.getOriginalComment());
			HorizontalLayout hlFileCommOri = new HorizontalLayout(lblCommOriCaption, lblCommOri);
			
			// Keyword
			Label lblKeywordCaption = new Label("Tag / 类别");
			lblKeywordCaption.setClassName("fileDetailsLabelCaption");
			Label lblKeyword = new Label(": " + objFile.getKeyword());
			HorizontalLayout hlFileKeyword = new HorizontalLayout(lblKeywordCaption, lblKeyword);
			
			
			VerticalLayout vlRightPanel = new VerticalLayout(hlFileName, hlFileEvent, hlFilePlace, hlFileMission, hlFileEventDate, hlPhotographerName, hlFileRemarks, hlFileCommOri, hlFileKeyword);
			
			HorizontalLayout row1 = new HorizontalLayout(imgFrame, vlRightPanel);
			row1.setClassName("fileDetailsEditRow");
			//***** Read Only Panel End *****
			
			//***** Bottom Panel Start *****
			Anchor downloadLink = new Anchor(imgStream, "Download");
			downloadLink.getElement().setAttribute("download", true);
			
			HorizontalLayout row2 = new HorizontalLayout(downloadLink);
			//***** Bottom Panel End *****
			
			add(row1, row2);
		}
	}
		
	protected boolean isInputValid() {
		boolean blnInputValid = true;
		
		// To validate input fields. For future enhancement.
		
		return blnInputValid;
	}
	
	// For future use.
	/*
	protected void saveImageDetails() {
		System.out.println("************** Start Save *************");
		
		GridSelectionModel<Category> sel = grid.getSelectionModel();
		Set<Category> setCategory = sel.getSelectedItems();
		
		ImageFile objFileSave = new ImageFile();
		
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		String sesFileName = "agent47_" + lblFileName.getText();
		String fileName = (String) vaadinSession.getAttribute(sesFileName);
		
		objFileSave.setFileName(fileName);
		objFileSave.setEditedComment(txtEditedComm.getValue());
		
		// Get selected category.
		if (setCategory.size() > 0) {
			setCategory.forEach(item -> 
				objFileSave.addCategory(item.getCode(), item.getDescription(), item.getParentCode())
			);
		}
		
		System.out.println("FileName: " + objFileSave.getFileName());
		System.out.println("EditedComment: " + objFileSave.getEditedComment());
		System.out.println("Category: " + objFileSave.getCategory().size());
	
		boolean saveImage = FileBC.saveImageDetails(objFileSave);
		
		Label lblMsgBoxMessage = new Label("");
		diMsgBox.setCloseOnEsc(true);
		diMsgBox.setCloseOnOutsideClick(true);
		diMsgBox.setWidth("200px");
		
		Button okButton = new Button("OK", event -> {
			diMsgBox.close();
		});
		
		HorizontalLayout hlDialogButtons = new HorizontalLayout();
		hlDialogButtons.add(okButton);
		hlDialogButtons.setWidth("100%");
		
		VerticalLayout vlDialog = new VerticalLayout();
		vlDialog.add(lblMsgBoxMessage, hlDialogButtons);
		vlDialog.setWidth("100%");
		
		diMsgBox.add(vlDialog);
		
		
		if (saveImage) {
			lblMsgBoxMessage.setText("Image details saved successfully.");
		}
		else
		{
			lblMsgBoxMessage.setText("Unable to save details.");
		}
		diMsgBox.open();
		
		System.out.println("saveImage: " + saveImage);
		
		System.out.println("************** End Save *************");
	}
	*/
	
	/*
	protected void selectCategory() {
		GridSelectionModel<Category> sel = grid.getSelectionModel();
		Set<Category> setCategory = sel.getSelectedItems();
		 
		String strCategory = "";
		txtCatSelected.setValue(""); // reset value.
		 
		if (setCategory.size() > 0) {
			setCategory.forEach(item -> txtCatSelected.setValue("-" + txtCatSelected.getValue() + item.getDescription() + "\n"));
		
			strCategory = txtCatSelected.getValue();
			txtCatSelected.setValue(strCategory.substring(0, strCategory.length() - 1));
		}
	}
	*/
	
	protected void selectCategory2() {
		//Grid<Category> catGrid = new Grid<>();
		
		
	}
	
	
}
