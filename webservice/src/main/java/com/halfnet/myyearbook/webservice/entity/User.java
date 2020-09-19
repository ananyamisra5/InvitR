package com.halfnet.myyearbook.webservice.entity;

import com.halfnet.myyearbook.webservice.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "myyb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final long TIMEOUT = 432000000; //5 days

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String emailAddress;

    @Column
    private boolean emailConfirmed;

    @Column
    private boolean deleted;

    @Column
    private String passwordHash;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<Token> tokens;
    
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private UserSettings settings;
    
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private PasswordReset passwordReset;
    
    public User(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailConfirmed = false;
        this.emailAddress = email;
        this.deleted = false;
    }
    
    public void removeOldTokens(){
        this.tokens.removeIf(n->(System.currentTimeMillis() - n.getDateAdded().getTime()) > TIMEOUT);
    }
    
    public boolean isValidToken(String token){
        return this.tokens.stream().anyMatch(n->n.getToken().equals(token));
    }
    
    public void addToken(String token){
        removeOldTokens();
        Token t = new Token();
        t.setDateAdded(new Date());
        t.setToken(token);
        t.setUser(this);
        this.tokens.add(t);
    }
    
    public boolean removeToken(String token){
        return this.tokens.removeIf(n->n.getToken().equals(token));
    }

    public User() {

    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        settings.setUser(this);
        this.settings = settings;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        return this.id == ((User)obj).id;
    }

    @Override
    public int hashCode() {
        return (int)this.id;
    }

    public boolean checkPassword(String password) {
        return Utils.checkPassword(password, this.passwordHash);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public PasswordReset getPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(PasswordReset passwordReset) {
        this.passwordReset = passwordReset;
    }

}
