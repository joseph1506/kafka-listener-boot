package com.joe.kafkalistenerboot.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    private Item[] results;
    private String nationality;
    private String seed;
    private String version;
}
