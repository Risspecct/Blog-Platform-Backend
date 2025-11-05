package users.rishik.BlogPlatform.Controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import users.rishik.BlogPlatform.Repositories.CommentRepository;
import users.rishik.BlogPlatform.Repositories.PostRepository;
import users.rishik.BlogPlatform.Security.RoleHierarchyConfig;
import users.rishik.BlogPlatform.Security.SecurityConfig;
import users.rishik.BlogPlatform.Services.JwtService;
import users.rishik.BlogPlatform.Services.MyUserDetailsService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModeratorController.class)
@Import({SecurityConfig.class, RoleHierarchyConfig.class})
class ModeratorControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock direct dependencies
    @MockitoBean
    private CommentRepository commentRepository;
    @MockitoBean
    private PostRepository postRepository;

    // Mock security dependencies
    @MockitoBean
    private MyUserDetailsService myUserDetailsService;
    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser(roles = "MOD") // User has the exact role
    void deletePost_Success_WithModRole() throws Exception {
        mockMvc.perform(delete("/mod/delete/posts/10")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Admin role is higher than MOD
    void deletePost_Success_WithAdminRole() throws Exception {
        // This test proves your RoleHierarchyConfig is working
        mockMvc.perform(delete("/mod/delete/posts/10")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AUTHOR") // Author role is lower than MOD
    void deletePost_Fails_WithAuthorRole() throws Exception {
        mockMvc.perform(delete("/mod/delete/posts/10")
                        .with(csrf()))
                .andExpect(status().isForbidden()); // Expect 403
    }
}