package com.withleaf.withleaf.controller;


import com.withleaf.withleaf.form.CharacterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.withleaf.withleaf.model.Character;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    public static List<Character> characters=new ArrayList<>();
    static {
        characters.add(new Character(1, new String("Kaaris"), new String("Guerrier")));
        characters.add(new Character(2, new String("Booba"), new String("Guerrier")));
        characters.add(new Character(3, new String("La fouine"), new String("Sorcier")));
    }

    // Injectez (inject) via application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("message", message);

        return "index";
    }

    @RequestMapping(value = { "/charactersList" }, method = RequestMethod.GET)
    public String personList(Model model) {

        model.addAttribute("characters", characters);

        return "charactersList";
    }

    @RequestMapping(value = { "/addCharacter" }, method = RequestMethod.GET)
    public String showAddCharacterPage(Model model) {

        CharacterForm characterForm = new CharacterForm();
        model.addAttribute("characterForm", characterForm);

        return "addCharacter";
    }

    @RequestMapping(value = { "/addCharacter" }, method = RequestMethod.POST)
    public String saveCharacter(Model model, @ModelAttribute("characterForm") CharacterForm characterForm) {

        String name = characterForm.getName();
        String type = characterForm.getType();

        if (name != null && name.length() > 0 //
                && type != null && type.length() > 0) {
            Character newCharacter = new Character(90, name, type);
            characters.add(newCharacter);

            return "redirect:/charactersList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCharacter";
    }

}