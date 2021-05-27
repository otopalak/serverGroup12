package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Tags;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;

    @DataJpaTest
    public class TagsRepositoryTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private TagsRepository tagsRepository;



        private User user;
        private Item item;
        private Tags tag;

        @BeforeEach
        void setUp() {
            //create tags
            tag = new Tags();
            tag.setId(1L);
            tag.setDescription("Tag 1");

        }

        @AfterEach
        void tearDown() {
        }

        @Test
        void findAllTags(){
            tagsRepository.save(tag);

            List<Tags> foundTags = tagsRepository.findAll();
            assertEquals(foundTags.get(0).getDescription(),"Tag 1");

        }



    }