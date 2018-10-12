package com.objectfrontier.training.java.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PersonTest {

    PersonService person;
    Connection connection;

    @BeforeTest
    private void initAddress() throws Exception {
        person = new PersonService();
        connection = ConnectionManager.initConnection();
    }

//    @Test (dataProvider = "testInsert_positiveDP")
    private void insertRecord_positiveDP(Person personRecord, Address addressRecord, int expectedResult) throws Exception {

        try {
            long actualResult = person.insertRecord(connection, personRecord, addressRecord);
            Assert.assertEquals(actualResult, expectedResult);
            connection.commit();
        } catch(Exception error) {
            connection.rollback();
//            error.printStackTrace();
        }
    }

    @DataProvider
    private Object[][] testInsert_positiveDP() {

        Person recordOne = new Person();
        recordOne.setFirstName("Arvindhakrishna");
        recordOne.setLastName("Krishnasamy");
        recordOne.setEmail("karthi.arvindh@gmail.com");
        recordOne.setBirthDate(Date.valueOf("1996-11-24"));

        Address addressRecordOne = new Address();
        addressRecordOne.setStreet("323/1 Vallalar Nagar");
        addressRecordOne.setCity("Salem");
        addressRecordOne.setPostalCode(636008);

        recordOne.getFirstName();
        Person recordTwo = new Person();
        recordTwo.setFirstName("Gowtham");
        recordTwo.setLastName("Krishnan");
        recordTwo.setEmail("gowtham.cruze@gmail.com");
        recordTwo.setBirthDate(Date.valueOf("1997-05-30"));

        Address addressRecordTwo = new Address();
        addressRecordTwo.setStreet("Queens Circle");
        addressRecordTwo.setCity("Salem");
        addressRecordTwo.setPostalCode(636006);

        return new Object[][]{
            {recordOne, addressRecordOne, 1},
            {recordTwo, addressRecordTwo, 2}
        };
    }

    @Test(dataProvider = "testInsert_negativeDP")
    private void testInsert_negativeDP(Person personOne, Address addressOne,ArrayList ExpectedErrorList) throws Exception {
        try {
            PersonService personService = new PersonService();
            personService.insertRecord(connection, personOne, addressOne);
            Assert.fail("Fields are empty");
            connection.rollback();
        } catch (AppException error) {
            Assert.assertEquals(error.getErrorList().toString(), ExpectedErrorList.toString());
        }
    }

    @DataProvider
    private Object[][] testInsert_negativeDP() {

        Person personOne = new Person();
        Address addressOne = new Address();

        ArrayList<AppException> errorList = new ArrayList<>();
        errorList.add(new AppException(ErrorCode.ERROR001, "Field is empty"));
        errorList.add(new AppException(ErrorCode.ERROR002, "Field is empty"));
//        errorList.add(new AppException(ErrorCode.ERROR003, "column id is not auto incremented"));
//        errorList.add(new AppException(ErrorCode.ERROR006, "column id is not auto incremented"));
        errorList.add(new AppException(ErrorCode.ERROR007, "street field is empty"));
        errorList.add(new AppException(ErrorCode.ERROR008, "city field is empty"));
        errorList.add(new AppException(ErrorCode.ERROR009, "Pincode is empty"));

        ArrayList<AppException> errorListTwo = new ArrayList<>();
//        errorListTwo.add(new AppException(ErrorCode.ERROR003, "column id is not auto incremented"));
        errorListTwo.add(new AppException(ErrorCode.ERROR004, "Duplicate email found"));
//        errorListTwo.add(new AppException(ErrorCode.ERROR006, "column id is not auto incremented"));
        errorListTwo.add(new AppException(ErrorCode.ERROR005, "Name already exist"));

        Person recordOne = new Person();
        recordOne.setFirstName("Arvindhakrishna");
        recordOne.setLastName("Krishnasamy");
        recordOne.setEmail("karthi.arvindh@gmail.com");
        recordOne.setBirthDate(Date.valueOf("1996-11-24"));

        Address addressRecordOne = new Address();
        addressRecordOne.setStreet("Vallalar Nagar");
        addressRecordOne.setCity("Salem");
        addressRecordOne.setPostalCode(636008);

        return new Object[][] {
            {personOne, addressOne, errorList},
            {recordOne, addressRecordOne, errorListTwo}
        };

    }
//    @Test(dataProvider = "testRead_positiveDP")
    private void readRecord(Person personDetails, Person personExpectedDetails, boolean includeAddress) throws Exception {

        try {
            Person personRecords = person.readRecord(connection, personDetails, includeAddress);
            Assert.assertEquals(personRecords, personExpectedDetails);
            connection.commit();
        } catch (Exception error) {
//            error.getMessage();
            connection.rollback();
        }
    }

    @DataProvider
    private Object[][] testRead_positiveDP() {
        Person personRecords = new Person();
        personRecords.setFirstName("Arvindhakrishna");
        personRecords.setLastName("Krishnasamy");
        personRecords.setEmail("karthi.arvindh@gmail.com");
        personRecords.setBirthDate(Date.valueOf("1996-11-24"));

        Address addressDetails = new Address();
        addressDetails.setStreet("323/1 Vallalar Nagar");
        addressDetails.setCity("salem");
        addressDetails.setPostalCode(636008);
        personRecords.setAddress(addressDetails);

        Person personRecordTwo = new Person();
        personRecords.setFirstName("Arvindhakrishna");
        personRecords.setLastName("Krishnasamy");
        personRecords.setEmail("karthi.arvindh@gmail.com");
        personRecords.setBirthDate(Date.valueOf("1996-11-24"));

        Person personDetails = new Person();
        personDetails.setAddressId(1);

        return new Object[][] {
            {personDetails, personRecords, true},
            {personDetails, personRecordTwo, false}
        };
    }


//    @Test(dataProvider = "testUpdate_positiveDP")
    private void updateRecord(Person personOne, Person personTwo, boolean includeAddress) throws Exception {

        try {
            PersonService personService = new PersonService();
            Person person = personService.updatePerson(connection, personOne, includeAddress);
            Assert.assertEquals(person, personTwo);
            connection.commit();
        } catch (Exception error) {
//            Assert.fail("Record not updated");
            System.out.println(error.getMessage());
            error.printStackTrace();
            connection.rollback();
        }
    }

    @DataProvider
    private Object[][] testUpdate_positiveDP(){
        Person personOne = new Person();
        personOne.setFirstName("Arvind");
        personOne.setLastName("krish");
        personOne.setEmail("karthi.arvindh96@gmail.com");
        personOne.setId(1);

        Address addressOne = new Address();
        addressOne.setStreet("Vallalar nagar");
        addressOne.setCity("Salem");
        addressOne.setPostalCode(636008);

        personOne.setAddress(addressOne);

        return new Object[][] {
            {personOne, personOne, true}
        };
    }

//  @Test(dataProvider = "testDelete_positiveDP")
    private void deleteRecord_positiveDP(Person personOne, Person person) throws Exception {
        try {
            PersonService personService = new PersonService();
            Person personTwo = personService.deletePerson(connection, personOne);
            Assert.assertEquals(personTwo, person);
            connection.commit();
        } catch (Exception error) {
            System.out.println(error.getMessage());
            error.printStackTrace();
            connection.rollback();
        }
    }

    @DataProvider
    private Object[][] testDelete_positiveDP(){
        Person personOne = new Person();
        personOne.setFirstName("Arvindhakrishna");
        personOne.setLastName("Krishnasamy");
        personOne.setEmail("karthi.arvindh@gmail.com");
        personOne.setBirthDate(Date.valueOf("1996-11-24"));
        personOne.setId(4);
        Address addressOne = new Address();
        addressOne.setStreet("323/1 Vallalar Nagar");
        addressOne.setCity("salem");
        addressOne.setPostalCode(636008);

        return new Object[][] {
            {personOne, personOne}
        };

    }

    @AfterTest
    private void release() throws Exception {
        person = null;
        connection.close();
    }
}
