package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonService {

    public long insertRecord(Connection connection, Person personOne, Address addressOne) throws Exception {

        Address address = null ;
        List<AppException> errorList = new ArrayList<>();
        errorList = validate(errorList, connection, personOne);

        AddressService addressService = new AddressService();
        addressService.validateAddress(errorList, connection, addressOne);

        if (!errorList.isEmpty()) {
            throw new AppException(errorList);
        }
        address = addressService.insertRecord(connection, addressOne);
        String insertQuery = new StringBuilder("INSERT INTO person ")
                                               .append("(first_name, last_name, email, date_of_birth, address_id)")
                                               .append(" VALUES")
                                               .append("(?, ?, ?, ?, ?);")
                                               .toString();

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

        preparedStatement.setString(1, personOne.getFirstName());
        preparedStatement.setString(2, personOne.getLastName());
        preparedStatement.setString(3, personOne.getEmail());
        preparedStatement.setDate(4, (Date) personOne.getBirthDate());
        preparedStatement.setLong(5, address.getId());
        preparedStatement.executeUpdate();

        return address.getId();
    }

    public Person updatePerson(Connection connection, Person person, boolean includeAddress) throws Exception {

        List<AppException> errorList = new ArrayList<>();
        if (includeAddress == true) {

            Address addressTwo = new Address();
            addressTwo.setStreet(person.getAddress().getStreet());
            addressTwo.setCity(person.getAddress().getCity());
            addressTwo.setPostalCode(person.getAddress().getPostalCode());

            AddressService addressService = new AddressService();
            addressTwo = addressService.updateRecord(connection, addressTwo);
        }

//        validate(connection, person);
        String updateStatement = new StringBuilder("UPDATE person SET")
                                                   .append(" first_name = ?, last_name = ?, email = ?")
                                                   .append(" WHERE id = ?")
                                                   .toString();

        PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        preparedStatement.setString(3, person.getEmail());
        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Person personRecord = new Person();
        personRecord.setFirstName(resultSet.getString("first_name"));
        personRecord.setLastName(resultSet.getString("last_name"));
        personRecord.setEmail(resultSet.getString("email"));

        return person;
    }

    public List<AppException> validate(List<AppException> errorList, Connection connection, Person personOne) throws Exception {

//        List<AppException> errorList = new ArrayList<>();
        if (personOne.getFirstName() == null) {
            errorList.add(new AppException(ErrorCode.ERROR001, "Field is empty"));
        }

        if (personOne.getLastName() == null) {
            errorList.add(new AppException(ErrorCode.ERROR002, "Field is empty"));
        }

        StringBuilder readAutoIncrement = new StringBuilder("Select id from person");
        PreparedStatement preparedStatement = connection.prepareStatement(readAutoIncrement.toString());
        boolean isIdPresent = preparedStatement.getMetaData().isAutoIncrement(1);

        if (isIdPresent != true) {
            errorList.add(new AppException(ErrorCode.ERROR003, "column id is not auto incremented"));
        }

        StringBuilder selectEmailId = new StringBuilder("SELECT id ")
                                                 .append("FROM person ")
                                                 .append("WHERE email = ?");
        String selectEmail = selectEmailId.toString();

        preparedStatement = connection.prepareStatement(selectEmail);
        preparedStatement.setString(1, personOne.getEmail());
        ResultSet resultSet = preparedStatement.executeQuery();
//        ResultSet insertedId = preparedStatement.getGeneratedKeys();// convert to int;

        if (resultSet.next()) {
            errorList.add(new AppException(ErrorCode.ERROR004, "Duplicate email found"));
        }

        String readDuplictaePerson = new StringBuilder("SELECT id")
                                                       .append(" FROM person")
                                                       .append(" WHERE first_name = ? AND last_name = ?")
                                                       .toString();
        preparedStatement = connection.prepareStatement(readDuplictaePerson);
        preparedStatement.setString(1, personOne.getFirstName());
        preparedStatement.setString(2, personOne.getLastName());
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            errorList.add(new AppException(ErrorCode.ERROR005, "Name already exist"));
        }

        return errorList;
    }

    public Person readRecord(Connection connection, Person personOne,
                             boolean includeAddress) throws Exception {

        personOne = searchPerson(connection, personOne);

        if (includeAddress == true) {

            AddressService addressOne = new AddressService();
            Address address = addressOne.searchRecord(connection, personOne);
            personOne.setAddress(address);
        }
        return personOne;
    }

    private Person searchPerson(Connection connection, Person personDetails) throws Exception {

        String selectRecord = new StringBuilder("Select ")
                                                .append("first_name, last_name, email, date_of_birth, address_id ")
                                                .append(" FROM person ")
                                                .append("WHERE ")
                                                .append("id = ? ;")
                                                .toString();
        PreparedStatement preparedStatement = connection.prepareStatement(selectRecord);

        preparedStatement.setLong(1, personDetails.getId());
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        personDetails.setFirstName(resultSet.getString("first_name"));
        personDetails.setLastName(resultSet.getString("last_name"));
        personDetails.setEmail(resultSet.getString("email"));
        personDetails.setBirthDate(resultSet.getDate("date_of_birth"));
        personDetails.setAddressId(resultSet.getLong("address_id"));

        return personDetails;
    }

    public Person deletePerson(Connection connection, Person person) throws Exception {


      String selectAddressId = new StringBuilder("Select address_id from person").toString();
      PreparedStatement preparedStatement = connection.prepareStatement(selectAddressId);
      ResultSet result = preparedStatement.executeQuery();
      result.next();

      AddressService addressService = new AddressService();
      Address address = new Address();
      address.setId(result.getLong("address_id"));

      String deleteRecord = new StringBuilder("DELETE ")
                                              .append("FROM person ")
                                              .append("WHERE id")
                                              .append(" = ?")
                                              .toString();

      preparedStatement = connection.prepareStatement(deleteRecord);

      preparedStatement.setLong(1, person.getId());
      preparedStatement.execute();

      address.setId(person.getAddressId());

      addressService.deleteRecord(connection, address);
      return person;
    }
}
