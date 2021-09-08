package com.idbmobile.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.idbmobile.entity.UploadForm;
import com.idbmobile.service.UploadService;

@Controller
public class UploadController {

	private UploadService service;

	@RequestMapping(value = "/")
	public String homePage() {

		return "index";
	}

	// GET: Show upload form page.
	@RequestMapping(value = "/uploadReport", method = RequestMethod.GET)
	public String uploadOneFileHandler(Model model) {

		UploadForm uploadForm = new UploadForm();
		model.addAttribute("uploadForm", uploadForm);

		return "uploadReport";
	}

	// POST: Do Upload
	@RequestMapping(value = "/uploadReport", method = RequestMethod.POST)
	public String uploadOneFileHandlerPOST(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("uploadForm") UploadForm uploadForm) {

		return service.doUpload(request, model, uploadForm);

	}
//
//	@GetMapping("/billing")
//	public String sendDcbVodacomData(final String date) {
//
//		LocalDate localDate = LocalDate.parse(date, formatter);
//
//		// reset.resetStatusVibe();
//
//		try {
//			dcbVodacom.publishTrxData(localDate);
//		} catch (Exception e) {
//			log.error("Error Vibe billing {} {}", e, date);
//
//		}
//
//		return "OK";
//
//	}

}
