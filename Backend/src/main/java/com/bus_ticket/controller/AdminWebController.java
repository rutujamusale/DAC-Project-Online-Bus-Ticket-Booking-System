package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bus_ticket.dto.VendorDto;
import com.bus_ticket.services.AdminService;
import com.bus_ticket.services.VendorService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private VendorService vendorService;

    @GetMapping("/")
    public String home() {
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String username, 
                             @RequestParam String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        // Hardcoded admin credentials
    	
        if ("admin".equals(username) && "1234".equals(password)) {
            session.setAttribute("adminLoggedIn", true);
            session.setAttribute("adminUsername", username);
            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/admin/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login?logout=true";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get dashboard data
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalUsers", adminService.getTotalUsers());
        dashboardData.put("totalVendors", adminService.getTotalVendors());
        dashboardData.put("dailyTraffic", adminService.getDailyTraffic());
        
        model.addAttribute("dashboardData", dashboardData);
        
        // Get recent vendors (limit to 5)
        try {
            model.addAttribute("recentVendors", vendorService.getAllVendors().stream().limit(5).toList());
        } catch (Exception e) {
            model.addAttribute("recentVendors", java.util.Collections.emptyList());
        }
        
        return "admin/dashboard";
    }

    @GetMapping("/add-vendor")
    public String addVendorPage(Model model) {
        model.addAttribute("vendorDto", new VendorDto());
        return "admin/add-vendor";
    }

    @PostMapping("/add-vendor")
    public String addVendor(@Valid @ModelAttribute("vendorDto") VendorDto vendorDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        
        if (bindingResult.hasErrors()) {
            return "admin/add-vendor";
        }
        
        try {
            vendorService.addVendor(vendorDto);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor added successfully!");
            return "redirect:/admin/add-vendor";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/add-vendor";
        }
    }

    @GetMapping("/vendors")
    public String vendorsPage(Model model) {
        try {
            model.addAttribute("vendors", vendorService.getAllVendors());
        } catch (Exception e) {
            model.addAttribute("vendors", java.util.Collections.emptyList());
            model.addAttribute("errorMessage", "Error loading vendors: " + e.getMessage());
        }
        return "admin/vendors";
    }

    @PostMapping("/vendors/{id}/delete")
    public String deleteVendor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vendorService.softDeleteVendor(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor removed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error removing vendor: " + e.getMessage());
        }
        return "redirect:/admin/vendors";
    }
}
