package com.planner.activity;

import java.time.LocalDateTime;

public record ActivityData(Long id, String title,
LocalDateTime occursAt) {
}
