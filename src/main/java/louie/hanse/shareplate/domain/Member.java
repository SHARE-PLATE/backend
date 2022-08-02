package louie.hanse.shareplate.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "members")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    private Long id;

    private String profileImageUrl;
    private String thumbnailImageUrl;
    private String nickname;
    private String email;
    private String location;
    private double latitude;
    private double longitude;
    private String refreshToken;


    public Member(Long id, String profileImageUrl, String thumbnailImageUrl, String nickname,
        String email) {
        this.id = id;
        this.profileImageUrl = profileImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.nickname = nickname;
        this.email = email;
    }

    public void changeProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeLocation(String location) {
        this.location = location;
    }

    public void changeLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void changeLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

