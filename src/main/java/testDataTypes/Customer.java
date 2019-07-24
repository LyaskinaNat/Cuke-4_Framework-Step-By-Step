package testDataTypes;

// This is a Java POJO class to represent JSON data
// To use this JSON data in the test we need to first deserializes the JSON into an object of the specified class
// And to have the JSON deserialized, a java class object must be created that has the same fields names with the fields in the JSON string

public class Customer {
    public String firstName;
    public String lastName;
    public int age;
    public String emailAddress;
    public Address address;
    public PhoneNumber phoneNumber;

    public class Address {
        public String streetAddress;
        public String city;
        public String postCode;
        public String state;
        public String country;
        public String county;
    }

    public class PhoneNumber {
        public String home;
        public String mob;
    }
}
