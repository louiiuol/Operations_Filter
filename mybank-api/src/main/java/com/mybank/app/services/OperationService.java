package com.mybank.app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.mybank.app.entities.OperationLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.mybank.app.config.ElasticSearchConfig;
import com.mybank.app.entities.Operation;
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
 * <p>
 * This class gives all the methods usable by the controller
 * </p>
 *
 * @see org.elasticsearch.client.transport.TransportClient
 * @see org.elasticsearch.client.transport.NoNodeAvailableException
 *
 */
@Service
public class OperationService {

	private static final Logger LOG = LogManager.getLogger();

	private static final String INDEX = "account";
	private static final String TYPE = "operation";
	private static TransportClient client = null;

	// Return a singleton instance of Elastic Search Config
	public OperationService() throws HostUnavailableException {
		client = ElasticSearchConfig.getInstance().openClient();
	}

	/**
	 * <p>
	 * This method save a new Operation:
	 * </p>
	 * <ol>
	 * <li>Format Operation to Json</li>
	 * <li>Send Json to Elastic Search</li>
	 * <li>Update Elastic Document (ID & Date)</li>
	 * <li>Return new operation (copied from Elastic Search)</li>
	 * </ol>
	 *
	 * @param operation Entity containing all information on the operation to save
	 * @see com.fasterxml.jackson.databind.ObjectMapper
	 * @see org.elasticsearch.action.index.IndexResponse
	 * @see org.elasticsearch.action.update.UpdateRequest
	 * @see org.elasticsearch.script.Script
	 * @see org.elasticsearch.search.SearchHit
	 */
	public Operation save(Operation operation)
			throws BadInputException, HostUnavailableException, DBConnectionFailedException, IdMissingException,
			UnauthorizedException, NotFoundException, ConflictException, PreconditionException, InternalErrorException {

		// Transform the operation object into Json document
		IndexResponse response ;
		Map<String, Object> esDocument = null;
		String json = ConvertService.objectToJson(operation);

		LOG.info("Indexing new Elastic Document from Json Document: " + json);

		try {

			// Create new Elastic Search Document filled by the JSON
			response = client.prepareIndex(INDEX, TYPE).setSource(json, XContentType.JSON).get();
			LOG.info("The Elastic document was successfully created with the id: " + response.getId());
		} catch (NoNodeAvailableException e) {

			// Throw exception if the connection is unavailable
			LOG.error("The document couldn't be created, the connection to elastic search failed: Host Unavailable.");
			throw new HostUnavailableException();

		}

		switch (response.status().getStatus()) { 		// Response code from elastic search

		case 201: // CREATED

			LOG.info("SUCCESS: Document indexed! - Index: " + response.getIndex());

			if (response.getId() != null && response.getId().length() > 0) {

				// Updating requests for the ID.
				UpdateRequest updateIdRequest = new UpdateRequest(INDEX, TYPE, response.getId())
						.script(new Script("ctx._source.id = \"" + response.getId() + "\""));

				// Updating requests for the created Date.
				UpdateRequest updateDateRequest = new UpdateRequest(INDEX, TYPE, response.getId())
						.script(new Script("ctx._source.date = \"" + LocalDateTime.now() + "\""));

				try {

					LOG.info("Updating id and date of the elastic document...");

					// Updating The ID
					client.update(updateIdRequest).get();
					LOG.info("SUCCESS: The Id was updated!");

					// Updating the date
					client.update(updateDateRequest).get();
					LOG.info("SUCCESS: The date was updated!");

				} catch (InterruptedException | ExecutionException e) {

					LOG.error("Adding Date and ID: Update failed. Caused by " + e.getMessage());
					throw new DBConnectionFailedException(
							"The connection was interrupted: The Elastic document couldn't update.");
				}

				// Get the final Elastic Search document updated with all new information
				esDocument = client.prepareGet(INDEX, TYPE, response.getId()).get().getSource();
				LOG.info("SUCCESS: Final Elastic Search Document created and trimmed to source" + esDocument);

			} else {
				LOG.error("The created document doesn't contain ID. Please check your Elastic Search configuration.");
				throw new IdMissingException(
						"The created document doesn't contain ID. Please check your Elastic Search configuration.");
			}
			break;

		case 400: // BadRequest
			LOG.error("The input is not valid, please check the JSON syntax and try again. Param:" + json);
			throw new BadInputException("BAD INPUT");
		case 403: // Forbidden
			LOG.error("Elastic Search doesn't allow this operation, please check your configuration file");
			throw new UnauthorizedException("Elastic Search doesn't allow this operation!");
		case 404: // Not Found
			LOG.error("Elastic Search can't find the json to import, check your configuration file.");
			throw new NotFoundException("No document found!");
		case 409: // Conflict
			LOG.error("Some conflict happened during the operation...");
			throw new ConflictException("Some conflict happened during the operation...");
		case 412: // PreconditionFailed
			LOG.error("Some condition are not respected, please refer to ElasticSearch documentation.");
			throw new PreconditionException("Some condition are not respected, please refer to ElasticSearch documentation.");
		case 500: // Internal Server Error
			LOG.error("Elastic Search has internal problems, check your configuration file and restart the app.");
			throw new InternalErrorException("Elastic Search has internal problems, check your configuration file and restart the app.");
		case 503: // ServiceUnavailable
			LOG.error("Elastic Search has internal problems, check your configuration file and restart the app.");
			throw new InternalErrorException("Elastic Search has internal problems, check your configuration file and restart the app.");
		}

		// Transform document in object and return it
		return ConvertService.esMapToObject(esDocument);
	}

