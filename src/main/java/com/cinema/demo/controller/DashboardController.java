package com.cinema.demo.controller;

import com.cinema.demo.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Statistics object for cards
        model.addAttribute("stats", new Object() {
            public long getTotalFilms() { return statisticsService.getTotalFilms(); }
            public long getTotalSalles() { return statisticsService.getTotalSalles(); }
            public long getTotalSeances() { return statisticsService.getTotalSeances(); }
            public long getTotalReservations() { return statisticsService.getTotalReservations(); }
        });
        
        // Upcoming seances and recent films
        model.addAttribute("upcomingSeances", statisticsService.getProchainesSeances(5));
        model.addAttribute("recentFilms", statisticsService.getFilmsRecents(5));
        
        return "admin/dashboard";
    }
}
