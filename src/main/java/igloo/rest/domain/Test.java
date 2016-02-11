package igloo.rest.domain;

/**
 * @author Yikai Gong
 */

public class Test {
    private String firstname;

    private String lastname;

    public Test() {
    }

    public Test(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
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
