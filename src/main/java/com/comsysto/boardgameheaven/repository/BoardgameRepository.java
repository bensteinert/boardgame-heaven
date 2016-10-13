package com.comsysto.boardgameheaven.repository;

import com.comsysto.boardgameheaven.domain.Boardgame;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Boardgame entity.
 */
@SuppressWarnings("unused")
public interface BoardgameRepository extends JpaRepository<Boardgame,Long> {

    @Query("select distinct boardgame from Boardgame boardgame left join fetch boardgame.categories left join fetch boardgame.users")
    List<Boardgame> findAllWithEagerRelationships();

    @Query("select boardgame from Boardgame boardgame left join fetch boardgame.categories left join fetch boardgame.users where boardgame.id =:id")
    Boardgame findOneWithEagerRelationships(@Param("id") Long id);

}
