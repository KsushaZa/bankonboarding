package ru.alfabank.practice.nadershinaka.bankonboarding.model.dto;

import java.util.List;

public class DadataResponse {
    private List<Suggestions> suggestions;

    public List<Suggestions> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestions> suggestions) {
        this.suggestions = suggestions;
    }
}
