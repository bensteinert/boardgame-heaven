package com.comsysto.boardgameheaven.repository;

import com.comsysto.boardgameheaven.domain.Category;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
@SuppressWarnings("unused")
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
