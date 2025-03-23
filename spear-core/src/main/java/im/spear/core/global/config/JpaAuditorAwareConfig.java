package im.spear.core.global.config;

import im.spear.core.global.UserInfo;
import im.spear.core.global.util.Util;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaAuditorAwareConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        UserInfo userInfo = Util.getUserInfo();
        if (null != userInfo) {
            return Optional.of(userInfo.getId());
        }
        return Optional.empty();
    }
}
