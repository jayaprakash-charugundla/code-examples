package com.jc.soda.repository;

import static java.util.Optional.empty;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jc.soda.ApplicationException;
import com.jc.soda.JsonHelper;
import com.jc.soda.controller.PagedData;
import com.jc.soda.controller.PagedMeta;
import com.jc.soda.document.Employee;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import oracle.soda.OracleCollection;
import oracle.soda.OracleCursor;
import oracle.soda.OracleDatabase;
import oracle.soda.OracleDocument;
import oracle.soda.OracleException;

public class EmployeeRepository {

    private final String collection;
    private final OracleDatabase oracleDatabase;
    private final OracleCollection oracleCollection;
    private Supplier<String> uuidSupplier = () -> randomUUID().toString();
    private JsonHelper jsonHelper = JsonHelper.getInstance();

    public EmployeeRepository(OracleDatabase oracleDatabase, String collection) throws OracleException {
        this.collection = collection;
        this.oracleDatabase = oracleDatabase;
        this.oracleCollection = oracleDatabase.openCollection(collection);

    }

    public Employee create(Employee employee) {
        try {
            OracleDocument oracleDocument = oracleCollection.insertAndGet(
                oracleDatabase.createDocumentFromString(uuidSupplier.get(), jsonHelper
                    .toString(employee)));
            employee.setId(fromString(oracleDocument.getKey()));
            return employee;
        } catch (OracleException e) {
            throw new ApplicationException("Error occurred while saving "
                + "employee in a collection - " + collection, e);
        }
    }

    public Optional<Employee> find(String id) {
        try (OracleCursor cursor = oracleCollection.find().key(id).getCursor()) {
            if (cursor.hasNext()) {
                OracleDocument document = cursor.next();
                Employee employee = jsonHelper.fromString(document.getContentAsString(), Employee.class);
                employee.setId(fromString(document.getKey()));
                return Optional.of(employee);
            }
        } catch (OracleException | IOException e) {
            throw new ApplicationException("Error occurred while fetching "
                + "document in a collection - " + collection, e);
        }
        return empty();
    }

    public PagedData<Employee> findAll(int offset, int count) {
        try (OracleCursor cursor = oracleCollection
            .find().skip(offset).limit(count).getCursor()) {
            long totalCount = oracleCollection.find().count();
            return PagedData.<Employee>builder().meta(new PagedMeta(offset, count, totalCount))
                .data(fetch(cursor)).build();
        } catch (OracleException | IOException e) {
            throw new ApplicationException("Error occurred while fetching "
                + "documents in a collection - " + collection, e);
        }
    }

    public PagedData<Employee> findAll(JsonNode query, int offset, int count) {
        try (OracleCursor cursor = oracleCollection
            .find().filter(jsonHelper.toString(query)).getCursor()) {
            long totalCount = oracleCollection.find().filter(jsonHelper.toString(query)).count();
            return PagedData.<Employee>builder().meta(new PagedMeta(offset, count, totalCount))
                .data(fetch(cursor)).build();
        } catch (OracleException | IOException e) {
            throw new ApplicationException("Error occurred while fetching "
                + "documents in a collection - " + collection, e);
        }
    }

    public boolean updateEmployee(Employee employee) {
        try {
            return oracleCollection
                .find()
                .key(employee.getId().toString())
                .replaceOne(oracleDatabase.createDocumentFromString(jsonHelper.toString(employee)));
        } catch (OracleException e) {
            throw new ApplicationException("Error occurred while updating "
                + "document in a collection - " + collection, e);
        }
    }

    public boolean delete(String id) {
        try {
            return oracleCollection
                .find()
                .key(id)
                .remove() > 0;
        } catch (OracleException e) {
            throw new ApplicationException("Error occurred while deleting "
                + "document in a collection - " + collection, e);
        }
    }

    private List<Employee> fetch(OracleCursor cursor) throws OracleException {
        List<Employee> employees = new ArrayList<>();
        while (cursor.hasNext()) {
            OracleDocument oracleDocument = cursor.next();
            JsonNode jsonNode = jsonHelper.readTree(oracleDocument.getContentAsString());
            ((ObjectNode) jsonNode).put("id", oracleDocument.getKey());
            employees.add(jsonHelper.fromString(jsonNode, Employee.class));
        }
        return employees;
    }
}
