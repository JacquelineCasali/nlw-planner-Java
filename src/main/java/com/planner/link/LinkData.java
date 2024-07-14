package com.planner.link;

import java.time.LocalDateTime;

public record LinkData(
        Long id, String title,

        String url
) {
}
