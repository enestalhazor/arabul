package com.example.arabul;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class UserControllerTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserRepository repository = mock(UserRepository.class);

    // missing name
    @Test
    @Transactional
    void testMissingRequiredFields1() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "talha.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("email", "talha@gmail.com")
                        .param("phone", "5519710452")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required fields"));
    }

    // missing email
    @Test
    @Transactional
    void testMissingRequiredFields2() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "talha.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("phone", "5519710452")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required fields"));
    }

    // missing password
    @Test
    @Transactional
    void testMissingRequiredFields3() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "talha.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", "talha@gmail.com")
                        .param("phone", "5519710452")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required fields"));
    }

    // missing address
    @Test
    @Transactional
    void testMissingRequiredFields4() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "talha.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", "talha@gmail.com")
                        .param("phone", "5519710452")
                        .param("password", "12345"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required fields"));
    }

    @Test
    @Transactional
    void testEmailIsTaken() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "talha.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", "enes@gmail.com")
                        .param("phone", "5519710452")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.info").value("Email taken"));
    }

    @Test
    @Transactional
    void testEmailFormat() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "talha.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", "talhagmail.com")
                        .param("phone", "5519710452")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Invalid email format"));
    }

    @Test
    @Transactional
    void testPasswordLength() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "talha.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", "talha@gmail.com")
                        .param("phone", "5519710452")
                        .param("password", "1")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Password should be more than 4 characters"));
    }

    @Test
    @Transactional
    void testPhoneFormat() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "talha.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", "talha@gmail.com")
                        .param("phone", "4535365373")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Invalid phone format"));
    }

    @Test
    @Transactional
    void testProfilePictureNotJpeg() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "profile.png",
                "image/png",               // <- not jpeg
                "dummy-image-content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", "talha@gmail.com")
                        .param("phone", "5512123204")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("This is not JPEG file"));
    }

    @Test
    @Transactional
    void testIsUserSavedToDb() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "profile.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        String email = "talha@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", email)
                        .param("phone", "5512343204")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("User inserted"));

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE email=?",
                Integer.class, email
        );
        assertNotNull(count);
        assertTrue(count == 1);
    }

    @Test
    @Transactional
    void loginTest() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "profile.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        String email = "talha@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", email)
                        .param("phone", "5512343204")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("User inserted"));

        var resultActions = mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"email\":\"talha@gmail.com\",\"password\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        String responseJson = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseJson);
        String token = jsonNode.get("token").asText();
        Claims claims = Jwts.parser()
                .setSigningKey(JWTService.getKey())
                .parseClaimsJws(token)
                .getBody();

        String userEmail = claims.get("email", String.class);
        assertEquals("talha@gmail.com", userEmail);
    }

    @Test
    @Transactional
    void loginTestForNotFound() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "profile.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        String email = "talha@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/users")
                        .file(file)
                        .param("name", "talha")
                        .param("email", email)
                        .param("phone", "5512343204")
                        .param("password", "12345")
                        .param("address", "ankara/etimesgut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("User inserted"));

        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"email\":\"talha@gmail.com\",\"password\":\"1345\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void loginMissingFieldsTest() throws Exception {
        // Missing password
        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"email\":\"talha@gmail.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required field or fields"));

        // Missing email
        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{\"password\":\"12345\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required field or fields"));

        // Both missing
        mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Missing required field or fields"));
    }

    @Test
    void testGetUserInfoIsOk() throws Exception {

        String token = JWTService.create("enes@gmail.com", 1);
        var result = mockMvc.perform(get("/api/users/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonResponse);
        String email = jsonNode.get("email").asText();

        assertEquals("enes@gmail.com", email);
    }

    @Test
    @Transactional
    void testNoTokenForGetUserInfo() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"));
    }

    @Test
    @Transactional
    void testInvalidTokenForGetUserInfo() throws Exception {

        String token = JWTService.create("enes@gmail.com", 2);
        mockMvc.perform(get("/api/users/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"));

    }

    @Test
    @Transactional
    void testNoUserForGetUserInfo() throws Exception {

        String token = JWTService.create("enes@gmail.com", 3);
        mockMvc.perform(get("/api/users/3")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("Not found user for id: 3"));
    }

    //------------------------------------------------------------------------------------------------------

    @Test
    @Transactional
    void testEditUserInfoNoToken() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "profile.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        String email = "talha@gmail.com";

        String token = JWTService.create("enes@gmail.com", 1);
        var result = mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name", "talha")
                        .param("email", "talha@gmail.com")
                        .param("phone", "5014848484")
                        .param("password", "talha1234")
                        .param("address", "Ankara/Cankaya"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void testEditUserInfoInvalidToken() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "profile.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        String email = "talha@gmail.com";

        String token = JWTService.create("enes@gmail.com", 2);
        var result = mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + token)
                        .param("name", "talha")
                        .param("email", "talha@gmail.com")
                        .param("phone", "5014848484")
                        .param("password", "talha1234")
                        .param("address", "Ankara/Cankaya"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.info").value("Unauthorized"));
    }

    @Test
    @Transactional
    void testEditUserInfoUserNotFound() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "profile_picture",
                "profile.jpeg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        String email = "talha@gmail.com";

        String token = JWTService.create("enes@gmail.com", 3);
        var result = mockMvc.perform(put("/api/users/3")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + token)
                        .param("name", "talha")
                        .param("email", "talha@gmail.com")
                        .param("phone", "5014848484")
                        .param("password", "talha1234")
                        .param("address", "Ankara/Cankaya"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("No user found for id: -1"));
    }

    @Test
    @Transactional
    void testEditUserInfoIsOk() throws Exception {

        String token = JWTService.create("enes@gmail.com", 1);
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + token)
                        .param("name", "bora")
                        .param("email", "bora@gmail.com")
                        .param("phone", "5014848484")
                        .param("password", "bora1234")
                        .param("address", "Ankara/Cankaya"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("User infos edited id: 1"));

    }
}

