package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerSpecification playerSpecification;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PlayerSpecification playerSpecification) {
        this.playerRepository = playerRepository;
        this.playerSpecification = playerSpecification;
    }

    public ResponseEntity<List<Player>> getAll(List<SearchCriteria> filters, Pageable pageable) {
        Page<Player> pagePlayers;
        pagePlayers = playerSpecification.getQueryResult(filters, pageable);

        List<Player> players;
        players = pagePlayers.getContent();

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getPlayersCount(List<SearchCriteria> filters, Pageable pageable) {
        Page<Player> pagePlayers;
        pagePlayers = playerSpecification.getQueryResult(filters, pageable);
        return new ResponseEntity<>((int) pagePlayers.getTotalElements(), HttpStatus.OK);
    }

    public ResponseEntity<Player> createPlayer(Player player) {

        if (player.getName() == null
                || player.getTitle() == null
                || player.getRace() == null
                || player.getProfession() == null
                || player.getBirthday() == null
                || player.getExperience() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (player.getName().length() > 12
                || player.getName().trim().length() == 0
                || player.getTitle().length() > 30
                || player.getExperience() < 0 || player.getExperience() > 10000000
                || player.getBirthday().getYear() + 1900 < 2000
                || player.getBirthday().getYear() + 1900 > 3000) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.isBanned() == null) {
            player.setBanned(false);
        }
        Integer level = (int) (Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100;
        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);

        return new ResponseEntity<>(playerRepository.save(player), HttpStatus.OK);
    }

    public ResponseEntity<?> getPlayerByID(String id) {
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.OK);
        if (!response.equals(checkId(id))) {
            return checkId(id);
        }

        Player player = playerRepository.findById(Long.parseLong(id)).get();
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    public ResponseEntity<?> updatePlayerByID(String id, Player player) {
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.OK);
        if (!response.equals(checkId(id))) {
            return checkId(id);
        }

        Player updatablePlayer = playerRepository.findById(Long.parseLong(id)).get();

        if ((player.getName() == null || player.getName().trim().length() == 0)
                && (player.getTitle() == null || player.getTitle().trim().length() == 0)
                && player.getBirthday() == null
                && player.getRace() == null
                && player.getProfession() == null
                && player.getExperience() == null
                && player.isBanned() == null) {
            updatablePlayer =  playerRepository.save(updatablePlayer);
            return new ResponseEntity<>(updatablePlayer, HttpStatus.OK);
        }


        if (player.getName() != null && player.getName().trim().length() != 0) {
            if(player.getName().length() > 12) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            updatablePlayer.setName(player.getName());
        }

        if (player.getTitle() != null && player.getTitle().trim().length() != 0) {
            if(player.getTitle().length() > 30) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            updatablePlayer.setTitle(player.getTitle());
        }
        if (player.getRace() != null) {
            updatablePlayer.setRace(player.getRace());
        }
        if (player.getProfession() != null) {
            updatablePlayer.setProfession(player.getProfession());
        }
        if (player.isBanned() != null) {
            updatablePlayer.setBanned(player.isBanned());
        }
        if (player.getBirthday() != null) {
            if(player.getBirthday().getYear() + 1900 < 2000
                    || player.getBirthday().getYear() + 1900 > 3000) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            updatablePlayer.setBirthday(player.getBirthday());
        }
        if (player.getExperience() != null ) {
            if(player.getExperience() < 0
                    || player.getExperience() > 10000000) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            updatablePlayer.setExperience(player.getExperience());
            Integer level = (int) (Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100;
            Integer untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
            updatablePlayer.setLevel(level);
            updatablePlayer.setUntilNextLevel(untilNextLevel);
        }

        updatablePlayer =  playerRepository.save(updatablePlayer);

        return new ResponseEntity<>(updatablePlayer, HttpStatus.OK);
    }

    public ResponseEntity<?> deletePlayerById(String id) {
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.OK);
        if (!response.equals(checkId(id))) {
            return checkId(id);
        }

        playerRepository.deleteById(Long.parseLong(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<?> checkId(String id) {
        Long localId = 0L;
        try {
            localId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (localId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (playerRepository.existsById(localId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public Pageable getPageable(int page, int size, PlayerOrder order) {
        Sort sort = Sort.by(order.getFieldName());
        return PageRequest.of(page, size, sort);
    }

    public List<SearchCriteria> getFilters(Boolean banned, String name, String title, Race race,
                                           Profession profession, Integer minLevel, Integer maxLevel,
                                           Integer minExperience, Integer maxExperience,
                                           Long before, Long after) {
        List<SearchCriteria> filters = new ArrayList<>();
        if (name != null) {
            filters.add(new SearchCriteria("name", SearchOperation.LIKE, name));
        }
        if (title != null) {
            filters.add(new SearchCriteria("title", SearchOperation.LIKE, title));
        }
        if (banned != null) {
            filters.add(new SearchCriteria("banned", SearchOperation.EQUALITY, banned.toString()));
        }
        if (race != null) {
            filters.add(new SearchCriteria("race", SearchOperation.EQUALITY, race.name()));
        }
        if (profession != null) {
            filters.add(new SearchCriteria("profession", SearchOperation.EQUALITY, profession.name()));
        }
        if (minLevel != null) {
            filters.add(new SearchCriteria("level", SearchOperation.GREATER_THAN, Integer.valueOf(minLevel - 1).toString()));
        }
        if (maxLevel != null) {
            filters.add(new SearchCriteria("level", SearchOperation.LESS_THAN, Integer.valueOf(1 + maxLevel).toString()));
        }
        if (minExperience != null) {
            filters.add(new SearchCriteria("experience", SearchOperation.GREATER_THAN, Integer.valueOf(minExperience - 1).toString()));
        }
        if (maxExperience != null) {
            filters.add(new SearchCriteria("experience", SearchOperation.LESS_THAN, Integer.valueOf(1 + maxExperience).toString()));
        }
        if (before != null) {
            filters.add(new SearchCriteria("birthday", SearchOperation.LESS_THAN_DATE, before.toString()));
        }
        if (after != null) {
            filters.add(new SearchCriteria("birthday", SearchOperation.GREATER_THAN_DATE, after.toString()));
        }
        return filters;
    }
}