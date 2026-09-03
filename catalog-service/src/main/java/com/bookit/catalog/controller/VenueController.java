package com.bookit.catalog.controller;

import com.bookit.catalog.model.Venue;
import com.bookit.catalog.repository.VenueRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/venues")
public class VenueController {

    private final VenueRepository venueRepository;

    public VenueController(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @GetMapping
    @Cacheable("venues")
    public List<Venue> listVenues() {
        return venueRepository.findAll();
    }

    @PostMapping
    @CacheEvict(value = "venues", allEntries = true)
    public Venue createVenue(@RequestBody Venue venue) {
        return venueRepository.save(venue);
    }
}
