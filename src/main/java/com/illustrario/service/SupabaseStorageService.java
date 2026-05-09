package com.illustrario.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String supabaseUrl;
    private final String supabaseKey;
    private final String bucket;

    public SupabaseStorageService(
        @Value("${supabase.url:}") String supabaseUrl,
        @Value("${supabase.key:}") String supabaseKey,
        @Value("${supabase.bucket:images}") String bucket
    ) {
        this.supabaseUrl = trimSlash(supabaseUrl);
        this.supabaseKey = supabaseKey;
        this.bucket = bucket;
    }

    public String save(MultipartFile file) throws IOException {
        validateConfig();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Nenhum arquivo foi enviado.");
        }

        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (extension.isBlank() ? "" : "." + extension);

        String objectPath = "artworks/" + filename;
        String thumbObjectPath = "artworks/thumb_" + filename;

        uploadObject(objectPath, file.getBytes(), resolveContentType(file));
        uploadObject(thumbObjectPath, buildThumbnail(file, extension), resolveThumbnailContentType(extension));

        return getPublicUrl(objectPath);
    }

    public String saveAvatar(MultipartFile file) throws IOException {
        validateConfig();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Nenhum arquivo foi enviado.");
        }

        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (extension.isBlank() ? "" : "." + extension);
        String objectPath = "avatars/" + filename;

        uploadObject(objectPath, file.getBytes(), resolveContentType(file));
        return getPublicUrl(objectPath);
    }

    public void deleteByPublicUrl(String publicUrl) {
        validateConfig();
        if (publicUrl == null || publicUrl.isBlank()) {
            return;
        }

        String marker = "/storage/v1/object/public/" + bucket + "/";
        int index = publicUrl.indexOf(marker);
        if (index < 0) {
            return;
        }

        String objectPath = publicUrl.substring(index + marker.length());
        deleteByObjectPath(objectPath, false);
        deleteByObjectPath(buildThumbObjectPath(objectPath), true);
    }

    public void deleteByObjectPath(String objectPath) {
        deleteByObjectPath(objectPath, false);
    }

    public String getPublicUrl(String objectPath) {
        validateConfig();
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + objectPath;
    }

    private void uploadObject(String objectPath, byte[] content, String contentType) throws IOException {
        String encodedPath = encodePathPreservingSlashes(objectPath);
        String uri = supabaseUrl + "/storage/v1/object/" + bucket + "/" + encodedPath;
        HttpResponse<String> response = sendWithAuthFallback(uri, contentType, HttpRequest.BodyPublishers.ofByteArray(content), "POST");
        if (response.statusCode() >= HttpStatus.BAD_REQUEST.value()) {
            throw new IOException(
                "Falha ao enviar arquivo para Supabase: HTTP " + response.statusCode() + " - " + response.body()
                    + ". Verifique SUPABASE_KEY, politicas do bucket e se ele eh publico para leitura."
            );
        }
    }

    private void deleteByObjectPath(String objectPath, boolean ignoreNotFound) {
        validateConfig();
        if (objectPath == null || objectPath.isBlank()) {
            return;
        }

        String encodedPath = encodePathPreservingSlashes(objectPath);
        String uri = supabaseUrl + "/storage/v1/object/" + bucket + "/" + encodedPath;
        HttpResponse<String> response;
        try {
            response = sendWithAuthFallback(uri, null, HttpRequest.BodyPublishers.noBody(), "DELETE");
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao conectar no Supabase para delecao.", e);
        }

        if (ignoreNotFound && response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            return;
        }
        if (response.statusCode() >= HttpStatus.BAD_REQUEST.value()) {
            throw new IllegalStateException("Falha ao deletar arquivo no Supabase: HTTP " + response.statusCode() + " - " + response.body());
        }
    }

    private byte[] buildThumbnail(MultipartFile file, String extension) throws IOException {
        String format = normalizeImageFormat(extension);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                .size(600, 600)
                .keepAspectRatio(true)
                .outputFormat(format)
                .toOutputStream(output);
            return output.toByteArray();
        }
    }

    private String resolveContentType(MultipartFile file) {
        return file.getContentType() == null || file.getContentType().isBlank()
            ? "application/octet-stream"
            : file.getContentType();
    }

    private String resolveThumbnailContentType(String extension) {
        String format = normalizeImageFormat(extension);
        return "image/" + format;
    }

    private String normalizeImageFormat(String extension) {
        String ext = extension == null ? "" : extension.toLowerCase(Locale.ROOT);
        if (ext.equals("jpg")) {
            return "jpeg";
        }
        if (ext.equals("jpeg") || ext.equals("png") || ext.equals("webp") || ext.equals("gif")) {
            return ext;
        }
        return "jpeg";
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private String buildThumbObjectPath(String objectPath) {
        int lastSlash = objectPath.lastIndexOf('/');
        if (lastSlash < 0) {
            return "thumb_" + objectPath;
        }
        String folder = objectPath.substring(0, lastSlash + 1);
        String filename = objectPath.substring(lastSlash + 1);
        return folder + "thumb_" + filename;
    }

    private String encodePathPreservingSlashes(String path) {
        String[] segments = path.split("/");
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < segments.length; i++) {
            if (i > 0) {
                encoded.append("/");
            }
            encoded.append(URLEncoder.encode(segments[i], StandardCharsets.UTF_8).replace("+", "%20"));
        }
        return encoded.toString();
    }

    private HttpResponse<String> sendWithAuthFallback(
        String uri,
        String contentType,
        HttpRequest.BodyPublisher bodyPublisher,
        String method
    ) throws IOException {
        HttpResponse<String> response = send(buildRequest(uri, contentType, bodyPublisher, method, true, true));
        if (!isAuthFailure(response.statusCode())) {
            return response;
        }

        HttpResponse<String> authOnly = send(buildRequest(uri, contentType, bodyPublisher, method, true, false));
        if (!isAuthFailure(authOnly.statusCode())) {
            return authOnly;
        }

        return send(buildRequest(uri, contentType, bodyPublisher, method, false, true));
    }

    private HttpRequest buildRequest(
        String uri,
        String contentType,
        HttpRequest.BodyPublisher bodyPublisher,
        String method,
        boolean includeAuthorization,
        boolean includeApiKey
    ) {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(uri));
        if (includeAuthorization) {
            builder.header("Authorization", "Bearer " + supabaseKey);
        }
        if (includeApiKey) {
            builder.header("apikey", supabaseKey);
        }
        if (contentType != null && !contentType.isBlank()) {
            builder.header("Content-Type", contentType);
        }
        if ("POST".equals(method)) {
            builder.header("x-upsert", "true");
            builder.POST(bodyPublisher);
        } else if ("DELETE".equals(method)) {
            builder.method("DELETE", bodyPublisher);
        } else {
            throw new IllegalArgumentException("Metodo HTTP nao suportado: " + method);
        }
        return builder.build();
    }

    private boolean isAuthFailure(int statusCode) {
        return statusCode == HttpStatus.UNAUTHORIZED.value() || statusCode == HttpStatus.FORBIDDEN.value();
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Requisicao interrompida.", e);
        }
    }

    private void validateConfig() {
        if (supabaseUrl.isBlank() || supabaseKey.isBlank() || bucket.isBlank()) {
            throw new IllegalStateException("Configuracao do Supabase ausente. Defina: supabase.url, supabase.key e supabase.bucket.");
        }
    }

    private String trimSlash(String value) {
        if (value == null) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
