package louie.hanse.shareplate.domain;

import louie.hanse.shareplate.type.ShareType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "share")
    private List<ShareImage> shareImages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ShareType type;

    private String title;
    private int price;
    private int originalPrice;
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime createDateTime;
}
