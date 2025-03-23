package im.spear.app.service.manager;

import im.spear.app.controller.admin.api.managers.dto.CreateManagerDto;
import im.spear.app.controller.admin.api.managers.dto.LoginManagerDto;
import im.spear.app.controller.admin.api.managers.dto.UpdateMangerDto;
import im.spear.core.common.BasicSearch;
import im.spear.core.common.PageInfo;
import im.spear.core.common.PageResponse;
import im.spear.core.dto.user.ResponseUserDto;
import im.spear.core.global.exception.ApiException;
import im.spear.core.global.exception.ErrorCode;
import im.spear.core.global.jwt.dto.TokenResponse;
import im.spear.core.global.jwt.service.JwtService;
import im.spear.core.model.user.Role;
import im.spear.core.model.user.User;
import im.spear.core.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerApplicationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(CreateManagerDto createManagerDto) {
        userService.save(createManagerDto.getEmail(), createManagerDto.getNickname(), passwordEncoder.encode(createManagerDto.getPassword()));
    }

    @Transactional
    public TokenResponse login(LoginManagerDto loginManagerDto) {
        User manager = userService.findByEmailAndRole(loginManagerDto.getEmail(), Role.ADMIN);
        if (!passwordEncoder.matches(loginManagerDto.getPassword(), manager.getPassword())) {
            throw new ApiException(ErrorCode.LOGIN_FAILED);
        }
        TokenResponse tokenResponse = jwtService.issueJwtToken(manager.getId());
        manager.updateRefreshToken(tokenResponse.getRefreshToken());
        return tokenResponse;
    }

    @Transactional(readOnly = true)
    public PageResponse list(BasicSearch search) {
        Page<User> users = userService.list(search.getSearchString(), search.getPageable());
        List<ResponseUserDto> list = users.getContent().stream().map(ResponseUserDto::of).toList();
        return PageResponse.of(list, PageInfo.of(users));
    }

    @Transactional(readOnly = true)
    public ResponseUserDto retrieve(long id) {
        return ResponseUserDto.of(userService.findById(id));
    }

    @Transactional
    public void update(long id, UpdateMangerDto updateMangerDto) {
        userService.update(id, updateMangerDto.getNickname());
    }

    @Transactional
    public void delete(long id, long deletedBy) {
        userService.delete(id, deletedBy);
    }
}
