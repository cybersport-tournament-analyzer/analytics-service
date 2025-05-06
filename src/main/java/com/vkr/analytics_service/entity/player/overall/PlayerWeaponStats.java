package com.vkr.analytics_service.entity.player.overall;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "player_weapon_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class PlayerWeaponStats {

    @Id
    private String id; // steamId-weapon-scope-scopeId

    private String steamId;
    private String weapon;

    private String scope;    // match / series / tournament / global
    private String scopeId;  // matchId/ matchId / tournamentId / "global"
    private int seriesOrder;

    private int kills;
    private int headshots;
    private int wallbangs;
    private int noscopes;
    private int throughSmoke;
}
