package com.technoshark.Loady.Firebase.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.technoshark.Loady.Enums.RoleEnums;
import com.technoshark.Loady.ErrorHandler.Exceptions.ForbiddenAccessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseToken verifyIdToken(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            throw new ForbiddenAccessException("Invalid Firebase token");
        }
    }

    public void setCustomClaims(String userUid, RoleEnums role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());

        try {
            firebaseAuth.setCustomUserClaims(userUid, claims);
        } catch (FirebaseAuthException e) {
            throw new ForbiddenAccessException("Failed to set custom claims");
        }catch(Exception e){
            throw new InternalError("An unexpected error occurred while setting custom claims");
        }
    } 

    public UserRecord getUserByUid(String uid) throws FirebaseAuthException {
        return firebaseAuth.getUser(uid);
    }

    public UserRecord getUserByEmail(String email) {
        try {
            return firebaseAuth.getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            return null;
        }
    }

    public UserRecord createUser(String email, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        return firebaseAuth.createUser(request);
    }

    public void deleteAllUsers() {
        try {
            ListUsersPage page = firebaseAuth.listUsers(null);

            while (page != null) {
                List<String> uids = new ArrayList<>();
                for (UserRecord user : page.getValues()) {
                    uids.add(user.getUid());
                }

                if (!uids.isEmpty()) {
                    firebaseAuth.deleteUsers(uids);
                    System.out.println("Deleted " + uids.size() + " users");
                }

                page = page.getNextPage();
            }

            System.out.println("All users deleted.");
        } catch (FirebaseAuthException e) {
            throw new ForbiddenAccessException("Failed to delete users");
        }
    }

}
