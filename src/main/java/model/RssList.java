package model;

import lombok.Data;

@Data
public class RssList {
    private int id;
    private String source;
    private String link;
    private Boolean isActive;
}
