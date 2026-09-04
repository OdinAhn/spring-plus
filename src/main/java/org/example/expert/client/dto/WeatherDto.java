package org.example.expert.client.dto;

import lombok.Getter;

@Getter
public class WeatherDto {

    private final String date;
    private final String weather;

    public WeatherDto(String date, String weather) {
        this.date = date;
        this.weather = weather;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String date;
        private String weather;

        public Builder date(String date) { this.date = date; return this; }
        public Builder weather(String weather) { this.weather = weather; return this; }

        public WeatherDto build() {
            return new WeatherDto(date, weather);
        }
    }
}
