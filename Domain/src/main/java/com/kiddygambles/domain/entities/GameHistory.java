package com.kiddygambles.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameHistory {
    private int rolledNumber;
    private int wonTokens = 0;
    private boolean won = false;
}
