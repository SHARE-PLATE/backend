package louie.hanse.shareplate.web.dto.share;

import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.type.MineType;
import louie.hanse.shareplate.type.ShareType;

@Getter
@Setter
public class ShareMineSearchRequest {

    private MineType mineType;
    private ShareType shareType;
    private boolean isExpired;
}
