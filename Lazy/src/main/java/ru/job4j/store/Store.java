package ru.job4j.store;

import ru.job4j.model.Model;
import ru.job4j.model.ModelAuto;

import java.util.List;

public interface Store {
    Model add(Model model);

    List<ModelAuto> getAllModels();
}
