package com.vkr.analytics_service.dto.pickban;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PickBanAction {

    private String team; //team1 team2
    private Action action; //ban pick
    private String mapOrSide; //de_dust2 de_inferno ct t
}