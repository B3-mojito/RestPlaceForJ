package com.sparta.restplaceforj.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "coworkers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coworker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Plan plan;

    @Builder
    public Coworker(User user, Plan plan) {
        this.user = user;
        this.plan = plan;
    }
}
