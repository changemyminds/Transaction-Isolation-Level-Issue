package com.darren.transactionisolation.isolation;

import lombok.Data;

import javax.persistence.*;

/**
 * Author: changemyminds.
 * Date: 2021/4/30.
 * Description:
 * Reference:
 */
@Data
@Table
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
}
