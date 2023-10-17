package com.fakecoders.foundation.quickstart.model.audit;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import org.hibernate.envers.Audited;

import com.fakecoders.foundation.quickstart.model.User;

@MappedSuperclass
@Getter
@Setter
@Audited
public class BaseEntity {

   // @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", updatable = false)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time")
    private Date modifiedTime;

    @ManyToOne
    @JoinColumn(name = "creator_id", updatable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "modifier_id")
    private User modifier;

    // Getter and Setter methods for id, createdTime, modifiedTime, creator, modifier

    @PrePersist
    protected void onCreate() {
        // Set the creator and createdTime only during creation
        this.creator = getCurrentUser();
        this.createdTime = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        // Set the modifier and modifiedTime during updates
        this.modifier = getCurrentUser();
        this.modifiedTime = new Date();
    }

    private User getCurrentUser() {
        // Implement a mechanism to get the current user (e.g., using Spring Security)
        // Replace this with your actual implementation to retrieve the current user.
        // You may use a custom UserDetails object or other means to get user information.
       // return getCurrentUserFromSecurityContext();
    	return null;
    }
    
    // You can include other common fields and methods that you need.
}

