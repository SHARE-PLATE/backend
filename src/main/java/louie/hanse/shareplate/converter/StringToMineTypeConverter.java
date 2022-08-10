package louie.hanse.shareplate.converter;

import louie.hanse.shareplate.type.MineType;
import org.springframework.core.convert.converter.Converter;

public class StringToMineTypeConverter implements Converter<String, MineType> {

    @Override
    public MineType convert(String source) {
        return MineType.valueOfWithCaseInsensitive(source);
    }
}
