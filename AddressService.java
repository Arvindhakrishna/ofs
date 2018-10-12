package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AddressService {

    public Address insertRecord(Connection connection, Address addressDetails) throws Exception {

        List<AppException> errorList = new ArrayList<>();
        errorList = validateAddress(errorList, connection, addressDetails);

        if (!errorList.isEmpty()) {
            throw new AppException(errorList);
        }
        String insertQuery = new StringBuilder("INSERT INTO address ")
                                               .append("(street, city, postal_code) ")
                                               .append("VALUES")
                                               .append("(?, ?, ?);")
                                               .toString();

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

        preparedStatement.setString(1, addressDetails.getStreet());
        preparedStatement.setString(2, addressDetails.getCity());
        preparedStatement.setLong(3, addressDetails.getPostalCode());

        preparedStatement.executeUpdate();
        Address addressId = getAddressId(connection, addressDetails);
        return addressId;
    }


    public Address updateRecord(Connection connection, Address addressOne) throws Exception {

        List<AppException> errorList = new ArrayList<>();
//        errorList = validateAddress(connection, addressOne);
        String updateRecord = new StringBuilder("Update address SET")
                                                .append(" street = ?, city = ?, postal_code = ? ")
                                                .append("WHERE id = ?")
                                                .toString();

        PreparedStatement preparedStatement = connection.prepareStatement(updateRecord);
        preparedStatement.setString(1, addressOne.getStreet());
        preparedStatement.setString(2, addressOne.getCity());
        preparedStatement.setLong(3, addressOne.getPostalCode());
        preparedStatement.setLong(4, addressOne.getId());

        preparedStatement.executeUpdate();

        String selectUpdate = new StringBuilder("SELECT street, city, postal_code")
                                                .append(" WHERE id = ?")
                                                .toString();
        preparedStatement = connection.prepareStatement(selectUpdate);
        preparedStatement.setLong(1, addressOne.getId());
        ResultSet result = preparedStatement.executeQuery(selectUpdate);
        Address addressTwo = new Address();
        result.next();
        addressTwo.setStreet(result.getString("street"));
        addressTwo.setCity(result.getString("city"));
        addressTwo.setPostalCode(result.getLong("postal_code"));

        return addressTwo;
    }

    public List<AppException> validateAddress(List<AppException> errorList, Connection connection, Address addressDetails) throws Exception {

        StringBuilder readAutoIncrement = new StringBuilder("Select id from address");
        PreparedStatement preparedStatement = connection.prepareStatement(readAutoIncrement.toString());
        boolean isAutoKey = preparedStatement.getMetaData().isAutoIncrement(1);

        if (isAutoKey != true) {
            errorList.add(new AppException(ErrorCode.ERROR006, "column id is not auto incremented"));
        }

        if (addressDetails.getStreet() == null) {
            errorList.add(new AppException(ErrorCode.ERROR007, "street field is empty"));
        }
        if (addressDetails.getCity() == null) {
            errorList.add(new AppException(ErrorCode.ERROR008, "city field is empty"));
        }
        if (addressDetails.getPostalCode() == 0) {
            errorList.add(new AppException(ErrorCode.ERROR009, "Pincode is empty"));
        }
        return errorList;
    }

    public Address getAddressId(Connection connection, Address addressDetail) throws Exception {


        Address address = new Address();
        String selectAddressId = new StringBuilder("SELECT id FROM address")
                                                   .append(" WHERE street = ?")
                                                   .append( "AND city = ?")
                                                   .append(" AND postal_code = ?;")
                                                   .toString();

        PreparedStatement preparedStatement = connection.prepareStatement(selectAddressId);
        preparedStatement.setString(1, addressDetail.getStreet());
        preparedStatement.setString(2, addressDetail.getCity());
        preparedStatement.setLong(3, addressDetail.getPostalCode());

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        address.setId(resultSet.getLong(1));
        return address;
    }

    public Address searchRecord(Connection connection, Person personOne) throws Exception {

        String searchAddress = new StringBuilder("Select")
                                                        .append("street, city, postal_code")
                                                        .append(" FROM address")
                                                        .append("WHERE id = ?")
                                                        .toString();

        PreparedStatement preparedStatement = connection.prepareStatement(searchAddress);
        preparedStatement.setLong(1, personOne.getAddressId());

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        Address addressRecord = new Address();
        addressRecord.setStreet(resultSet.getString("street"));
        addressRecord.setCity(resultSet.getString("city"));
        addressRecord.setPostalCode(resultSet.getLong("postal_code"));
        return addressRecord;
    }

    public boolean deleteRecord(Connection connection, Address addressDetail) throws Exception {

        String deleteRecord = new StringBuilder("DELETE FROM address ")
                                                .append("WHERE id = ?")
                                                .toString();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteRecord);
        preparedStatement.setLong(1, addressDetail.getId());
        boolean isDeleted = preparedStatement.execute();
        return isDeleted;
    }
}
