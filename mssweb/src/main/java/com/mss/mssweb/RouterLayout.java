package com.mss.mssweb;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import de.kaesdingeling.hybridmenu.HybridMenu;
import de.kaesdingeling.hybridmenu.components.HMButton;
import de.kaesdingeling.hybridmenu.components.HMLabel;
import de.kaesdingeling.hybridmenu.components.HMSubMenu;
import de.kaesdingeling.hybridmenu.components.HMTextField;
import de.kaesdingeling.hybridmenu.components.LeftMenu;
import de.kaesdingeling.hybridmenu.components.TopMenu;
import de.kaesdingeling.hybridmenu.data.MenuConfig;
//import de.kaesdingeling.hybridmenu.demo.page.GroupPage;
//import de.kaesdingeling.hybridmenu.demo.page.HomePage;
//import de.kaesdingeling.hybridmenu.demo.page.MemberPage;
//import de.kaesdingeling.hybridmenu.demo.page.NotificationBuilderPage;
//import de.kaesdingeling.hybridmenu.demo.page.SearchPage;
//import de.kaesdingeling.hybridmenu.demo.page.SettingsPage;
//import de.kaesdingeling.hybridmenu.demo.page.UploadPage;
import de.kaesdingeling.hybridmenu.design.DesignItem;

import com.mss.mssweb.bc.AuthenticationBC;
import com.mss.mssweb.dto.UserInfo;
import com.mss.mssweb.page.*;

@Push
@Theme(Lumo.class)
@BodySize(height = "100vh", width = "100vw")
public class RouterLayout extends HybridMenu  {
	private static final long serialVersionUID = 2L;

	
	
