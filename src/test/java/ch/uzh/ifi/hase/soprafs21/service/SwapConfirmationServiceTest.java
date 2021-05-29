package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.SwapConfirmation;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LikeRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MatchRepository;
import ch.uzh.ifi.hase.soprafs21.repository.SwapConfirmationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class SwapConfirmationServiceTest {

    @Mock
    private SwapConfirmationRepository swapConfirmationRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private ItemRepository itemRepository;

    @Spy
    @InjectMocks
    private SwapConfirmationService service;

    private SwapConfirmation swapConfirmation;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        //given
        swapConfirmation = new SwapConfirmation();
        swapConfirmation.setItemID1(1L);
        swapConfirmation.setItemID2(2L);
        swapConfirmation.setItem1ConfirmsItem2(false);
        swapConfirmation.setItem2ConfirmsItem1(false);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createSwapConfirmation_noSwapConfExists() {
        swapConfirmation.setItem1ConfirmsItem2(true);
        Mockito.when(swapConfirmationRepository.save(Mockito.any())).thenReturn(swapConfirmation);

        SwapConfirmation created = service.createSwapConfirmation(swapConfirmation);

        assertEquals(swapConfirmation.getItemID1(), created.getItemID1());
        assertEquals(swapConfirmation.getItemID2(), created.getItemID2());
        assertEquals(true, created.getItem1ConfirmsItem2());
        assertEquals(false, created.getItem2ConfirmsItem1());
    }

    @Test
    void createSwapConfirmation_SwapConfExistsAlready() {
        Mockito.when(swapConfirmationRepository.findByItemID1AndItemID2(
                1L,2L)).thenReturn(swapConfirmation);

        SwapConfirmation repoSwapConf = service.createSwapConfirmation(swapConfirmation);

        assertEquals(repoSwapConf.getItem1ConfirmsItem2(),true);
        assertEquals(repoSwapConf.getItem2ConfirmsItem1(),false);
        assertEquals(swapConfirmation.getItemID1(),repoSwapConf.getItemID1());
        assertEquals(swapConfirmation.getItemID2(),repoSwapConf.getItemID2());
    }

    @Test
    void createSwapConfirmation_SwapConfExistsAlreadyOpponentConfirmedAlready() {
        //opponent has already confirmed
        swapConfirmation.setItem2ConfirmsItem1(true);

        //mock the method which would swap the items
        //@Spy annotation is needed to mock the method which is in the same class
        Mockito.doNothing().when(service).changeItemsIfConfirmed(swapConfirmation);

        Mockito.when(swapConfirmationRepository.findByItemID1AndItemID2(
                Mockito.anyLong(),Mockito.anyLong())).thenReturn(swapConfirmation);

        SwapConfirmation repoSwapConf = service.createSwapConfirmation(swapConfirmation);

        assertEquals(repoSwapConf.getItem1ConfirmsItem2(),true);
        assertEquals(repoSwapConf.getItem2ConfirmsItem1(),true);
        assertEquals(swapConfirmation.getItemID1(),repoSwapConf.getItemID1());
        assertEquals(swapConfirmation.getItemID2(),repoSwapConf.getItemID2());
    }

    @Test
    void createSwapConfirmation_SwapConfExistsAlreadyFromOpponent() {
        SwapConfirmation oppositeSwapConfirmation = new SwapConfirmation();
        oppositeSwapConfirmation.setItemID1(2L);
        oppositeSwapConfirmation.setItemID2(1L);
        oppositeSwapConfirmation.setItem2ConfirmsItem1(false);
        oppositeSwapConfirmation.setItem2ConfirmsItem1(false);

        Mockito.when(swapConfirmationRepository.findByItemID1AndItemID2(
                2L,1L)).thenReturn(oppositeSwapConfirmation);

        SwapConfirmation repoSwapConf = service.createSwapConfirmation(swapConfirmation);

        assertEquals(repoSwapConf.getItem1ConfirmsItem2(),false);
        assertEquals(repoSwapConf.getItem2ConfirmsItem1(),true);
        assertEquals(swapConfirmation.getItemID1(),repoSwapConf.getItemID2());
        assertEquals(swapConfirmation.getItemID2(),repoSwapConf.getItemID1());
    }

    @Test
    void cancelSwapConfirmation_existsAlready() {
        swapConfirmation.setItem1ConfirmsItem2(true);
        Mockito.when(swapConfirmationRepository.findByItemID1AndItemID2(
                1L,2L)).thenReturn(swapConfirmation);

        String message = service.cancelSwapConfirmation(1L,2L);

        assertEquals("swap canceled",message);
        assertEquals(swapConfirmation.getItem1ConfirmsItem2(), false);
        assertEquals(swapConfirmation.getItem2ConfirmsItem1(), false);
    }

    @Test
    void cancelSwapConfirmation_existsAlreadyFromOpponent() {
        SwapConfirmation oppositeSwapConfirmation = new SwapConfirmation();
        oppositeSwapConfirmation.setItemID1(2L);
        oppositeSwapConfirmation.setItemID2(1L);
        oppositeSwapConfirmation.setItem2ConfirmsItem1(true);
        oppositeSwapConfirmation.setItem2ConfirmsItem1(false);

        //returns opponent swapConfirmation
        Mockito.when(swapConfirmationRepository.findByItemID1AndItemID2(
                2L,1L)).thenReturn(oppositeSwapConfirmation);

        String message = service.cancelSwapConfirmation(1L,2L);

        assertEquals("swap canceled",message);
        assertEquals(oppositeSwapConfirmation.getItem2ConfirmsItem1(), false);
        assertEquals(oppositeSwapConfirmation.getItem1ConfirmsItem2(), false);
    }

    @Test
    void cancelSwapConfirmation_doesNotExist() {
        assertThrows(ResponseStatusException.class, () -> service.cancelSwapConfirmation(1L,2L));
    }

    @Test
    void changeItemsIfConfirmed_valid() {
        swapConfirmation.setItem2ConfirmsItem1(true);
        swapConfirmation.setItem2ConfirmsItem1(true);

        //unit test for this method does not make sense
        //this method should be covered in an integrationTest
        service.changeItemsIfConfirmed(swapConfirmation);
    }

    @Test
    void check_test1() {
        swapConfirmation.setItem1ConfirmsItem2(true);

        Mockito.when(swapConfirmationRepository.findByItemID1AndItemID2(
                1L,2L)).thenReturn(swapConfirmation);

        assertEquals(true,service.check(1L,2L));
    }

    @Test
    void check_test2() {
        swapConfirmation.setItem2ConfirmsItem1(true);

        Mockito.when(swapConfirmationRepository.findByItemID1AndItemID2(
                2L,1L)).thenReturn(swapConfirmation);

        assertEquals(true,service.check(1L,2L));
    }
    @Test
    void check_notFound() {
        assertEquals(false,service.check(1L,2L));
    }
}