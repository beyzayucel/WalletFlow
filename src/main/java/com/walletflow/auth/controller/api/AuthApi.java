package com.walletflow.auth.controller.api;

import com.walletflow.auth.dto.request.LoginRequest;
import com.walletflow.auth.dto.request.RegisterRequest;
import com.walletflow.auth.dto.response.LoginResponse;
import com.walletflow.auth.refreshtoken.dto.RefreshTokenRequest;
import com.walletflow.auth.verificationtoken.dto.request.PasswordRequest;
import com.walletflow.common.constants.ApiEndpoints;
import com.walletflow.common.response.ApiStandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Kimlik Doğrulama ve Kayıt API'leri")
@RequestMapping(ApiEndpoints.Auth.BASE)
public interface AuthApi {

    @Operation(summary = "Yeni kullanıcı kaydı", description = "Sistemde yeni bir kullanıcı oluşturur ve e-posta doğrulama linki gönderir.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Kullanıcı başarıyla kaydedildi"),
            @ApiResponse(responseCode = "400", description = "Validasyon hatası"),
            @ApiResponse(responseCode = "409", description = "E-posta adresi zaten kullanımda")
    })
    @PostMapping(ApiEndpoints.Auth.REGISTER)
    ResponseEntity<ApiStandardResponse<Void>> userRegister(@Valid @RequestBody RegisterRequest request);

    @Operation(summary = "E-posta token doğrulama", description = "Kullanıcıya gönderilen token'ı kontrol eder ve e-postayı doğrular.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token kontrolü başarılı"),
            @ApiResponse(responseCode = "400", description = "Geçersiz veya süresi dolmuş token")
    })
    @GetMapping(ApiEndpoints.Auth.VERIFY)
    ResponseEntity<ApiStandardResponse<Boolean>> verifyToken(
            @Parameter(description = "E-postaya gönderilen doğrulama token'ı", required = true)
            @RequestParam String token);

    @Operation(summary = "Şifre belirleme ve hesap aktifleştirme", description = "Kullanıcının şifresini kaydeder ve hesabı aktif hale getirir.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Şifre başarıyla belirlendi ve hesap aktif edildi"),
            @ApiResponse(responseCode = "400", description = "Validasyon hatası veya geçersiz token")
    })
    @PostMapping(ApiEndpoints.Auth.SET_PASSWORD)
    ResponseEntity<ApiStandardResponse<Void>> setPassword(@Valid @RequestBody PasswordRequest passwordRequest);

    @Operation(summary = "Kullanıcı girişi", description = "Email ve şifre ile giriş yapar. İlk giriş ise tokenları döner, değilse 2FA OTP tetikler.")
    @PostMapping(ApiEndpoints.Auth.LOGIN)
    ResponseEntity<ApiStandardResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest);

    @Operation(summary = "Token yenileme (Rotation)", description = "Süresi biten Access Token yerine Refresh Token ile yeni token ikilisi üretir.")
    @PostMapping(ApiEndpoints.Auth.REFRESH)
    ResponseEntity<ApiStandardResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest);

    @Operation(summary = "Çıkış yapma (Logout)", description = "Cihaza ait Refresh Token'ı Redis'ten silerek oturumu kapatır.")
    @PostMapping(ApiEndpoints.Auth.LOGOUT)
    ResponseEntity<ApiStandardResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest);

}
