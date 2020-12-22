package lk.crystal.asset.user_management.user_session_log.entity;

import lk.crystal.asset.user_management.user.entity.User;
import lk.crystal.asset.user_management.user_session_log.entity.enums.UserSessionLogStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionLog {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;

    private int failureAttempts;

    @Column( updatable = false, nullable = false )
    private LocalDateTime createdAt;

    @Enumerated( EnumType.STRING )
    private UserSessionLogStatus userSessionLogStatus;

    @ManyToOne
    private User user;
}
