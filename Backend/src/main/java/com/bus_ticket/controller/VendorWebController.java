package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bus_ticket.dto.Bus.BusDto;
import com.bus_ticket.dto.Vendor.VendorLoginRequest;
import com.bus_ticket.services.BusService;
import com.bus_ticket.services.VendorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/vendor")
public class VendorWebController {

    @Autowired
    private VendorService vendorService;
    
    @Autowired
    private BusService busService;

    @GetMapping("/login")
    public String loginPage() {
        return "vendor/login";
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String email, 
                             @RequestParam String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        try {
            vendorService.authenticateVendor(email, password);
            var vendor = vendorService.getVendorByEmail(email);
            
            session.setAttribute("vendorLoggedIn", true);
            session.setAttribute("vendorId", vendor.getId());
            session.setAttribute("vendorName", vendor.getVendorName());
            session.setAttribute("vendorEmail", vendor.getEmail());
            
            return "redirect:/vendor/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vendor/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/vendor/login?logout=true";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Long vendorId = (Long) session.getAttribute("vendorId");
        
        try {
            model.addAttribute("buses", busService.getBusesByVendor(vendorId));
        } catch (Exception e) {
            model.addAttribute("buses", java.util.Collections.emptyList());
            model.addAttribute("errorMessage", "Error loading buses: " + e.getMessage());
        }
        
        return "vendor/dashboard";
    }

    @GetMapping("/add-bus")
    public String addBusPage(Model model) {
        model.addAttribute("busDto", new BusDto());
        return "vendor/add-bus";
    }

    @PostMapping("/add-bus")
    public String addBus(@Valid @ModelAttribute("busDto") BusDto busDto,
                        BindingResult bindingResult,
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        
        if (bindingResult.hasErrors()) {
            return "vendor/add-bus";
        }
        
        try {
            Long vendorId = (Long) session.getAttribute("vendorId");
            busDto.setVendorId(vendorId);
            
            busService.addBus(busDto);
            redirectAttributes.addFlashAttribute("successMessage", "Bus added successfully!");
            return "redirect:/vendor/add-bus";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "vendor/add-bus";
        }
    }

    @PostMapping("/buses/{id}/delete")
    public String deleteBus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            busService.deleteBus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bus removed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error removing bus: " + e.getMessage());
        }
        return "redirect:/vendor/dashboard";
    }
}
