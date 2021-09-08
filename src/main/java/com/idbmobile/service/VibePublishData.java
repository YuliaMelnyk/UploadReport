package com.idbmobile.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.idbmobile.utils.HelperClass;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VibePublishData extends PublishData {

	@Override
	public void publishTrxData(File file) throws IOException, JSchException, SftpException {

		File output = null;
		try {

			output = decompressFile(file);

			final List<List<String>> records = getRecords(output);

			sendTrxData(records);
		} catch (Exception e) {
			log.error("Error {}", e);
		} finally {
			HelperClass.deleteFile(output);
		}

	}

	@Override
	public void publishSubsInactiveData(File file) throws IOException, JSchException, SftpException {
		// TODO Auto-generated method stub

	}

}
