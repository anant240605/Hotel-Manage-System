package com.HotelBookingSystem.HBS.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

    public enum Role {

        CUSTOMER,
        OWNER,
        ADMIN

    }

