package com.github.automeican.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP OAuth 保护资源元数据端点
 * 提供 SSE 端点信息，避免 404 错误
 */
@RestController
public class McpMetadataController {

    /**
     * OAuth 2.0 保护资源元数据端点
     * 返回 SSE 端点的元数据信息
     */
    @GetMapping(value = "/.well-known/oauth-authorization-server/sse")
    public ResponseEntity<Map<String, Object>> getOAuthProtectedResourceMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("resource", "sse");
        metadata.put("sse_endpoint", "/sse");
        metadata.put("description", "Server-Sent Events endpoint for MCP");
        return ResponseEntity.ok(metadata);
    }
}
