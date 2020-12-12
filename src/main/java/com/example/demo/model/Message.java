package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("MESSAGE")
public class Message {

    @Id
    private Integer id;
    @Column("NAME")
    private String message;
    @Column("DESCRIPTION")
    private String description;

    public Message (){
        super();
    }

    public Message(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
