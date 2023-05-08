package com.ltp.globalsuperstore.Controller;

import com.ltp.globalsuperstore.Constants;
import com.ltp.globalsuperstore.Item;
import com.ltp.globalsuperstore.Service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class inventoryController {

    InventoryService inventoryService = new InventoryService();

    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id) {
//        model.addAttribute("categories", Constants.CATEGORIES);
        model.addAttribute("item", inventoryService.getItemById(id));
        return "form";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        model.addAttribute("items", inventoryService.getItems());

        return "inventory";
    }

    @PostMapping("/submitItem")
    public String handleSubmit(@Valid Item item, BindingResult result, RedirectAttributes redirectAttributes) {
        System.out.println("Has errors? :" + result.hasErrors());

        if (item.getPrice() < item.getDiscount()) {
            result.rejectValue("price", "", "Price can't be less than discount");
        }
        if (result.hasErrors()) return "form";

        String status = inventoryService.submitItem(item);
        redirectAttributes.addFlashAttribute("status", status);

        return "redirect:/inventory";
    }





}
