package com.fakecoders.foundation.quickstart.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Mail {
    private String from;
    private String to;
    private String subject;
    private String content;
    private Map<String, String> model;
    
    public Mail() {
        model = new HashMap<>();
    }
   
}
