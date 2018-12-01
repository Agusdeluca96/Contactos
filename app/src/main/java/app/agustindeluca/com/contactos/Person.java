package app.agustindeluca.com.contactos;

import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private String firstPhone;
    private String secondPhone;
    private String email;
    private String contactType;


    public Person(String name, String firstPhone, String secondPhone, String email, String contactType){
        this.name = name;
        this.firstPhone = firstPhone;
        this.secondPhone = secondPhone;
        this.email = email;
        this.contactType = contactType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstPhone() {
        return firstPhone;
    }

    public void setFirstPhone(String firstPhone) {
        this.firstPhone = firstPhone;
    }

    public String getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(String secondPhone) {
        this.secondPhone = secondPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
}
