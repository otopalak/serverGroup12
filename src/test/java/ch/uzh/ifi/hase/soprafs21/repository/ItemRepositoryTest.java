package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Item item;
    private Tags tag;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("1234");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        //userRepository.save(user);

        //create tag
        tag = new Tags();
        tag.setDescription("Test");
        //tagsRepository.save(tag);

        //create Item
        item = new Item();
        //do not set ItemId here, it will be generated when item is inserted in the repo
        //otherwise the itemID is increased by one, this leads to misbehaving
        item.setUserId(user.getId());
        item.setDescription("Test Description");
        item.setTitle("Title");
        List<Tags> tags = new ArrayList<>();
        tags.add(tag);
        item.setItemtags(tags);
        //itemRepository.save(item);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findItemsByUserIdFound() {
        userRepository.save(user);
        tagsRepository.save(tag);
        itemRepository.save(item);

        List<Item> foundItems = itemRepository.findItemsByUserId(user.getId());
        assertEquals(foundItems.get(0).getDescription(), "Test Description");
    }

    @Test
    void findItemByID() {
        userRepository.save(user);
        tagsRepository.save(tag);
        itemRepository.save(item);

        assertEquals(itemRepository.findById(item.getId()).get(), item);
    }

    @Test
    @Disabled
    void findByIdFound() {
        //this is implemented by JPA and we actually do not have to test this.
        userRepository.save(user);
        tagsRepository.save(tag);
        itemRepository.save(item);

        User dbUser = userRepository.findById(user.getId()).get();
        Item dbItem = itemRepository.findById(item.getId()).get();
        assertEquals(dbUser.getId(), user.getId());
        assertEquals(dbItem.getId(), item.getId());
    }

    @Test
    @Disabled
    void findItemsByUserId() {
    }
}