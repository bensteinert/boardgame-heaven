package com.comsysto.boardgameheaven.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.comsysto.boardgameheaven.domain.enumeration.Difficulty;

/**
 * A Boardgame.
 */
@Entity
@Table(name = "boardgame")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Boardgame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "min_number_of_players", nullable = false)
    private Integer minNumberOfPlayers;

    @NotNull
    @Column(name = "max_number_of_players", nullable = false)
    private Integer maxNumberOfPlayers;

    @NotNull
    @Column(name = "min_duration", nullable = false)
    private Integer minDuration;

    @NotNull
    @Column(name = "max_duration", nullable = false)
    private Integer maxDuration;

    @Column(name = "min_age_player")
    private Integer minAgePlayer;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "release_year")
    private String releaseYear;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "boardgame_category",
               joinColumns = @JoinColumn(name="boardgames_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="categories_id", referencedColumnName="ID"))
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "boardgame_user",
               joinColumns = @JoinColumn(name="boardgames_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="ID"))
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Boardgame name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinNumberOfPlayers() {
        return minNumberOfPlayers;
    }

    public Boardgame minNumberOfPlayers(Integer minNumberOfPlayers) {
        this.minNumberOfPlayers = minNumberOfPlayers;
        return this;
    }

    public void setMinNumberOfPlayers(Integer minNumberOfPlayers) {
        this.minNumberOfPlayers = minNumberOfPlayers;
    }

    public Integer getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public Boardgame maxNumberOfPlayers(Integer maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        return this;
    }

    public void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public Boardgame minDuration(Integer minDuration) {
        this.minDuration = minDuration;
        return this;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public Boardgame maxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
        return this;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMinAgePlayer() {
        return minAgePlayer;
    }

    public Boardgame minAgePlayer(Integer minAgePlayer) {
        this.minAgePlayer = minAgePlayer;
        return this;
    }

    public void setMinAgePlayer(Integer minAgePlayer) {
        this.minAgePlayer = minAgePlayer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Boardgame difficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public Boardgame releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getRating() {
        return rating;
    }

    public Boardgame rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public Boardgame description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public Boardgame image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Boardgame imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Boardgame categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Boardgame addCategory(Category category) {
        categories.add(category);
        category.getBoardgames().add(this);
        return this;
    }

    public Boardgame removeCategory(Category category) {
        categories.remove(category);
        category.getBoardgames().remove(this);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Boardgame users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Boardgame addUser(User user) {
        users.add(user);
        user.getBoardgames().add(this);
        return this;
    }

    public Boardgame removeUser(User user) {
        users.remove(user);
        user.getBoardgames().remove(this);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Boardgame boardgame = (Boardgame) o;
        if(boardgame.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, boardgame.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Boardgame{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", minNumberOfPlayers='" + minNumberOfPlayers + "'" +
            ", maxNumberOfPlayers='" + maxNumberOfPlayers + "'" +
            ", minDuration='" + minDuration + "'" +
            ", maxDuration='" + maxDuration + "'" +
            ", minAgePlayer='" + minAgePlayer + "'" +
            ", difficulty='" + difficulty + "'" +
            ", releaseYear='" + releaseYear + "'" +
            ", rating='" + rating + "'" +
            ", description='" + description + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            '}';
    }
}
