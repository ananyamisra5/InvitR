package com.halfnet.myyearbook.webservice.manager;

import com.halfnet.myyearbook.webservice.entity.PasswordReset;
import com.halfnet.myyearbook.webservice.entity.Token;
import com.halfnet.myyearbook.webservice.entity.User;
import com.halfnet.myyearbook.webservice.repo.PasswordResetRepo;
import com.halfnet.myyearbook.webservice.repo.TokenRepo;
import com.halfnet.myyearbook.webservice.repo.UserRepo;
import com.halfnet.myyearbook.webservice.util.Utils;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManager {

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private PasswordResetRepo passwordResetRepo;
    
    public UserManager() {

    }

    /**
     * checks to see if the password given matches the Password Requirements
     *
     * @param password the password to check
     * @return true if the password matches the Requirements
     */
    public boolean passwordMatchesCustomReqs(String password) {
        return password.length() >= 12 && password.length() <= 64;
    }
    
    public boolean doesTokenExist(String token){
        return this.tokenRepo.existsByToken(token);
    }

    /**
     * checks if a email matches custom requirements
     *
     * @param email the email to check
     * @return if the email passes custom requirements
     */
    public boolean emailMatchesCustomReqs(String email) {
        return !email.equalsIgnoreCase("info")
                && !email.equalsIgnoreCase("self")
                && email.matches("[a-zA-Z0-9._-]{8,32}");
    }

    /**
     * Gets a User Object from the Database given an ID
     *
     * @param id the ID of the User
     * @return A User Object with the given ID
     * @throws SQLException If there is an error when accessing the Database
     */
    public User getUserByID(long id) throws SQLException {
        return userRepo.findById(id).orElse(null);
    }

    public Iterable<User> getUsers() throws SQLException {
        return userRepo.findAll();
    }

    /**
     * returns a currently logged in user based off of the token
     *
     * @param token the token
     * @return the user
     */
    public User getUserByToken(String token) {
        Token t = tokenRepo.getByToken(token);
        if(t == null){
            return null;
        }
        User user = t.getUser();//userRepo.findByToken(token);
        if (user == null) {
            //throw new RuntimeException("user token is invalid [" + token + "]");
        }
        return user;
    }

    /**
     * checks if a user level is valid
     *
     * @param g the user level to check
     * @return true if the user level is valid
     */
    public boolean isValidUserType(String g) {
        return g.equalsIgnoreCase("admin") || g.equalsIgnoreCase("limited")
                || g.equalsIgnoreCase("owner") || g.equalsIgnoreCase("regular");
    }

    /**
     * checks if the user is logged in
     *
     * @param token the token of the user to check
     * @return true if there is a logged in user with the token
     */
    public boolean isLoggedIn(String token) {
        return this.getUserByToken(token) != null;
    }

    /**
     * Checks if a user exists in the database
     *
     * @param email the email to check against
     * @return true if the user exists
     */
    public boolean userExists(String email) {
        return userRepo.existsByEmailIgnoreCase(email);
    }

    /**
     * Checks if a user exists in the database
     *
     * @param userId the user id to check against
     * @return true if the user exists
     */
    public boolean userExists(long userId) {
        return userRepo.existsById(userId);
    }

    /**
     * checks if a user is logged in
     *
     * @param email the email to check against
     * @return true if the user is logged in
     */
    public boolean isUserLoggedIn(String email) {
        boolean loggedIn = false;
        User user = userRepo.findByEmailIgnoreCase(email);
        if (user != null && !user.getTokens().isEmpty()) {
            loggedIn = true;
        }
        return loggedIn;
    }

    /**
     * logs out a user
     *
     * @param email the email from which to logout
     * @param token the token to remove
     */
    public void logoutUser(String email, String token) {
        User user = userRepo.findByEmailIgnoreCase(email);
        if (user != null && user.isValidToken(token)) {
            user.removeToken(token);
            userRepo.save(user);
        }
    }

    /**
     * logs out a user
     *
     * @param u the user to logout
     * @param token the token to remove
     */
    public void logoutUser(User u, String token) {
        logoutUser(u.getEmailAddress(), token);
    }

    /**
     * logs in a user
     *
     * @param u the user to login
     * @param token the token to add
     */
    public void loginUser(User u, String token) {
        User user = userRepo.findByEmailIgnoreCase(u.getEmailAddress());
        // don't reset token if id < 0 as that is used for debugging and should not get reset
        //except Ethan Ferguson gets the id '-1'
        if (user != null && (user.getId() >= -1 || user.getTokens().isEmpty())) {
            user.addToken(token);
            userRepo.save(user);
        }

    }

    /**
     * adds a new user to the database if there isnt already a user with the
     * same email
     *
     * @param u the user entity with the data
     * @return true if a user was added
     */
    public boolean addUserIfNotExists(User u) {
        if (userExists(u.getEmailAddress())) {
            return false;
        }
        userRepo.save(u);
        return true;
    }

    /**
     * adds a new user to the database
     *
     * @param u the user entity with the data
     */
    public void addUserOrUpdate(User u) {
        userRepo.save(u);
    }

    /**
     * Checks if a user is not null<br>
     * Does not check for accessibility (if they can log in and are part of a
     * group)
     *
     * @param u the User to check
     * @return true if all checks pass
     */
    public boolean singleCheckNonAccessible(User u) {
        return u != null;
    }

    /**
     * Checks if a user is not null, not deleted, and not pending
     *
     * @param u the User to check
     * @return true if all checks pass
     */
    public boolean singleCheck(User u) {
        return u != null && !u.isDeleted();
    }
    
    public void deleteUser(User u){
        userRepo.delete(u);
    }
    
    public void resetPassword(@NotNull User u) {
        PasswordReset r = u.getPasswordReset();
        if(r == null) {
            r = new PasswordReset();
        }
        r.setNewPassword(Utils.genPasswordReset());
        r.setUser(u);
        passwordResetRepo.save(r);
    }
    
    public boolean checkPasswordReset(User u, String password){
        if(u.getPasswordReset() == null){
            return false;
        }
        return u.getPasswordReset().getNewPassword().equals(password);
    }
    
    public Set<PasswordReset> getPasswordResets() {
        Set<PasswordReset> ret = new HashSet<>();
        for (User u : userRepo.findAll()) {
            if(u.getPasswordReset() != null){
                ret.add(u.getPasswordReset());
            }
        }
        return ret;
    }
}
