package FOS_CORE;

public class Address {
    private int addressID;
    private String addressLine;
    private String city;
    private String state;
    private String zipCode;

    public Address(){ }

    public Address(int addressID, String addressLine, String city, String state, String zipCode){
        this.addressID = addressID;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }


    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String toString() {
        return addressLine + ", " + city + ", " + state + " " + zipCode;
    }
}
