package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Item;
import ch.uzh.ifi.hase.soprafs21.entity.Matches;
import ch.uzh.ifi.hase.soprafs21.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs21.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

    @Autowired
    private final ItemRepository itemRepository;
    private final MatchRepository matchRepository;

    public ItemService(ItemRepository itemRepository, MatchRepository matchRepository) {
        this.itemRepository = itemRepository;
        this.matchRepository = matchRepository;
    }

    public Item createItem(Item itemToCreate){
        itemToCreate = itemRepository.save(itemToCreate);
        itemRepository.flush();
        return itemToCreate;
    }

    // here just temporarely to test the chat feature
    public void createMatch(Long idOne, Long idTwo){
        Matches newMatch = new Matches();
        newMatch.setItemIdOne(idOne);
        newMatch.setItemIdTwo(idTwo);
        matchRepository.save(newMatch);
    }

    // also to move...
    public List<Matches> findMatchesForId(Long id){
        List<Matches> fromIdOne = matchRepository.findByItemIdOne(id);
        List<Matches> fromIdTwo = matchRepository.findByItemIdTwo(id);
        return Stream.concat(fromIdOne.stream(), fromIdTwo.stream())
                .collect(Collectors.toList());
    }
}
