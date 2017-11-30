package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * This class represents the user entity.
 */

@Entity
public class User {

    @Id
    private int id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String lastLoginIp;

    public User(){}

    public User(String username, String password, String ip){
        this.username = username;
        this.password = password;
        this.lastLoginIp = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
}
