package com.mss.mssweb.page;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.SingleSelectionModel;

import com.mss.mssweb.RouterLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridSelectionModel;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.selection.SelectionModel;
import com.vaadin.flow.router.Route;

import org.tzuchi.syslib.metadata.*;

@StyleSheet("../main.css") // Relative to Servlet URL
@Route(value = "selectcategory", layout = RouterLayout.class)
public class SelectCategory extends VerticalLayout {
	private static final long serialVersionUID = 6L;
	
	private TreeGrid<Person> grid = new TreeGrid<>();
	//private Grid<Person> selectGrid = new Grid<>();
	private Label output = new Label("Selected: ");
	private TextField txtCategory = new TextField("Category","","category");
	
	public static class Person {
        private String name;
        private Person parent;

        public String getName() {
            return name;
        }

        public Person getParent() {
            return parent;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setParent(Person parent) {
            this.parent = parent;
        }

        public Person(String name, Person parent) {
            this.name = name;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return name;
        }

    }
	
	 public SelectCategory() {
		 
		 String rootPath = "D:\\Development\\eclipse-workspace\\files\\Test\\";
		 uo_tjx_metadata_tz meta = new uo_tjx_metadata_tz();
		 //meta.tzReadAll(new File(rootPath + "Test_Car1.jpg"));
		 meta.tzReadAll(new File(rootPath + "RW_KL20190602_hyx02_003.JPG"));
		 
	     System.out.println("meta.TZ_COMMENTS=" + meta.TZ_COMMENTS);
	     System.out.println("meta.TZ_EVENT=" + meta.TZ_EVENT);
	     System.out.println("meta.TZ_PERSON=" + meta.TZ_PERSON);
	     System.out.println("meta.TZ_KEYWORDS=" + meta.TZ_KEYWORDS);
	     System.out.println("meta.TZ_WIN_TITLE=" + meta.TZ_WIN_TITLE);
	     System.out.println("meta.TZ_WIN_SUBJECT=" + meta.TZ_WIN_SUBJECT);
	     
	     //String strComment = meta.TZ_COMMENTS;
	     String photographer_code = meta.TZ_PCODE;
	     
	     DateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     Date dbDate = new Date();
	     System.out.println("Date: " + dbDateFormat.format(dbDate));
		 
		 //TreeGrid<Person> grid = new TreeGrid<>();
		 /*grid.setItems(getRootItems(), item -> {
			    if ((item.getLevel() == 0 && item.getId() > 10)
			            || item.getLevel() > 1) {
			        return Collections.emptyList();
			    }
			    if (!childMap.containsKey(item)) {
			        childMap.put(item, createSubItems(81, item.getLevel() + 1));
			    }
			    return childMap.get(item);
			});
		 */
		 grid.addHierarchyColumn(Person::getName).setHeader("Hierarchy");
		 //grid.addColumn(Person::getParent).setHeader("Age");
		 
		 Person grandPa = new Person("grand pa", null);
         Person dad = new Person("dad", grandPa);
         Person son = new Person("son", dad);
         Person daughter = new Person("daughter", dad);
        
         Person grandPa2 = new Person("grand pa2", null);
         Person dad2 = new Person("dad2", grandPa2);
         Person son2 = new Person("son2", dad2);
         Person daughter2 = new Person("daughter2", dad2);
        
         List<Person> all = Arrays.asList(grandPa, dad, son, daughter, grandPa2, dad2, son2, daughter2);
         
		 all.forEach(p -> grid.getTreeData().addItem(p.getParent(), p));
		 
		 
		 //grid.addSelectionListener(listener);
		 
		 grid.setSelectionMode(SelectionMode.MULTI);
		 
		 
		 Button selectCat = new Button("Select", event -> getSelectedCategory());
		 
		 //selectGrid.addColumn(Person::getName).setHeader("Selected Category");
		 //selectGrid.setItems(all);
		 txtCategory.setWidth("400px");
		 txtCategory.setReadOnly(true);
		 
		 add(grid, selectCat, txtCategory);
		 
		 // Set dialog.
		 Dialog dialog = new Dialog();

		 dialog.setCloseOnEsc(true);
		 dialog.setCloseOnOutsideClick(true);
		 dialog.setWidth("200px");

		 Label messageLabel = new Label();

		 Button confirmButton = new Button("Yes", event -> {
		     messageLabel.setText("Confirmed!");
		     dialog.close();
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
		 
		 Button openDialog = new Button("Alert", event -> dialog.open());
		 add(openDialog);
		 
		 //HorizontalLayout row1 = new HorizontalLayout(grid, selectCat);
		 //add(row1);
		 //add(grid, output, selectCat);
	 }
	 
	 public void getSelectedCategory() {
		 GridSelectionModel<Person> sel = grid.getSelectionModel();
		 Set<Person> setPerson = sel.getSelectedItems();
		 String strCategory = "";
		 txtCategory.setValue(""); // reset value.
		 
		 if (setPerson.size() > 0) {
			 setPerson.forEach(item -> txtCategory.setValue(txtCategory.getValue() + item.getName() + ";"));
			 
			 strCategory = txtCategory.getValue();
			 txtCategory.setValue(strCategory.substring(0, strCategory.length() - 1));
		 }
		 
		 //txtCategory.setValue(strCategory);
		 
		 //setPerson.forEach(item -> output.setText(output.getText() + item.getName() + "|"));
		 
		 //if (setPerson.size() > 0) {
			// selectGrid.setItems(setPerson);
		 //}
	 }
	 
	 public List<Person> getRootItems() {
		Person grandPa = new Person("grand pa", null);
        Person dad = new Person("dad", grandPa);
        Person son = new Person("son", dad);
        Person daughter = new Person("daughter", dad);
        
        Person grandPa2 = new Person("grand pa2", null);
        Person dad2 = new Person("dad2", grandPa2);
        Person son2 = new Person("son2", dad2);
        Person daughter2 = new Person("daughter2", dad2);
        
        List<Person> all = Arrays.asList(grandPa, dad, son, daughter, grandPa2, dad2, son2, daughter2);
        
        return all;
	 }

	/*
    public SelectCategory() {
        TreeGrid<Person> grid = new TreeGrid<>(Person.class);
        grid.setHierarchyColumn("name");
        grid.getSelectionModel().addSelectionListener(listener)
        grid.addSelectionListener(event -> SetCategory(event.getSource().getSelectionModel().getFirstSelectedItem()));
        
        //grid.getSelectionModel().getFirstSelectedItem();
        //SingleSelectionModel sel = (SingleSelectionModel) grid.getSelectionModel();
        //Object selected = sel.getSelectedRow();
        //grid.getContainerDataSource().getItem(selected).getItemProperty("Object").getValue();
        
        Person grandPa = new Person("grand pa", null);
        Person dad = new Person("dad", grandPa);
        Person son = new Person("son", dad);
        Person daughter = new Person("daughter", dad);
        
        Person grandPa2 = new Person("grand pa2", null);
        Person dad2 = new Person("dad2", grandPa2);
        Person son2 = new Person("son2", dad2);
        Person daughter2 = new Person("daughter2", dad2);
        
        List<Person> all = Arrays.asList(grandPa, dad, son, daughter, grandPa2, dad2, son2, daughter2);

        all.forEach(p -> grid.getTreeData().addItem(p.getParent(), p));

        MultiSelectionModel<Person> selectionModel = (MultiSelectionModel<Person>) grid.setSelectionMode(SelectionMode.MULTI);
        selectionModel.addMultiSelectionListener(event -> {
            Notification.show(selection.getAddedSelection().size()
                              + " items added, "
                              + selection.getRemovedSelection().size()
                              + " removed.");

            // Allow deleting only if there's any selected
            deleteSelected.setEnabled(
                 event.getNewSelection().size() > 0);
        });
        
        add(grid);

    }
    
    public void SetCategory(Person selectedPerson) {
    	add(new Label("Name: " + selectedPerson.getName()));
    }
    */
}
