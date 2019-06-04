package model;

import java.util.Vector;

public class User {
    private String id;
    private String name;
    private String url;
    private String at;
    private Vector<Card> Cards = new Vector<Card>();

    public User(String id, String name, String url, String at) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.at = at;
    }

    public Vector<Card> getCards() {
        return Cards;
    }

    public void setCards(Vector<Card> cards) {
        Cards = cards;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }
}
