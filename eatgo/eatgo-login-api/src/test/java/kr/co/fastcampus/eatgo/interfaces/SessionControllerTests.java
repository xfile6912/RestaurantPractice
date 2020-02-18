package kr.co.fastcampus.eatgo.interfaces;

import kr.co.fastcampus.eatgo.application.EmailNotExistedException;
import kr.co.fastcampus.eatgo.application.PasswordWrongException;
import kr.co.fastcampus.eatgo.application.UserService;
import kr.co.fastcampus.eatgo.domain.User;
import kr.co.fastcampus.eatgo.utils.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SessionController.class)
public class SessionControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;


    @Test
    public void createwithValid() throws Exception {


        String email="tester@example.com";
        String password="test";
        Long id=1004L;
        String name="John";


        User mockUser=User.builder().id(id).name(name).level(1L).build();
        given(userService.authenticate(email,password)).willReturn(mockUser);
        given(jwtUtil.createToken(id,name,  null)).willReturn("header.payload.signature");

            mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"name\":\"Tester\",\"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location","/session"))
                .andExpect(content().string(containsString("{\"accessToken\":\"header.payload.signature\"}")))
                    .andExpect(content().string(containsString(".")));
        verify(userService).authenticate(eq(email),eq(password));
    }
    @Test
    public void createRestaurantOwner() throws Exception {


        String email="tester@example.com";
        String password="test";
        Long id=1004L;
        String name="John";


        User mockUser=User.builder().id(id).name(name).level(50L).restaurantId(369L).build();
        given(userService.authenticate(email,password)).willReturn(mockUser);
        given(jwtUtil.createToken(id,name,  369L)).willReturn("header.payload.signature");

        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"name\":\"Tester\",\"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location","/session"))
                .andExpect(content().string(containsString("{\"accessToken\":\"header.payload.signature\"}")))
                .andExpect(content().string(containsString(".")));
        verify(userService).authenticate(eq(email),eq(password));
    }
    @Test
    public void createwithInvalidEmail() throws Exception {

        given(userService.authenticate("x","test")).willThrow(new EmailNotExistedException("x"));
        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"x\",\"name\":\"Tester\",\"password\":\"test\"}"))
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq("x"),eq("test"));
    }
    @Test
    public void createwithInvalidPassword() throws Exception {

        given(userService.authenticate("tester@example.com","x")).willThrow(new PasswordWrongException());
        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"name\":\"Tester\",\"password\":\"x\"}"))
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq("tester@example.com"),eq("x"));
    }
}