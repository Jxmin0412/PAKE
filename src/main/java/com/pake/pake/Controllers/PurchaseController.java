package com.pake.pake.Controllers;

import com.pake.pake.DTO.PurchaseRequest;
import com.pake.pake.Services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirmPurchase(@RequestBody PurchaseRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            purchaseService.processPurchase(request);
            response.put("success", true);
            response.put("message", "Purchase successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}