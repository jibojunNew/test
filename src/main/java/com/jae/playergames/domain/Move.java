package com.jae.playergames.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Move implements Serializable {

    private Player player;

    private int addedValue;

    private int finalNumber;

}
