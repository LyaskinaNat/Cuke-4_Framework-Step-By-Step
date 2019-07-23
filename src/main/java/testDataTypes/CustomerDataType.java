package testDataTypes;

import java.util.Map;

public class CustomerDataType {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String streetAddress;
    private String city;
    private String postCode;
    private String country;
    private String phoneNumber;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }



    public static CustomerDataType customerDetails (Map<String, String> entry) {
        CustomerDataType input = new CustomerDataType();
        input.setFirstName(entry.get("first_name"));
        input.setLastName(entry.get("last_name"));
        input.setCountry(entry.get("country"));
        input.setStreetAddress(entry.get("street_address"));
        input.setCity(entry.get("city"));
        input.setPostCode(entry.get("postcode"));
        input.setPhoneNumber(entry.get("phone_number"));
        input.setEmailAddress(entry.get("email_address"));

        return input;
    }

    @Override
    public String toString() {
        return "CustomerDataType [" +
                "first_name: " + firstName +
                ", last_name: " + lastName +
                ", country: " + country +
                ", street_address:" + streetAddress +
                ", city: " + city +
                ", postcode: " + postCode +
                ", phone_number: " + phoneNumber +
                ", email_address: " + emailAddress +
                "]";
    }
}
