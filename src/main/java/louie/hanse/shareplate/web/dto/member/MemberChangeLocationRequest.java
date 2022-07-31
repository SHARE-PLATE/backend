package louie.hanse.shareplate.web.dto.member;

import lombok.Getter;

@Getter
public class MemberChangeLocationRequest {
    private String location;
    private double longitude;
    private double latitude;


}
