package com.idbmobile.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelperClass {

	HelperClass() {

	}

	public static void deleteFile(File file) {

		boolean isOk = FileUtils.deleteQuietly(file);

		log.info("Deleting file {} : {}", file.getName(), isOk);

	}

}
