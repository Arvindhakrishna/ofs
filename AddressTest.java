package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddressTest {

    AddressService address;
    Connection connection;

    @BeforeTest
    private void init() throws Exception {
        address = new AddressService();
        connection = ConnectionManager.initConnection();
    }

    @Test (priority = 1, dataProvider = "testInsert_positiveDP")
    private void insertTable_positiveDP(Address addressDetails, Address expectedResult) throws Exception {
        try {
            Address actualResult = address.insertRecord(connection, addressDetails);
            Assert.assertEquals(actualResult.getId(), expectedResult.getId());
            connection.commit();
        } catch(Exception error) {
            error.getMessage();
            connection.rollback();
        }
    }

    @DataProvider
    private Object[][] testInsert_positiveDP() {
        Address addressRecord = new Address();
        addressRecord.setStreet("Vinaya Nagar");
        addressRecord.setCity("Salem");
        addressRecord.setPostalCode(636007);

        Address addressRecordTwo = new Address();
        addressRecordTwo.setStreet("Murugan nagar");
        addressRecordTwo.setCity("Cuddalore");
        addressRecordTwo.setPostalCode(620002);
        return new Object[][] {
            {addressRecord, addressRecord},
            {addressRecordTwo, addressRecordTwo}
        };
    }

    @Test (priority = 2, dataProvider = "testInsert_negativeDP")
    private void insertRecord_negativeDP(Address addressDetails, int expectedResult) {

        try {
            Address actualResult = address.insertRecord(connection, addressDetails);
            Assert.fail("Postal code is invalid");
            connection.rollback();
        } catch(Exception error) {
            Assert.assertEquals(error.getMessage(), "Postal code is invalid");
        }
    }

    @DataProvider
    private Object[][] testInsert_negativeDP() {

        Address addressRecord = new Address();
        addressRecord.setStreet("Vinaya Nagar");
        addressRecord.setCity("Salem");
        addressRecord.setPostalCode(0);

        Address addressRecordTwo = new Address();
        addressRecordTwo.setStreet(null);
        addressRecordTwo.setCity(null);
        addressRecordTwo.setPostalCode(636009);
        return new Object[][] {
            {addressRecord, 1}
        };
    }

    @AfterTest
    private void release() throws Exception {
        address = null;
        connection.close();
    }
}