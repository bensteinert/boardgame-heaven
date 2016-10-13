package com.comsysto.boardgameheaven.repository;

import com.comsysto.boardgameheaven.domain.Player;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Player entity.
 */
@SuppressWarnings("unused")
public interface PlayerRepository extends JpaRepository<Player,Long> {

}
