package org.anderes.web.rest.dto;

import javax.validation.constraints.NotNull;

public class Employee {

    @NotNull
    private String firstname;
    @NotNull
    private String lastname;

    public Employee(String lastname, String firstname) {
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
