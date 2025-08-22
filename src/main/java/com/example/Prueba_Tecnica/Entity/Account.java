package com.example.Prueba_Tecnica.Entity;

import com.example.Prueba_Tecnica.Entity.Enums.AccountType;
import com.example.Prueba_Tecnica.Entity.Enums.AccountStatus;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotBlank
    @Column(name = "account_number", nullable = false, unique = true, length = 10)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @NotNull
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @NotNull
    @Column(name = "gmf_exempt")
    private boolean gmfExempt;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "modification_date")
    private LocalDateTime modificationDate;

    // 👇 Campo para borrado lógico
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
        this.active = true;

        if (this.accountType == AccountType.SAVINGS && this.status == null) {
            this.status = AccountStatus.ACTIVE;
        }

        if (this.accountNumber == null) {
            this.accountNumber = generateAccountNumber();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.modificationDate = LocalDateTime.now();

        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException("A closed account cannot be modified");
        }
    }

    private String generateAccountNumber() {
        String prefix = this.accountType == AccountType.SAVINGS ? "53" : "33";
        String randomDigits = String.format("%08d", (long) (Math.random() * 100000000L));
        String numberWithoutCheckDigit = prefix + randomDigits;
        int checkDigit = calculateCheckDigit(numberWithoutCheckDigit);
        return numberWithoutCheckDigit + checkDigit;
    }

    private int calculateCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }

    public boolean canBeClosed() {
        if (this.status == AccountStatus.CLOSED) {
            return false;
        }

        BigDecimal epsilon = new BigDecimal("0.00001");
        boolean zeroBalance = balance != null && balance.abs().compareTo(epsilon) < 0;

        return zeroBalance;
    }
}
