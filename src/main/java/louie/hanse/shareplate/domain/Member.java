package louie.hanse.shareplate.domain;

import javax.persistence.*;

@Table(name = "members")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImageUrl;
    private String nickname;
    private String email;
    private String location;
    private double latitude;
    private double longitude;
}

