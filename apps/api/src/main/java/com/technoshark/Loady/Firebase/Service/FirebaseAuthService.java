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

    public void setCustomClaims(String userUid, RoleEnums role, String schoolId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());
        claims.put("schoolId", schoolId);

        try {
            firebaseAuth.setCustomUserClaims(userUid, claims);
        } catch (FirebaseAuthException e) {
            throw new ForbiddenAccessException("Failed to set custom claims");
        }
    }

    public UserRecord getUserByUid(String uid) throws FirebaseAuthException {
        return firebaseAuth.getUser(uid);
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
