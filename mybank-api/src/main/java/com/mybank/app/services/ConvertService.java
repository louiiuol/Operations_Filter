package com.mybank.app.services;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.app.entities.Operation;
import com.mybank.app.exceptions.BadInputException;
import com.mybank.app.exceptions.DBConnectionFailedException;

/**
 * <p>
 * This class defines different conversion tools
 * </p>
 *
 * @see com.fasterxml.jackson.databind.ObjectMapper
 *
 */
public class ConvertService {

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * <p>
	 * This method transform Elastic Search Map into Operation Object. <br>
	 * </p>
	 *
	 * @param src Elastic Search document to import as a map
	 * @return New Operation entity
	 * @see org.elasticsearch.client.support.AbstractClient
	 * @see com.mybank.app.entities.Operation
	 */

	public static Operation esMapToObject(Map<String, Object> src) {

		LOG.info("Creating operation from Elastic Search document: " + src);
		// Create a new Operation from NewAccount and Elastic Search fields
		Operation newOperation = new Operation((String) src.get("label"), (Double) src.get("amount"), (Boolean) src.get("isCredential"),  (String) src.get("account"), (String) src.get("type"), (String) src.get("id"), LocalDateTime.parse((String) src.get("date")));
		LOG.info("SUCCESS: Object created!");

		return newOperation;
	}

	/**
	 * <p>
	 * This method transform Object into Json document
	 * </p>
	 *
	 * @param src Json document to import as a map
	 * @return Json Document
	 * @see com.fasterxml.jackson.databind.ObjectMapper
	 */

	public static String objectToJson(Operation src) throws BadInputException {
		LOG.info("[OperationService - mapper.writeValueAsString] - Creating Json Document from " + src);
		String json;
		try {

			// Write Operation as JSON Document
			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writeValueAsString(src);
			LOG.info("SUCCESS: Json document created: " + json);

		} catch (JsonProcessingException e) {
			LOG.error("input param = " + src + " - Exception caused by :  " + e.getMessage());
			throw new BadInputException("Input object can't be parse into Json, please check Json syntax.");

		}
		return json;
	}

}
