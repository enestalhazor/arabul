package com.example.arabul;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class UserControllerTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(jsonPath("$.info").value("Password length should be more than 4 character"));
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
}

