package org.itsci.shop.config;

import org.itsci.shop.model.Authority;
import org.itsci.shop.model.AuthorityType;
import org.springframework.core.convert.converter.Converter;

public class StringToAuthorityConverter implements Converter<String, Authority> {
    @Override
    public Authority convert(String source) {
        AuthorityType at = AuthorityType.valueOf(source);
        return new Authority(at.toString());
    }
}
