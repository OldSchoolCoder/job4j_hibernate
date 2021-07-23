package ru.job4j.store;

import ru.job4j.model.Candidate;

public interface Store {
    Candidate add(Candidate candidate);
}
