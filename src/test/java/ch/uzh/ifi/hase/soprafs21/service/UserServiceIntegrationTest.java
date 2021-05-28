package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    // Checks, if the the username != null
    @Test
    public void update_user_test(){
        // given
        User currentUser1 = new User();
        currentUser1.setName("testName");
        currentUser1.setUsername("testUsername");
        currentUser1.setPassword("123");
        User currentUser = userService.createUser(currentUser1);

        User userInput = new User();
        userInput.setName("newName");
        userInput.setPassword("1243");

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(currentUser,userInput));
    }

    // Checks if the password isn't blank
    @Test
    public void update_user_test_2(){
        // given
        User currentUser1 = new User();
        currentUser1.setName("testName");
        currentUser1.setUsername("testUsername");
        currentUser1.setPassword("123");
        User currentUser = userService.createUser(currentUser1);

        User userInput = new User();
        userInput.setName("newName");
        userInput.setUsername("Hallo");
        userInput.setPassword("");

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(currentUser,userInput));
    }

    @Test
    public void update_user_test_3(){
        // given
        User currentUser1 = new User();
        currentUser1.setName("testName");
        currentUser1.setUsername("testUsername");
        currentUser1.setPassword("123");
        User currentUser = userService.createUser(currentUser1);

        User userInput = new User();
        userInput.setName("newName");
        userInput.setPassword("1233");
        userInput.setUsername("Hallo");
        userInput.setAddress("");
        userInput.setCity("");

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(currentUser,userInput));
    }
    @Test
    public void update_user_test_4(){
        // given
        User currentUser1 = new User();
        currentUser1.setName("hdhdh");
        currentUser1.setUsername("hdhdh");
        currentUser1.setPassword("123");
        User currentUser = userService.createUser(currentUser1);

        User currentUser2 = new User();
        currentUser2.setName("DennisShushack");
        currentUser2.setUsername("dennis");
        currentUser2.setPassword("dennis");
        userService.createUser(currentUser2);

        User userInput = new User();
        userInput.setName("newName");
        userInput.setUsername("dennis");
        userInput.setPassword("1233");
        userInput.setAddress("22");
        userInput.setCity("22");

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(currentUser,userInput));
    }

    @Test
    public void update_user_test_5(){
        // given
        User currentUser1 = new User();
        currentUser1.setName("hdhdh");
        currentUser1.setUsername("hdhdh");
        currentUser1.setPassword("123");
        User currentUser = userService.createUser(currentUser1);


        User userInput = new User();
        userInput.setName("newName");
        userInput.setUsername("dennis");
        userInput.setPassword("1233");
        userInput.setAddress("22");
        userInput.setCity("22");

        User EndUser = userService.updateUser(currentUser,userInput);

        assertEquals(EndUser.getAddress(),userInput.getAddress());
        assertEquals(EndUser.getCity(),userInput.getCity());
        assertEquals(EndUser.getUsername(),userInput.getUsername());
        assertEquals(EndUser.getPassword(),userInput.getPassword());


    }

    @Test
    public void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("123");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("123");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the name but forget about the username
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
    }
    // This method checks that a dublicate creation of a user with the same name throws an error
    @Test
    public void createUser_duplicateName_throwsException() {
        assertNull(userRepository.findByName("testName"));

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("v1234");

        //when
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same name
        User testUser2 = new User();

        testUser2.setName("testName");
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("v1234");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
    }

    // test method checks, if the getUserbyId method
    @Test
    public void check_getUserById_success() {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("v1234");

        User createdUser = userService.createUser(testUser);

        // when
        User found = userService.getUserbyID(createdUser.getId());

        // then
        assertEquals(testUser.getId(), found.getId());
    }



    //tests Login, if Login has valid Inputs
    @Test
    public void LoginUser_validInputs_success() {
        //given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("v1234");
        User createdUser = userService.createUser(testUser);
        // when
        User loggedInUser = userService.checkLoginCredentials(createdUser);

        //then
        assertEquals(testUser.getUsername(), loggedInUser.getUsername());
        assertEquals(testUser.getPassword(), loggedInUser.getPassword());
        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
    }

    //test method for Logout
    @Test
    public void LogoutUser_success() {

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("v1234");
        User createdUser = userService.createUser(testUser);

        // when
        User loggedOutUser = userService.setUserOffline(createdUser);

        // then
        assertEquals(loggedOutUser.getStatus(), UserStatus.OFFLINE);
    }


    //tests Login with a wrong password given
    @Test
    public void LoginUser_with_WrongPassword_throwsException() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("v1234");
        User createdUser = userService.createUser(testUser);

        //create input user
        User testUser2 = new User();
        // wrong password input
        testUser2.setUsername("testUsername");
        testUser2.setPassword("wrongPassword");

        // when
        User loggedInUser = userService.checkLoginCredentials(createdUser);

        // Is an error thrown
        assertThrows(ResponseStatusException.class, () -> userService.checkLoginCredentials(testUser2));
    }

}
