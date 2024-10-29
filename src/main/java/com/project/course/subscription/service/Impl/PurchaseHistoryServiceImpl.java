package com.project.course.subscription.service.Impl;

import com.project.course.subscription.dto.PurchaseHistoryDTO;
import com.project.course.subscription.model.PurchaseHistory;
import com.project.course.subscription.repository.PurchaseHistoryRepository;
import com.project.course.subscription.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {

    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;

    @Override
    public List<PurchaseHistoryDTO> getAllPurchaseSubscriptionHistory() {
        List<PurchaseHistory> histories = purchaseHistoryRepository.findAll();
        return histories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void createPurchaseHistory(PurchaseHistory purchaseHistory) {
        purchaseHistoryRepository.save(purchaseHistory);
    }

    public List<PurchaseHistory> findPurchaseHistoryByUserAndSubscription(Long userId, Long subscriptionId) {
        return purchaseHistoryRepository.findPurchaseHistoryByUserAndSubscription(userId, subscriptionId);
    }

    public List<PurchaseHistoryDTO> getPurchaseHistoriesByPaxUserUuid(String uuid) {
        List<PurchaseHistory> histories = purchaseHistoryRepository.findByPaxUser_Uuid(uuid);
        return histories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurchaseHistoryDTO> getActivePurchaseHistories(String userUuid) {
        LocalDateTime now = LocalDateTime.now();
        List<PurchaseHistory> histories = purchaseHistoryRepository.findByPaxUser_UuidAndPurchaseDateLessThanEqualAndExpiryDateGreaterThanEqual(userUuid, now, now);
        return histories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PurchaseHistoryDTO convertToDTO(PurchaseHistory history) {
        PurchaseHistoryDTO dto = new PurchaseHistoryDTO();
        dto.setPaxUserUuid(history.getPaxUser().getUuid());
        dto.setClientName(history.getClientName());
        dto.setClientEmail(history.getClientEmail());
        dto.setPlanName(history.getPlanName());
        dto.setRenewalCount(history.getRenewalCount());
        dto.setPurchaseDate(history.getPurchaseDate());
        dto.setExpiryDate(history.getExpiryDate());
        dto.setNotificationType(history.getNotificationType());
        return dto;
    }
}
