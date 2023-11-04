package com.cineevent.userservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.cineevent.userservice.dto.response.UserAuthResponseDTO;

public class ThreadLocalAuthStoreTest {

    @Test
    public void testSetAndGetAccessToken() {
        String accessToken = "testAccessToken";
        ThreadLocalAuthStore.setAccessToken(accessToken);
        String retrievedAccessToken = ThreadLocalAuthStore.getAccessToken();

        assertEquals(accessToken, retrievedAccessToken);
    }

    @Test
    public void testSetAndGetAuthDetails() {
        UserAuthResponseDTO authDetails = new UserAuthResponseDTO();
        authDetails.setUserId(1);
        authDetails.setUserName("testUser");
        authDetails.setUserRole("ROLE_USER");

        ThreadLocalAuthStore.setAuthDetails(authDetails);
        UserAuthResponseDTO retrievedAuthDetails = ThreadLocalAuthStore.getAuthDetails();

        assertNotNull(retrievedAuthDetails);
        assertEquals(authDetails.getUserId(), retrievedAuthDetails.getUserId());
        assertEquals(authDetails.getUserName(), retrievedAuthDetails.getUserName());
        assertEquals(authDetails.getUserRole(), retrievedAuthDetails.getUserRole());
    }

    @Test
    public void testSetAndGetAccessTokenInDifferentThreads() throws InterruptedException {
        // Test setting and getting access token in multiple threads
        String accessToken1 = "access-token-thread-1";
        String accessToken2 = "access-token-thread-2";

        Thread thread1 = new Thread(() -> {
            ThreadLocalAuthStore.setAccessToken(accessToken1);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(accessToken1, ThreadLocalAuthStore.getAccessToken());
        });

        Thread thread2 = new Thread(() -> {
            ThreadLocalAuthStore.setAccessToken(accessToken2);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(accessToken2, ThreadLocalAuthStore.getAccessToken());
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
