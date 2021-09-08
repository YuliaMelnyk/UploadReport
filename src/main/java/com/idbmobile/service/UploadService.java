package com.idbmobile.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.idbmobile.entity.UploadForm;
import com.idbmobile.utils.Constants;

@Service
public class UploadService {

	@Autowired
	private static VibePublishData vibePublishData;

	// upload the file and return of this file

	public static String doUpload(HttpServletRequest request, Model model, //
			UploadForm uploadForm) {
		vibePublishData = new VibePublishData();
		String description = uploadForm.getDescription();
		System.out.println("Description: " + description);

		// Root Directory.;
		String uploadRootPath = System.getProperty("user.dir") + "/tmp";
		System.out.println("uploadRootPath=" + uploadRootPath);

		File uploadRootDir = new File(uploadRootPath);
		// Create directory if it not exists.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}
		MultipartFile[] fileDatas = uploadForm.getFileDatas();
		//
		List<File> uploadedFiles = new ArrayList<File>();
		List<String> failedFiles = new ArrayList<String>();

		for (MultipartFile fileData : fileDatas) {

			// Client File Name
			String name = fileData.getOriginalFilename();
			System.out.println("Client File Name = " + name);

			if (name != null && name.length() > 0) {
				try {
					// Create the file at server
					File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(fileData.getBytes());
					stream.close();
					//
					uploadedFiles.add(serverFile);
					System.out.println("Write file: " + serverFile);
					if (name.contains(Constants.TRX_FILE) && name.endsWith(Constants.CSV_GZ)) {
						try {
							vibePublishData.publishTrxData(serverFile);
						} catch (Exception e) {
							System.out.println("Error Publish data file: " + e);
						}
					} else if (name.contains(Constants.INACTIVE_FILE) && name.endsWith(Constants.CSV_GZ)) {
						vibePublishData.publishSubsInactiveData(serverFile);
					}
				} catch (Exception e) {
					System.out.println("Error Write file: " + name);
					failedFiles.add(name);
				}
			}
		}

		model.addAttribute("description", description);
		model.addAttribute("uploadedFiles", uploadedFiles);
		model.addAttribute("failedFiles", failedFiles);
		return "uploadResult";
	}

}
