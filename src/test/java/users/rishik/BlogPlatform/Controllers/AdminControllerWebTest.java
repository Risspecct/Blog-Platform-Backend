package users.rishik.BlogPlatform.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// Import your SecurityConfig
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
// Import the new @MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Import all your security components
import users.rishik.BlogPlatform.Entities.User;
import users.rishik.BlogPlatform.Repositories.UserRepository;
import users.rishik.BlogPlatform.Security.JwtConfig;
import users.rishik.BlogPlatform.Security.SecurityConfig;
import users.rishik.BlogPlatform.Services.JwtService;
import users.rishik.BlogPlatform.Services.MyUserDetailsService;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. Tell the test to load your real SecurityConfig
@Import(SecurityConfig.class)
@WebMvcTest(AdminController.class)
class AdminControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // We must mock *all* dependencies for AdminController AND SecurityConfig

    // For AdminController
    @MockitoBean
    private UserRepository userRepository;

    // For SecurityConfig
    @MockitoBean
    private MyUserDetailsService myUserDetailsService;

    @MockitoBean
    private JwtService jwtService;

    // JwtService needs JwtConfig
    @MockitoBean
    private JwtConfig jwtConfig;


    @Test
    @WithMockUser(roles = "ADMIN") // User HAS the correct role
    void setUserRole_Success_WithAdminRole() throws Exception {
        Set<users.rishik.BlogPlatform.Enums.Roles> roles = Set.of(users.rishik.BlogPlatform.Enums.Roles.AUTHOR);

        User mockUser = new User();
        mockUser.setId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);


        mockMvc.perform(put("/admin/users/roles/2")
                        .with(csrf()) // 2. Add a valid CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roles)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AUTHOR") // User DOES NOT have the role
    void setUserRole_Fails_WithAuthorRole() throws Exception {
        Set<users.rishik.BlogPlatform.Enums.Roles> roles = Set.of(users.rishik.BlogPlatform.Enums.Roles.AUTHOR);

        mockMvc.perform(put("/admin/users/roles/2")
                        .with(csrf()) // 2. Add CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roles)))
                .andExpect(status().isForbidden()); // Now fails at @PreAuthorize
    }

    @Test
    void setUserRole_Fails_Unauthenticated() throws Exception {
        // No @WithMockUser

        Set<users.rishik.BlogPlatform.Enums.Roles> roles = Set.of(users.rishik.BlogPlatform.Enums.Roles.AUTHOR);

        mockMvc.perform(put("/admin/users/roles/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roles)))
                .andExpect(status().isUnauthorized()); // Fails at the security filter
    }
}