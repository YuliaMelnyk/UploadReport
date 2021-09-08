/*
 * Copyright (C) IDB Mobile Technology S.L. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.idbmobile.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.idbmobile.utils.Constants;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PublishData {

	@Autowired
	protected RestTemplate restTemplate;

	public abstract void publishSubsInactiveData(final File file) throws IOException, JSchException, SftpException;

	public abstract void publishTrxData(final File file) throws IOException, JSchException, SftpException;

	// read the file line per line

	protected final List<List<String>> getRecords(final File output) throws FileNotFoundException {
		final List<List<String>> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(output)) {
			// Skip first line
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
		}
		return records;
	}

	protected final void sendSubsInactiveData(final List<List<String>> records, final String domain) {

		for (final List<String> list : records) {

			final String[] split = list.get(0).split(";");
			// SUBSCRIPTION_ID;TRANSACTION_ID;PACKAGE_ID;AMOUNT;TIME_STAMP

			final String url = buildUrlSubsInactive(split, domain);
			log.info("Build Inactive data parameters:   {}", url);

			final String response = restTemplate.getForObject(url, String.class);

			log.info("Response Inactive data {}", response);

		}
	}

	private String buildUrlSubsInactive(final String[] split, final String domain) {

		return domain + "?subscription_id=" + split[0] + "&package_id=" + split[1] + "&time_stamp=" + split[2]
				+ "&account=" + split[3] + "&status=" + split[4] + "&start_date=" + split[5] + "&end_date="
				+ URLEncoder.encode(split[6], StandardCharsets.UTF_8);
	}

	protected void sendTrxData(final List<List<String>> records) {
		for (final List<String> list : records) {

			final String[] split = list.get(0).split(";");
			// SUBSCRIPTION_ID;TRANSACTION_ID;PACKAGE_ID;AMOUNT;TIME_STAMP
			log.info("Build trx parameters:   {}", buildUrlTrx(split));

			// final String response = restTemplate.getForObject(buildUrlTrx(split),
			// String.class);

			// log.info("Response Trx data {}", response);
		}
	}

	private String buildUrlTrx(final String[] split) {
		return Constants.DOMAIN_URL + "?subscription_id=" + split[0] + "&transaction_id=" + split[1] + "&package_id="
				+ split[2] + "&amount=" + split[3] + "&time_stamp=" + split[4] + "&type="
				+ URLEncoder.encode(split[6], StandardCharsets.UTF_8);
	}

	// decompress File from .gz to .csv
	protected final File decompressFile(File file) throws IOException {

		String ouString = file.getPath().replace(".gz", "");
		final File output = new File(ouString);

		decompressGZIP(file, output);

		com.idbmobile.utils.HelperClass.deleteFile(file);

		return output;
	}

	private static final void decompressGZIP(final File input, final File output) throws IOException {
		try (GzipCompressorInputStream in = new GzipCompressorInputStream(new FileInputStream(input))) {
			FileOutputStream stream = new FileOutputStream(output);
			try {
				IOUtils.copy(in, stream);
			} catch (Exception e) {
				log.error("Response Inactive data {}", e);
			} finally {
				stream.close();
			}

		}
	}

	private List<String> getRecordFromLine(final String line) {
		final List<String> values = new ArrayList<>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		return values;
	}

}