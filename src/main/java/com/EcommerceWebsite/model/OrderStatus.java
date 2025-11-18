package com.EcommerceWebsite.model;

public enum OrderStatus {
    PENDING,     // User placed order, waiting for admin approval
    PAID,        // Admin accepted order
    DELIVERED,   // Admin marked delivered
    CANCELLED    // User cancelled before admin approval
}