	@Override
	public boolean init(VaadinSession vaadinSession, UI ui) {
		withConfig(MenuConfig.get().withDesignItem(DesignItem.getWhiteDesign()).withBreadcrumbs(true));
		
		boolean challengeUser = true;
		//VaadinSession session = VaadinSession.getCurrent();
		
		if (vaadinSession != null) {
			if(vaadinSession.getAttribute("sesUserID") != null) {
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
			    		vaadinSession.setAttribute("sesUserID", userInfo.getUserID());
			    		vaadinSession.setAttribute("sesUserFullName", userInfo.getFullName());
			    		vaadinSession.setAttribute("sesUserType", userInfo.getUserType());
			    		vaadinSession.setAttribute("sesUserIsActive", userInfo.getIsActive());
			    		//userFullName = userInfo.getFullName();
			    		
			    		initiateControls();
			    	}
			        component.close();
			    } else {
			    	component.setError(true);
			    }
			    
			});
			component.setOpened(true);
		}
		else {
			initiateControls();
		}
		
		return true; // build menu
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
	
	protected void initiateControls() {
		//withConfig(MenuConfig.get().withDesignItem(DesignItem.getWhiteDesign()).withBreadcrumbs(true));
		
		TopMenu topMenu = getTopMenu();
		
		String userFullName = "Agent 47";
		int userType = 0;
		
		VaadinSession vaadinSession = VaadinSession.getCurrent();
		if (vaadinSession != null) {
			if(vaadinSession.getAttribute("sesUserFullName") != null) {
				userFullName = vaadinSession.getAttribute("sesUserFullName").toString();
				userType = Integer.parseInt(vaadinSession.getAttribute("sesUserType").toString());
			}
		}
		
		topMenu.add(HMLabel.get()
				.withCaption("Welcome " + userFullName)
				);
		// JB(20190531): Hide temporary.
		//topMenu.add(HMTextField.get(VaadinIcon.SEARCH, "Search ..."));
		
		topMenu.add(HMButton.get()
				.withIcon(VaadinIcon.HOME)
				.withDescription("Home")
				.withNavigateTo(HomePage.class));
		
		getNotificationCenter()
				.setNotiButton(topMenu.add(HMButton.get()
						.withDescription("Notifications")));
		
		LeftMenu leftMenu = getLeftMenu();
		
		/*
		leftMenu.add(HMLabel.get()
				.withCaption("<b>Hybrid</b> Menu")
				.withIcon(new Image("./frontend/hybridmenu/logo.png", "HybridMenu Logo")));
		*/
		leftMenu.add(HMLabel.get()
				.withCaption("<b>Tzu Chi</b> MSS")
				.withIcon(new Image("./frontend/hybridmenu/logo.png", "HybridMenu Logo")));
		
		getBreadCrumbs().setRoot(leftMenu.add(HMButton.get()
				.withCaption("Home")
				.withIcon(VaadinIcon.HOME)
				.withNavigateTo(HomePage.class)));
		
		leftMenu.add(HMButton.get()
				.withCaption("Search")
				.withIcon(VaadinIcon.SEARCH)
				.withNavigateTo(SearchPage.class));
		
		// Contributor only.
		if (userType == 2) {
			leftMenu.add(HMButton.get()
					.withCaption("Upload")
					.withIcon(VaadinIcon.UPLOAD)
					.withNavigateTo(UploadPage.class));
		}
		/*
		leftMenu.add(HMButton.get()
				.withCaption("Notification Builder") 
				.withIcon(VaadinIcon.BELL)
				.withNavigateTo(NotificationBuilderPage.class));
		*/
		/*
		leftMenu.add(HMButton.get()
				.withCaption("Theme Builder")
				.withIcon(FontAwesome.WRENCH)
				.withNavigateTo(ThemeBuilderPage.class));
		*/

		/*
		HMSubMenu memberList = leftMenu.add(HMSubMenu.get()
				.withCaption("Member")
				.withIcon(VaadinIcon.USERS));
		
		memberList.add(HMButton.get()
				.withCaption("Settings")
				.withIcon(VaadinIcon.COGS)
				.withNavigateTo(SettingsPage.class));
		
		memberList.add(HMButton.get()
				.withCaption("Member")
				.withIcon(VaadinIcon.USERS)
				.withNavigateTo(MemberPage.class));
		
		memberList.add(HMButton.get()
				.withCaption("Group")
				.withIcon(VaadinIcon.USERS)
				.withNavigateTo(GroupPage.class));

		HMSubMenu memberListTwo = memberList.add(HMSubMenu.get()
				.withCaption("Member")
				.withIcon(VaadinIcon.USERS));

		memberListTwo.add(HMButton.get()
				.withCaption("Settings")
				.withIcon(VaadinIcon.COGS)
				.withNavigateTo(SettingsPage.class));
		
		memberListTwo.add(HMButton.get()
				.withCaption("Member")
				.withIcon(VaadinIcon.USERS)
				.withNavigateTo(MemberPage.class));
		*/

		// For BizAdmin and SysAdmin only.
		if (userType == 3 || userType == 4) {
			HMSubMenu memberList = leftMenu.add(HMSubMenu.get()
					.withCaption("Settings")
					.withIcon(VaadinIcon.COGS));
			
			memberList.add(HMButton.get()
					.withCaption("User")
					.withIcon(VaadinIcon.USERS)
					.withNavigateTo(SearchUserPage.class));

			/*
			HMSubMenu demoSettings = leftMenu.add(HMSubMenu.get()
					.withCaption("Settings")
					.withIcon(VaadinIcon.COGS));
			
			demoSettings.add(HMButton.get()
					.withCaption("White Theme")
					.withIcon(VaadinIcon.PALETE)
					.withClickListener(e -> switchTheme(DesignItem.getWhiteDesign())));

			demoSettings.add(HMButton.get()
					.withCaption("Dark Theme")
					.withIcon(VaadinIcon.PALETE)
					.withClickListener(e -> switchTheme(DesignItem.getDarkDesign())));
			
			demoSettings.add(HMButton.get()
					.withCaption("Minimal")
					.withIcon(VaadinIcon.COG)
					.withClickListener(e -> getLeftMenu().toggleSize()));
			*/
		}
		
	}
}
