package com.application.controller;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/selleractivity")
public class SellerActivitiesController {
	
	//private static final Logger logger = LoggerFactory.getLogger(SellerActivitiesController.class);
	
	@RequestMapping(value = "/showpendingorder", method = RequestMethod.GET)
    public ModelAndView showPendingOrders() {
		return new ModelAndView("showpendingorder");
    }
	
	@RequestMapping(value = "/showconfirmorder", method = RequestMethod.GET)
    public ModelAndView showConfirmedOrders() {
		return new ModelAndView("showconfirmorder");
    }
	
	@RequestMapping(value = "/showdeliveredorder", method = RequestMethod.GET)
    public ModelAndView showDeliveredOrders() {
		return new ModelAndView("showdeliveredorder");
    }

	@RequestMapping(value = "/showcancelorder", method = RequestMethod.GET)
    public ModelAndView showCancelledOrders() {
		return new ModelAndView("showcancelorder");
    }
	
	@RequestMapping(value = "/processrefund", method = RequestMethod.GET)
    public ModelAndView processRefundPage() {
		return new ModelAndView("processrefund");
    }
	
	@RequestMapping(value = "/confirmedrefund", method = RequestMethod.GET)
    public ModelAndView processConfirmedRefundPage() {
		return new ModelAndView("confirmedrefund");
    }
	
	@RequestMapping(value = "/cancelledrefund", method = RequestMethod.GET)
    public ModelAndView processCancelledRefundPage() {
		return new ModelAndView("cancelledrefund");
    }
	
	@RequestMapping(value = "/refundedrefund", method = RequestMethod.GET)
    public ModelAndView processRefundedRefundPage() {
		return new ModelAndView("refundedrefund");
    }
	
	@RequestMapping(value = "/searchorder", method = RequestMethod.GET)
    public ModelAndView searchorderPage() {
		return new ModelAndView("sellerSearchView");
    }
	
	@RequestMapping(value = "/raisecancelrequest", method = RequestMethod.GET)
    public ModelAndView getSellerCancelRequest() {
		return new ModelAndView("cancelrequest");
    }
}