	/**
	 * <p>
	 * This method return a list of operations matching with given criteria. If none
	 * given, the method will return all Operations present in Elastic Search
	 * Database.
	 * </p>
	 *
	 * @param isCredential The operation is a credential or not?
	 * @param label        The subject of the operations.
	 * @param amount      The amount of the operations.
	 * @param date         The date which the operation was made.
	 * @param id           The identifier of the operations.
	 * @param type         The type of the operations.
	 * @return The list of matching operations.
	 * @see BoolQueryBuilder
	 * @see QueryBuilders
	 * @see SearchHit
	 */
	public OperationLibrary findByCriteria(Boolean isCredential, String label, Double amount, String date, String id,
										   String type, String sort, String order, Integer page, Integer size, String account, String range) throws DBConnectionFailedException {

		List<Operation> allHits = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		SearchRequestBuilder requestHits;
		SearchHit[] finalHits;
		String loading = "Loading all the operations from the Database where ";
		boolean completeResult;
		LOG.info(date);
		if (account != null) {
			LOG.info(loading + "the account is: " + account);
			sourceBuilder.query(QueryBuilders.wildcardQuery("account", "*"+account+"*"));
		}
		if (label != null) {
			LOG.info(loading + "the label is: " + label);
            sourceBuilder.query(QueryBuilders.wildcardQuery("label", "*"+label+"*"));
		}
		if (isCredential != null) {
			LOG.info(loading + "isCredential is: " + isCredential);
            sourceBuilder.query(QueryBuilders.termsQuery("isCredential", isCredential));
		}
		if (amount != null) {
			LOG.info(loading + "the amount is " + amount);
			if (range != null) {
				LOG.info("Filtering operations depending on range: " + range);
				switch (range) {
					case "gte": sourceBuilder.query(QueryBuilders.rangeQuery("amount").gte(amount));
						break;
					case "gt": sourceBuilder.query(QueryBuilders.rangeQuery("amount").gt(amount));
						break;
					case "lt":
						sourceBuilder.query(QueryBuilders.rangeQuery("amount").lt(amount));
						break;
					case "lte":
						sourceBuilder.query(QueryBuilders.rangeQuery("amount").lte(amount));
						break;
					default:
						sourceBuilder.query(QueryBuilders.matchQuery("amount", amount));
				}
			}
		}
		if (date != null) {
			if (range != null) {
				LOG.info("Filtering operations depending on range: " + range);
				switch (range) {
					case "gte":
						sourceBuilder.query(QueryBuilders.rangeQuery("date").gte(date));
						break;
					case "gt":
						sourceBuilder.query(QueryBuilders.rangeQuery("date").gt(date));
						break;
					case "lt":
						sourceBuilder.query(QueryBuilders.rangeQuery("date").lt(date));
						break;
					case "lte":
						sourceBuilder.query(QueryBuilders.rangeQuery("date").lte(date));
						break;
					default:
						sourceBuilder.query(QueryBuilders.matchQuery("date", date));
				}
			}
			LOG.info(loading + "the date is: " + date);
		}
		if (id != null) {
			LOG.info(loading + "the id is: " + id);
            sourceBuilder.query(QueryBuilders.wildcardQuery("id", "*"+id+"*"));
		}
		if (type != null) {
			LOG.info(loading + "the type of operation is: " + type);
            sourceBuilder.query(QueryBuilders.termsQuery("type", type));
		}

        if(page != null) {
            LOG.info("Loading all the operations from the Database starting at page: " + page);
            sourceBuilder.from(page);
        }
        if(size != null) {
            LOG.info("Sorting all the operations from the Database showing : " + size +" items");
            sourceBuilder.size(size);
        }


        requestHits = client.prepareSearch().setSource(sourceBuilder);
		long totalCount = requestHits.get().getHits().getTotalHits();

        if (sort != null) {
            LOG.info("Sorting all the operations from the Database on the field: " + sort);

            if(order.equalsIgnoreCase("asc")) {
                finalHits = requestHits.addSort(sort, SortOrder.ASC).get().getHits().getHits();
            }else {
                finalHits = requestHits.addSort(sort, SortOrder.DESC).get().getHits().getHits();
            }
        }else {
            finalHits = requestHits.get().getHits().getHits();
        }

		for (SearchHit hit : finalHits) {
			// Adding every document one by one into the list
			allHits.add(ConvertService.esMapToObject(hit.getSourceAsMap()));
		}
		completeResult = !allHits.isEmpty();
        return new OperationLibrary(totalCount, completeResult, allHits);
	}

	/**
	 * <p>
	 * This method delete an operation with the given id.
	 * </p>
	 *
	 * @param id   The identifier of the operations.
	 * @see AbstractClient
	 * @see QueryBuilders
	 */
	public void deleteById(String id) {

		LOG.info("Deleting the document with the id: " + id);
		client.prepareDelete("account", "operation", id).get();
		LOG.info("SUCCESS: The document was deleted!");

	}
}
