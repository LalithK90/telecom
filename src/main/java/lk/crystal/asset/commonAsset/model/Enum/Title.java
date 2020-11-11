package lk.crystal.asset.commonAsset.model.Enum;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Title {
    MR("Mr. "),
    MRS("Mrs. "),
    MISS("Miss. "),
    MS("Ms. "),
    REV("Rev. "),
    DR("Dr. "),
    DRMRS("Dr(Mrs). "),
    PRO("Prof. "),
    SISTER("Sister. ");

    public static Object Mr;
    private final String title;

    public class Mr {
    }
}

