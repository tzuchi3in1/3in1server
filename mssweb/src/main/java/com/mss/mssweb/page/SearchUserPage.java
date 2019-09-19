package com.mss.mssweb.page;

import java.util.ArrayList;

import com.mss.mssweb.RouterLayout;
import com.mss.mssweb.dto.UserInfo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@StyleSheet("../main.css") // Relative to Servlet URL
@Route(value = "user", layout = RouterLayout.class)
public class SearchUserPage extends VerticalLayout {
	private static final long serialVersionUID = 10L;
	
	private TextField txtSearchBox;
	private String srcSearchText = "";
	private Grid<UserInfo> grid = new Grid<>();
	
	public SearchUserPage() {
		add(new H2("Search User"));
		
		InitializeControls();
	}
	
	protected void InitializeControls() {
		/*########## Search Criteria Start ##########*/
		/* Row #1 */
		txtSearchBox = new TextField("Search","","search user name or user id."); 	
		txtSearchBox.setClassName("srcTextbox1");
		
		Button btnSearch = new Button("Search", event -> searchRecord());
		btnSearch.setClassName("srcButton1");
		btnSearch.setIcon(VaadinIcon.SEARCH.create());
		
		Button btnReset = new Button("Reset", event -> resetSearch());
		btnReset.setClassName("srcButton1");
		btnReset.setIcon(VaadinIcon.REFRESH.create());
		
		Button btnAddUser = new Button("Add User", event -> resetSearch());
		btnAddUser.setClassName("srcButton1");
		btnAddUser.setIcon(VaadinIcon.USER.create());
		
		HorizontalLayout hlRow1 = new HorizontalLayout(txtSearchBox,btnSearch, btnReset, btnAddUser);
		
		add(hlRow1);
	}
	
	public void searchRecord() {
		// Get search criteria from UI. 
		srcSearchText = txtSearchBox.getValue();
		
		grid.addColumn(i -> i.getUserID()).setHeader("User Id");
		grid.addColumn(i -> i.getFullName()).setHeader("Full Name");
		grid.addColumn(i -> i.getUserType()).setHeader("Full Name");
		
		ArrayList<UserInfo> lstUserInfo = new ArrayList<UserInfo>();
		// stop here.
		grid.setItems(lstUserInfo);
		
		add(grid);
	}
	
	public void resetSearch() {
		// Reset search criteria.
		txtSearchBox.clear();
	}
}
