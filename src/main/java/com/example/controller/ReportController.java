package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.IssueReport;
import com.example.repository.IssueRepository;

@Controller
public class ReportController {

	@Autowired
	IssueRepository issueRepository;

	@GetMapping("/issuereport")
	public String getReport(Model model, @RequestParam(name = "submitted", required = false) boolean submitted) {
		model.addAttribute("submitted", submitted);
		model.addAttribute("issuereport", new IssueReport());
		return "issues/report_form";
	}

	@PostMapping("/issuereport")
	public String submitReport(@RequestParam("file") MultipartFile file, IssueReport issueReport,
			RedirectAttributes ra) {
		// Path.GetTempFileName
		// issueReport.filePath = tempFilePath;
		// Or upload to cloud and save file url
		issueRepository.save(issueReport);
		ra.addAttribute("submitted", true);
		return "issues/issuereport_list";
	}

	@GetMapping("/issues")
	public String getIssueReport(Model model) {
		model.addAttribute("issues", issueRepository.findAllButPrivate());
		return "issues/issuereport_list";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
		return "redirect:/issuereport";
	}

}
