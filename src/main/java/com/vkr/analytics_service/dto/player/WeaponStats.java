package com.vkr.analytics_service.dto.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class WeaponStats {
    private int kills;
    private int headshots;
    private int wallbangs;
    private int noscopes;
    private int throughSmoke;
}
