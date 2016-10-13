package com.comsysto.boardgameheaven.web.rest;

import com.comsysto.boardgameheaven.BoardgameHeavenApp;

import com.comsysto.boardgameheaven.domain.Boardgame;
import com.comsysto.boardgameheaven.repository.BoardgameRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.comsysto.boardgameheaven.domain.enumeration.Difficulty;
/**
 * Test class for the BoardgameResource REST controller.
 *
 * @see BoardgameResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BoardgameHeavenApp.class)
public class BoardgameResourceIntTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";

    private static final Integer DEFAULT_MIN_NUMBER_OF_PLAYERS = 1;
    private static final Integer UPDATED_MIN_NUMBER_OF_PLAYERS = 2;

    private static final Integer DEFAULT_MAX_NUMBER_OF_PLAYERS = 1;
    private static final Integer UPDATED_MAX_NUMBER_OF_PLAYERS = 2;

    private static final Integer DEFAULT_MIN_DURATION = 1;
    private static final Integer UPDATED_MIN_DURATION = 2;

    private static final Integer DEFAULT_MAX_DURATION = 1;
    private static final Integer UPDATED_MAX_DURATION = 2;

    private static final Integer DEFAULT_MIN_AGE_PLAYER = 1;
    private static final Integer UPDATED_MIN_AGE_PLAYER = 2;

    private static final Difficulty DEFAULT_DIFFICULTY = Difficulty.EASY;
    private static final Difficulty UPDATED_DIFFICULTY = Difficulty.INTERMEDIATE;
    private static final String DEFAULT_RELEASE_YEAR = "AAAAA";
    private static final String UPDATED_RELEASE_YEAR = "BBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Inject
    private BoardgameRepository boardgameRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBoardgameMockMvc;

    private Boardgame boardgame;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BoardgameResource boardgameResource = new BoardgameResource();
        ReflectionTestUtils.setField(boardgameResource, "boardgameRepository", boardgameRepository);
        this.restBoardgameMockMvc = MockMvcBuilders.standaloneSetup(boardgameResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boardgame createEntity(EntityManager em) {
        Boardgame boardgame = new Boardgame()
                .name(DEFAULT_NAME)
                .minNumberOfPlayers(DEFAULT_MIN_NUMBER_OF_PLAYERS)
                .maxNumberOfPlayers(DEFAULT_MAX_NUMBER_OF_PLAYERS)
                .minDuration(DEFAULT_MIN_DURATION)
                .maxDuration(DEFAULT_MAX_DURATION)
                .minAgePlayer(DEFAULT_MIN_AGE_PLAYER)
                .difficulty(DEFAULT_DIFFICULTY)
                .releaseYear(DEFAULT_RELEASE_YEAR)
                .rating(DEFAULT_RATING)
                .description(DEFAULT_DESCRIPTION)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return boardgame;
    }

    @Before
    public void initTest() {
        boardgame = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoardgame() throws Exception {
        int databaseSizeBeforeCreate = boardgameRepository.findAll().size();

        // Create the Boardgame

        restBoardgameMockMvc.perform(post("/api/boardgames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardgame)))
                .andExpect(status().isCreated());

        // Validate the Boardgame in the database
        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(databaseSizeBeforeCreate + 1);
        Boardgame testBoardgame = boardgames.get(boardgames.size() - 1);
        assertThat(testBoardgame.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBoardgame.getMinNumberOfPlayers()).isEqualTo(DEFAULT_MIN_NUMBER_OF_PLAYERS);
        assertThat(testBoardgame.getMaxNumberOfPlayers()).isEqualTo(DEFAULT_MAX_NUMBER_OF_PLAYERS);
        assertThat(testBoardgame.getMinDuration()).isEqualTo(DEFAULT_MIN_DURATION);
        assertThat(testBoardgame.getMaxDuration()).isEqualTo(DEFAULT_MAX_DURATION);
        assertThat(testBoardgame.getMinAgePlayer()).isEqualTo(DEFAULT_MIN_AGE_PLAYER);
        assertThat(testBoardgame.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);
        assertThat(testBoardgame.getReleaseYear()).isEqualTo(DEFAULT_RELEASE_YEAR);
        assertThat(testBoardgame.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testBoardgame.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBoardgame.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBoardgame.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardgameRepository.findAll().size();
        // set the field null
        boardgame.setName(null);

        // Create the Boardgame, which fails.

        restBoardgameMockMvc.perform(post("/api/boardgames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardgame)))
                .andExpect(status().isBadRequest());

        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinNumberOfPlayersIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardgameRepository.findAll().size();
        // set the field null
        boardgame.setMinNumberOfPlayers(null);

        // Create the Boardgame, which fails.

        restBoardgameMockMvc.perform(post("/api/boardgames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardgame)))
                .andExpect(status().isBadRequest());

        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxNumberOfPlayersIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardgameRepository.findAll().size();
        // set the field null
        boardgame.setMaxNumberOfPlayers(null);

        // Create the Boardgame, which fails.

        restBoardgameMockMvc.perform(post("/api/boardgames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardgame)))
                .andExpect(status().isBadRequest());

        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardgameRepository.findAll().size();
        // set the field null
        boardgame.setMinDuration(null);

        // Create the Boardgame, which fails.

        restBoardgameMockMvc.perform(post("/api/boardgames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardgame)))
                .andExpect(status().isBadRequest());

        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = boardgameRepository.findAll().size();
        // set the field null
        boardgame.setMaxDuration(null);

        // Create the Boardgame, which fails.

        restBoardgameMockMvc.perform(post("/api/boardgames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boardgame)))
                .andExpect(status().isBadRequest());

        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoardgames() throws Exception {
        // Initialize the database
        boardgameRepository.saveAndFlush(boardgame);

        // Get all the boardgames
        restBoardgameMockMvc.perform(get("/api/boardgames?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(boardgame.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].minNumberOfPlayers").value(hasItem(DEFAULT_MIN_NUMBER_OF_PLAYERS)))
                .andExpect(jsonPath("$.[*].maxNumberOfPlayers").value(hasItem(DEFAULT_MAX_NUMBER_OF_PLAYERS)))
                .andExpect(jsonPath("$.[*].minDuration").value(hasItem(DEFAULT_MIN_DURATION)))
                .andExpect(jsonPath("$.[*].maxDuration").value(hasItem(DEFAULT_MAX_DURATION)))
                .andExpect(jsonPath("$.[*].minAgePlayer").value(hasItem(DEFAULT_MIN_AGE_PLAYER)))
                .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())))
                .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void getBoardgame() throws Exception {
        // Initialize the database
        boardgameRepository.saveAndFlush(boardgame);

        // Get the boardgame
        restBoardgameMockMvc.perform(get("/api/boardgames/{id}", boardgame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boardgame.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.minNumberOfPlayers").value(DEFAULT_MIN_NUMBER_OF_PLAYERS))
            .andExpect(jsonPath("$.maxNumberOfPlayers").value(DEFAULT_MAX_NUMBER_OF_PLAYERS))
            .andExpect(jsonPath("$.minDuration").value(DEFAULT_MIN_DURATION))
            .andExpect(jsonPath("$.maxDuration").value(DEFAULT_MAX_DURATION))
            .andExpect(jsonPath("$.minAgePlayer").value(DEFAULT_MIN_AGE_PLAYER))
            .andExpect(jsonPath("$.difficulty").value(DEFAULT_DIFFICULTY.toString()))
            .andExpect(jsonPath("$.releaseYear").value(DEFAULT_RELEASE_YEAR.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingBoardgame() throws Exception {
        // Get the boardgame
        restBoardgameMockMvc.perform(get("/api/boardgames/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoardgame() throws Exception {
        // Initialize the database
        boardgameRepository.saveAndFlush(boardgame);
        int databaseSizeBeforeUpdate = boardgameRepository.findAll().size();

        // Update the boardgame
        Boardgame updatedBoardgame = boardgameRepository.findOne(boardgame.getId());
        updatedBoardgame
                .name(UPDATED_NAME)
                .minNumberOfPlayers(UPDATED_MIN_NUMBER_OF_PLAYERS)
                .maxNumberOfPlayers(UPDATED_MAX_NUMBER_OF_PLAYERS)
                .minDuration(UPDATED_MIN_DURATION)
                .maxDuration(UPDATED_MAX_DURATION)
                .minAgePlayer(UPDATED_MIN_AGE_PLAYER)
                .difficulty(UPDATED_DIFFICULTY)
                .releaseYear(UPDATED_RELEASE_YEAR)
                .rating(UPDATED_RATING)
                .description(UPDATED_DESCRIPTION)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restBoardgameMockMvc.perform(put("/api/boardgames")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBoardgame)))
                .andExpect(status().isOk());

        // Validate the Boardgame in the database
        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(databaseSizeBeforeUpdate);
        Boardgame testBoardgame = boardgames.get(boardgames.size() - 1);
        assertThat(testBoardgame.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBoardgame.getMinNumberOfPlayers()).isEqualTo(UPDATED_MIN_NUMBER_OF_PLAYERS);
        assertThat(testBoardgame.getMaxNumberOfPlayers()).isEqualTo(UPDATED_MAX_NUMBER_OF_PLAYERS);
        assertThat(testBoardgame.getMinDuration()).isEqualTo(UPDATED_MIN_DURATION);
        assertThat(testBoardgame.getMaxDuration()).isEqualTo(UPDATED_MAX_DURATION);
        assertThat(testBoardgame.getMinAgePlayer()).isEqualTo(UPDATED_MIN_AGE_PLAYER);
        assertThat(testBoardgame.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
        assertThat(testBoardgame.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testBoardgame.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testBoardgame.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBoardgame.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBoardgame.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteBoardgame() throws Exception {
        // Initialize the database
        boardgameRepository.saveAndFlush(boardgame);
        int databaseSizeBeforeDelete = boardgameRepository.findAll().size();

        // Get the boardgame
        restBoardgameMockMvc.perform(delete("/api/boardgames/{id}", boardgame.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Boardgame> boardgames = boardgameRepository.findAll();
        assertThat(boardgames).hasSize(databaseSizeBeforeDelete - 1);
    }
}
