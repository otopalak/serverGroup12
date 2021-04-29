package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Like;
import ch.uzh.ifi.hase.soprafs21.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final MatchService matchService;

    @Autowired
    public LikeService(@Qualifier("likeRepository") LikeRepository likeRepository, @Qualifier("matchService") MatchService matchService) {
        this.likeRepository = likeRepository;
        this.matchService = matchService;
    }
    public List<Like> getAllLikes(){
        return this.likeRepository.findAll();
    }

    public void createLike(Like likeInput) {
        //check if like already exists by itemIDSwiper and itemIDSwiped
        Long itemIDSwiper = likeInput.getItemIDSwiper();
        Long itemIDSwiped = likeInput.getItemIDSwiped();
        for(Like repoLike: likeRepository.findAll()){
            //if so, overwrite Boolean liked to current value
            //in normal case you can only overwrite from false to true, because true items would not be shown again to swipe.
            if(itemIDSwiper.equals(repoLike.getItemIDSwiper()) && itemIDSwiped.equals(repoLike.getItemIDSwiped())){
                repoLike.setLiked(likeInput.getLiked());
                checkLikeForMatch(repoLike);
                return;
            }
        }
        //add new Like to LikeRepository
        likeRepository.save(likeInput);
        likeRepository.flush();

        //check for new match
        checkLikeForMatch(likeInput);

    }
    public void deleteLikeByLikeId(long likeId){
        likeRepository.deleteById(likeId);
    }
    public void checkLikeForMatch(Like like){
        if (like.getLiked()){
            long itemIDSwiper = like.getItemIDSwiper();
            long itemIDSwiped = like.getItemIDSwiped();
            Like likeOpponent = likeRepository.findByItemIDSwipedAndItemIDSwiper(itemIDSwiper, itemIDSwiped);
            if (likeOpponent != null) {
                if (likeOpponent.getLiked()){
                    //create match
                    matchService.createMatch(like.getItemIDSwiper(), like.getItemIDSwiped());
                }
            }
        }
    }
}
