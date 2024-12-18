package com.project.course.subscription.controller;

import com.project.course.subscription.dto.SubscriptionDTO;
import com.project.course.subscription.model.Subscription;
import com.project.course.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/admin/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Object> createSubscription(@Valid @RequestBody Subscription subscription) {
        try {
            Subscription createdSubscription = subscriptionService.createSubscription(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<SubscriptionDTO>> getAllActiveSubscriptions() {
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllActiveSubscriptionList();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping
    public Page<SubscriptionDTO> getPaginatedAndSortedHeads(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "planName") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        return subscriptionService.getAllPaginatedAndSortedSubscription(page, size, sortBy, direction);
    }

    @GetMapping("/{uuid}")
    public Optional<SubscriptionDTO> getSubscriptionByUuid(@PathVariable String uuid) {
        return subscriptionService.getSubscriptionDTOByUuid(uuid);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SubscriptionDTO>> searchSubscription(@RequestParam String keyword,@RequestParam(defaultValue = "planName") String sortBy,
        @RequestParam(defaultValue = "asc") String direction){
        List<SubscriptionDTO> subscriptions = subscriptionService.searchSubscription(keyword, sortBy, direction);
        return ResponseEntity.ok(subscriptions);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateCategory(@PathVariable String uuid, @RequestBody Subscription Subscription) {
        try {
            Subscription updateSubscription = subscriptionService.updateSubscription(uuid, Subscription);
            return new ResponseEntity<>(updateSubscription, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteUserByUuid(@PathVariable String uuid) {
        boolean deactivated = subscriptionService.deleteSubscription(uuid);
        if (deactivated) {
            return ResponseEntity.ok("Subscription is deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
