package louie.hanse.shareplate.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @OneToMany(mappedBy = "writer")
    private List<Share> shares = new ArrayList<>();

    private String profileImageUrl;
    private String thumbnailImageUrl;
    private String nickname;
    private String email;
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

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(profileImageUrl,
            member.profileImageUrl) && Objects.equals(thumbnailImageUrl,
            member.thumbnailImageUrl) && Objects.equals(nickname, member.nickname)
            && Objects.equals(email, member.email) && Objects.equals(refreshToken,
            member.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, profileImageUrl, thumbnailImageUrl, nickname, email, refreshToken);
    }
}

