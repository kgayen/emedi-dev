package com.application.controller;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.application.logger.LogActivity;
import com.application.model.User;
import com.application.service.UserService;
import com.application.utility.SpringPropertiesUtil;

@Controller
@RequestMapping(value = {"/user","/seller"})
public class UserActionContoller {
	
	@Autowired
	UserService userService;
	@Autowired
	LogActivity logActivity;
	
	@RequestMapping(value = "/placeorder", method = RequestMethod.GET)
    public String getPlaceOrderPage(ModelMap model,HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		/*if(user.getUser_role().equalsIgnoreCase("seller") || user.getUser_role().equalsIgnoreCase("admin")){
			return "SellerHomePage";
		}*/
        return "placeorder";
    }
	
	@RequestMapping(value = "/userprofile", method = RequestMethod.GET)
    public ModelAndView getUserProfilePage(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = ((User)session.getAttribute("user"));
		return new ModelAndView("userprofile", "command", user);
    }
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req,HttpServletResponse resp) throws IOException,ServletException {
		//logActivity.saveLogActivity(((User)session.getAttribute("user")).getUser_id(), "User has been logged out.", SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getName(), "logout");
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		session.removeAttribute("user");
		session.invalidate();
		return "redirect:/";
    }
	
	/*
	 * This method set user model from session and view to registration page
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView getRegistrationData(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = ((User)session.getAttribute("user"));
		return new ModelAndView("registration", "command", user);
    }

	/*
	 * This method Save user model information getting from registration page and set a message
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String saveRegistrationData(@ModelAttribute("SpringWeb")User user, 
 		   ModelMap model,BindingResult result,HttpServletRequest req,
 		  RedirectAttributes redirectAttributes) {
		if(userService.getUserListByMobileNo(user).size() >0 && !((userService.getUserListByMobileNo(user).get(0).getUser_id().equalsIgnoreCase(user.getUser_id())))){
			redirectAttributes.addFlashAttribute("message", "User Mobile Number Already Exists.");
			if(user.getUser_role().equalsIgnoreCase("admin") || user.getUser_role().equalsIgnoreCase("seller")){
				return "redirect:/seller/registration";
			}
			else{
				return "redirect:/user/registration";
			}
		}
		boolean insertStatus = true;
		try{
			insertStatus = userService.updateData(user);
			if(!insertStatus){
				HttpSession session = req.getSession();
				session.setAttribute("user", user);
				logActivity.saveLogActivity(user.getUser_id(), "User has been registered successfully.", SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getName(), "saveRegistrationData");
				redirectAttributes.addFlashAttribute("message", "Successfully registered.");
			}
			else{
				logActivity.saveLogActivity(user.getUser_id(), "User has not been registered.", SpringPropertiesUtil.getProperty("LogActivity.Unavailabl"), this.getClass().getName(), "saveRegistrationData");
				redirectAttributes.addFlashAttribute("message", "User Not registered.");
			}
		}
		catch(Exception e){
			logActivity.saveLogActivity(user.getUser_id(), "Exception occured.", SpringPropertiesUtil.getProperty("LogActivity.Exception"), this.getClass().getName(), "saveRegistrationData");
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		if(user.getUser_role().equalsIgnoreCase("admin") || user.getUser_role().equalsIgnoreCase("seller")){
			return "redirect:/seller/registration";
		}
		else{
			return "redirect:/user/registration";
		}
    }
	
	/*
	 * This method set save new user model new customer reg jsp page
	 */
	@RequestMapping(value = "/newregistration", method = RequestMethod.POST)
    public String saveNewRegistrationData(@ModelAttribute("SpringWeb")User user, 
    		   ModelMap model,BindingResult result,@RequestParam(value = "image", required = false) MultipartFile image,
    		   HttpServletRequest req,RedirectAttributes redirectAttributes) {
		try{
			if((userService.getDataListByUserId(user.getUser_id()).size() == 0) && (userService.getUserListByMobileNo(user).size() == 0)){
				user.setUser_creator(user.getUser_id());
				boolean insertStatus = userService.insertData(user);
				if(!insertStatus){
					HttpSession session = req.getSession();
					session.setAttribute("user", user);
					logActivity.saveLogActivity(user.getUser_id(), "User has been registered successfully.", SpringPropertiesUtil.getProperty("LogActivity.Success"), this.getClass().getName(), "saveNewRegistrationData");
					return "redirect:/user/placeorder";
				}
				else{
					logActivity.saveLogActivity(user.getUser_id(), "User has not been registered.", SpringPropertiesUtil.getProperty("LogActivity.Unavailabl"), this.getClass().getName(), "saveNewRegistrationData");
					redirectAttributes.addFlashAttribute("message", "User Not registered.");
				}
			}
			else if(userService.getDataListByUserId(user.getUser_id()).size() > 0){
				redirectAttributes.addFlashAttribute("message", "User Id Already Exists.");
			}
			else if(userService.getUserListByMobileNo(user).size() > 0){
				redirectAttributes.addFlashAttribute("message", "User Mobile Number Already Exists.");
			}
		}
		catch(Exception e){
			logActivity.saveLogActivity(user.getUser_id(), "Exception occured. Exception:"+e.getMessage(), SpringPropertiesUtil.getProperty("LogActivity.Exception"), this.getClass().getName(), "saveRegistrationData");
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/retail/newcustomerreg";
    }
	
	/*
	 * This method use to set changepassword.jsp view 
	 */
	@RequestMapping(value = "/changepassword", method = RequestMethod.GET)
	public String getChangePasswordPage(HttpServletRequest req,HttpServletResponse res) {
		//System.out.println("getChangePasswordPage handler method");
		return "changepassword";
	}
	
	/*
	 * This method use to change user password
	 * get user by using userid and old password, if true then save new password
	 */
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public String changeUserPassword(HttpServletRequest req,HttpServletResponse res) {
		String newpassword = req.getParameter("newpassword");
		String oldpassword = req.getParameter("oldpassword");
		HttpSession session = req.getSession();
		User user = ((User)session.getAttribute("user"));
		user.setUser_password(oldpassword);
		try{
			//Check user with old password and user id, if user exists then password will be change
			List<User> status= (List<User>) userService.getDataList(user);
			if(status.size() == 1){
				user = status.get(0);
				user.setUser_password(newpassword);
				//change password
				boolean passwordchangeStatus = userService.changePassword(user);
				if(!passwordchangeStatus){
					req.setAttribute("message", "Password has been changeed Successfully.");
				}
				else{
					req.setAttribute("message", "Password has not changed.");
				}
			}
			else{
				req.setAttribute("message", "Old Password does not matched");
			}
		}
		catch(Exception e){
			req.setAttribute("message", e.getMessage());
		}
		return "changepassword";
	}
	
	@RequestMapping(value = "/sellerdashboard", method = RequestMethod.GET)
    public String sellerhomePage() {
        return "SellerHomePage";
    }
	
	@RequestMapping(value = "/sellStatistic", method = RequestMethod.GET)
    public String getSellStatisticPage(HttpServletRequest req) {
		return "sellStatistic";
    }
	
	
	@RequestMapping(value = "/changesellerinfo", method = RequestMethod.GET)
    public String getChangeSellerInfoPage(HttpServletRequest req) {
		return "changesellerinfo";
    }
}
