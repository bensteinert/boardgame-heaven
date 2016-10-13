package com.comsysto.boardgameheaven.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "players")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Boardgame> boardgames = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public Player nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User getUser() {
        return user;
    }

    public Player user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Boardgame> getBoardgames() {
        return boardgames;
    }

    public Player boardgames(Set<Boardgame> boardgames) {
        this.boardgames = boardgames;
        return this;
    }

    public Player addBoardgame(Boardgame boardgame) {
        boardgames.add(boardgame);
        boardgame.getPlayers().add(this);
        return this;
    }

    public Player removeBoardgame(Boardgame boardgame) {
        boardgames.remove(boardgame);
        boardgame.getPlayers().remove(this);
        return this;
    }

    public void setBoardgames(Set<Boardgame> boardgames) {
        this.boardgames = boardgames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        if(player.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + id +
            ", nickname='" + nickname + "'" +
            '}';
    }
}
