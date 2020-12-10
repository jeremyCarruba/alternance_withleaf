package com.withleaf.withleaf.controller;


import com.withleaf.withleaf.form.CharacterForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import com.withleaf.withleaf.model.Character;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class MainController {

    private static final Logger log = Logger.getLogger(MainController.class.getName());
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String baseUrl = "http://localhost:8081/";

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

        Character[] characters = restTemplate.getForObject(baseUrl + "characters", Character[].class);
        model.addAttribute("characters", characters);

        return "charactersList";
    }

    @GetMapping(value = {"/characters/{id}"})
    public String getCharacter(@PathVariable int id, Model model){

        Character character = restTemplate.getForObject(baseUrl + "characters/" + id, Character.class);
        model.addAttribute("character", character);

        return "character";
    }

    @GetMapping(value = {"/characters/delete/{id}"})
    public String deleteCharacter(@PathVariable int id, Model model){

        restTemplate.delete(baseUrl + "characters/" + id);
        return "redirect:/charactersList";
    }

    @GetMapping(value = {"/characters/edit/{id}"})
    public String editCharacter(@PathVariable int id, Model model){

        Character character = restTemplate.getForObject(baseUrl + "characters/" + id, Character.class);
        CharacterForm characterForm = new CharacterForm();

        HashMap m = new HashMap<>();
        m.put("characterForm", characterForm);
        m.put("character", character);

        model.addAllAttributes(m);

        return "editCharacter";
    }

    @PostMapping(value ="/editCharacter")
    public String updateCharacter(Model model, @ModelAttribute("characterForm") CharacterForm characterForm) {

        int id = characterForm.getId();
        String name = characterForm.getName();
        String type = characterForm.getType();

        if (name != null && name.length() > 0 //
                && type != null && type.length() > 0) {
            Character newCharacter = new Character(id, name, type);
            HttpEntity<Character> request = new HttpEntity<>(newCharacter);
            restTemplate.put(baseUrl + "/characters", request, Character.class);

            return "redirect:/charactersList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCharacter";
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
            HttpEntity<Character> request = new HttpEntity<>(newCharacter);
            restTemplate.postForObject(baseUrl + "/characters", request, Character.class);

            return "redirect:/charactersList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCharacter";
    }

}