package louie.hanse.shareplate.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.KeywordExceptionType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String contents;
    private String location;
    private double latitude;
    private double longitude;

    public Keyword(Member member, String contents, String location, double latitude,
        double longitude) {
        this.member = member;
        this.contents = contents;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void isNotMemberThrowException(Member member) {
        if (!this.member.equals(member)) {
            throw new GlobalException(KeywordExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD);
        }
    }
}
