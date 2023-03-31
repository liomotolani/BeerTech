package com.beerkhaton.mealtrackerapi.controller;

import com.beerkhaton.mealtrackerapi.dto.input.LoginInputDTO;
import com.beerkhaton.mealtrackerapi.dto.input.PasswordInputDTO;
import com.beerkhaton.mealtrackerapi.dto.output.BasicResponseDTO;
import com.beerkhaton.mealtrackerapi.dto.output.LoginResponseDTO;
import com.beerkhaton.mealtrackerapi.service.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController extends Controller {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Value("${qr.code.message}")
    private String code;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginInputDTO dto) throws Exception {
        return updateHttpStatus(userService.login(dto,authenticationManager));
    }

    @PutMapping("/change_password")
    public BasicResponseDTO changePassword(@RequestParam("code") String code, @RequestBody @Valid PasswordInputDTO dto) throws Exception {
        return updateHttpStatus(userService.changePassword(code,dto));
    }

    @GetMapping("/barcode")
    public void generateBarcode(HttpServletResponse response) throws Exception {
        int width = 300; // Set the width of the barcode image
        int height = 300; // Set the height of the barcode image
        String format = "png"; // Set the format of the barcode image
        BitMatrix bitMatrix = new MultiFormatWriter().encode(code, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream); // Write the barcode image to a byte array output stream
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setContentLength(outputStream.size());
        response.setHeader("Content-Disposition", "attachment; filename=qrcode.png");
        response.getOutputStream().write(outputStream.toByteArray()); // Write the barcode image to the HTTP response
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
