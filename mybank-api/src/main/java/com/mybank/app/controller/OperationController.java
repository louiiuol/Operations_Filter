package com.mybank.app.controller;

import javax.validation.Valid;
import javax.websocket.server.PathParam;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mybank.app.entities.Operation;
import com.mybank.app.entities.OperationLibrary;

import com.mybank.app.services.OperationService;

import com.mybank.app.exceptions.BadInputException;
import com.mybank.app.exceptions.ConflictException;
import com.mybank.app.exceptions.DBConnectionFailedException;
import com.mybank.app.exceptions.HostUnavailableException;
import com.mybank.app.exceptions.IdMissingException;
import com.mybank.app.exceptions.InternalErrorException;
import com.mybank.app.exceptions.NotFoundException;
import com.mybank.app.exceptions.PreconditionException;
import com.mybank.app.exceptions.UnauthorizedException;

/**
 *
 * <p>
 *      This class exposes REST API containing multiple CRUD request
 * </p>
 *
 * @see javax.validation
 * @see org.springframework.http.HttpStatus
 * @see org.springframework.http.MediaType
 * @see org.springframework.http.ResponseEntity
 * @see org.springframework.web.bind.annotation
 * @see com.mybank.app.services
 *
 */

@RestController
@CrossOrigin("*")
public class OperationController {

	private static final Logger log = LogManager.getLogger();


    /**
     *
     * <p>
     *      This method tries to save a new operation entity into elastic search database
     * </p>
     *
     * @param       operation entity containing all information related to an operation
     * @return      Elastic HTTP response status and operation saved (if succeed)
     *
     * @see         com.mybank.app.services
     * @see         org.springframework.http.HttpStatus
     * @see         org.springframework.http.MediaType
     * @see         org.springframework.http.ResponseEntity
     * @see         javax.validation.Valid
     * @see         javax.websocket.server.PathParam
     * @see         com.mybank.app.entities.Operation
     *
     */

	@PostMapping(path = "/operations", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Operation> save(@RequestBody @Valid Operation operation) {

		Operation newOperation = null;
		HttpStatus status;

		try {

			newOperation = new OperationService().save(operation);
			status = HttpStatus.CREATED;
			log.info("Operation created: " + newOperation);

		} catch (BadInputException e) {
			status = HttpStatus.BAD_REQUEST;
			log.error("Bad Request: Fields must be valid: " + operation);

		} catch (IdMissingException e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("Can't connect to ElasticSearch, causing: Missing ID.");

		} catch (HostUnavailableException e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("Can't connect to ElasticSearch, caused by: Host not reachable.");

		} catch (DBConnectionFailedException e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("Can't connect to ElasticSearch, caused by: Connection Interrupted.");

		} catch (NotFoundException e) {
			status = HttpStatus.NOT_FOUND;
			log.error("Elastic Search can't found document, please try again.");

		} catch (InternalErrorException e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("Can't connect to ElasticSearch, caused by: Elastic Search Internal Error.");

		} catch (UnauthorizedException e) {
			status = HttpStatus.UNAUTHORIZED;
			log.error("Can't connect to ElasticSearch, caused by: Unauthorized to connect.");

		} catch (ConflictException e) {
			status = HttpStatus.CONFLICT;
			log.error("Can't connect to ElasticSearch, caused by: Elastic Search Conflict.");

		} catch (PreconditionException e) {
			status = HttpStatus.PRECONDITION_FAILED;
			log.error("Can't connect to ElasticSearch, caused by: The conditions required are not respected");

		}
		return new ResponseEntity<>(newOperation, status);
	}


	/**
     *
     * <p>
     * This method tries to save a new operation entity into elastic search database
     * </p>
     *
     * @param   isCredential Input used to filter search results
     * @param   account Input used to filter search results
     * @param   label Input used to filter search results
     * @param   amount Input used to filter search results
     * @param   date Input used to filter search results
     * @param   id Input used to filter search results
     * @param   type Input used to filter search results
     * @param   sort Input used to sort search results
     * @param   order Input used to sort search results in the specified order
     * @param   page Input used to determine starting page of search results
     * @param   size Input used to determine the total of result to show throw all search results
     *
     * @return  Depending on criteria, returns Elastic HTTP response status and OperationLibrary: entity containing all Operations and search informations
     *
     * @see     com.mybank.app.services
     * @see     org.springframework.http.HttpStatus
     * @see     org.springframework.http.MediaType
     * @see     org.springframework.http.ResponseEntity
     * @see     javax.websocket.server.PathParam
     * @see     com.mybank.app.entities.Operation
     * @see     com.mybank.app.entities.OperationLibrary
     *
     */

	@RequestMapping(value = "/operations", method = RequestMethod.GET)
	public ResponseEntity<OperationLibrary> findByCriteria(
			@RequestParam(value = "isCredential", required = false) Boolean isCredential,
			@RequestParam(value = "compte", required = false) String account,
			@RequestParam(value = "label", required = false) String label,
			@RequestParam(value = "amount", required = false) Double amount,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "range", required = false) String range) {

        OperationLibrary response = null;
		HttpStatus status;

		try {

			response = new OperationService().findByCriteria(isCredential, label, amount, date, id, type, sort, order, page, size, account, range);
			status = HttpStatus.OK;
			log.info("SUCCESS: All operations found!");

		} catch (HostUnavailableException e) {

			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("Can't connect to ElasticSearch, caused by: Host not reachable.");

		} catch (DBConnectionFailedException e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error("Can't connect to ElasticSearch, caused by: Connection Failed.");
		}
		return new ResponseEntity<>(response, status);
	}


    /**
     *
     * <p>
     * This method deletes the operation with the specified id
     * </p>
     *
     * @param   id Input used to determine which operation to delete
     *
     * @return  Depending on criteria, returns Elastic HTTP response status and OperationLibrary: entity containing all Operations and search informations
     *
     * @see     com.mybank.app.services
     * @see     org.springframework.http.HttpStatus
     * @see     org.springframework.http.MediaType
     * @see     org.springframework.http.ResponseEntity
     * @see     javax.websocket.server.PathParam
     * @see     com.mybank.app.entities.Operation
     *
     */

	@DeleteMapping(path = "/operations")
	public ResponseEntity<?> deleteById(@PathParam("id") String id) {

		HttpStatus status = null;

		try {
			status = HttpStatus.OK;
			log.info("Deleting the operation: " + id);
			new OperationService().deleteById(id);
		} catch (HostUnavailableException e) {
			log.error("SUCCESS: Operation deleted: " + id);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(status);
	}

}
