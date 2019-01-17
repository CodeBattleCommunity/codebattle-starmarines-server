package com.epam.game.controller.forms;

/**
 * Form for createGame page
 * 
 * @author Roman_Spiridonov
 * 
 */
public class CreateGameForm {

    private String title;
    private String description;
    private int type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
