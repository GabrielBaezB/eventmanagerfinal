package com.eventmanager.controllers;

import com.eventmanager.models.Event;
import com.eventmanager.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public String listEvents(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "events/list"; // Vista Thymeleaf
    }

    @GetMapping("/new")
    public String showEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "events/form"; // Vista Thymeleaf
    }

    @PostMapping
    public String saveEvent(@ModelAttribute Event event) {
        eventRepository.save(event);
        return "redirect:/events"; // Redirigir a la lista de eventos
    }
}
