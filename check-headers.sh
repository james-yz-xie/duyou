#!/bin/bash

# 对比 duyou.me 和 duyou-me.pages.dev 的响应头和 SSL 证书
# 用于验证自定义域名是否正确映射到 Cloudflare Pages

DOMAIN1="duyou.me"
DOMAIN2="duyou-me.pages.dev"

echo "=========================================="
echo "对比 duyou.me 和 duyou-me.pages.dev"
echo "=========================================="
echo ""

# 获取响应头
echo "=== 获取响应头 ==="
HEADERS1=$(curl -sI "https://${DOMAIN1}")
HEADERS2=$(curl -sI "https://${DOMAIN2}")

echo ""
echo "📍 ${DOMAIN1} 响应头:"
echo "${HEADERS1}" | while read line; do echo "  $line"; done

echo ""
echo "📍 ${DOMAIN2} 响应头:"
echo "${HEADERS2}" | while read line; do echo "  $line"; done

# 提取 CF-RAY
echo ""
echo "=========================================="
echo "CF-RAY 对比"
echo "=========================================="
RAY1=$(echo "${HEADERS1}" | grep -i "CF-Ray" | awk '{$1=$2=""; print $0}' | xargs)
RAY2=$(echo "${HEADERS2}" | grep -i "CF-Ray" | awk '{$1=$2=""; print $0}' | xargs)

echo "  ${DOMAIN1}: ${RAY1:-N/A}"
echo "  ${DOMAIN2}: ${RAY2:-N/A}"

# 提取 SSL 证书信息
echo ""
echo "=========================================="
echo "SSL 证书信息"
echo "=========================================="

# 获取证书详细信息
CERT_INFO1=$(curl -s --head "https://${DOMAIN1}" | openssl x509 -noout -dates -issuer 2>/dev/null || echo "无法获取证书信息")
CERT_INFO2=$(curl -s --head "https://${DOMAIN2}" | openssl x509 -noout -dates -issuer 2>/dev/null || echo "无法获取证书信息")

echo ""
echo "📍 ${DOMAIN1}:"
echo "${CERT_INFO1}" | while read line; do echo "  $line"; done

echo ""
echo "📍 ${DOMAIN2}:"
echo "${CERT_INFO2}" | while read line; do echo "  $line"; done

# 证书有效期分析
echo ""
echo "=========================================="
echo "证书有效期对比"
echo "=========================================="

EXPIRY1=$(curl -s --head "https://${DOMAIN1}" | openssl x509 -noout -enddate 2>/dev/null | cut -d= -f2)
EXPIRY2=$(curl -s --head "https://${DOMAIN2}" | openssl x509 -noout -enddate 2>/dev/null | cut -d= -f2)

echo "  ${DOMAIN1} 过期时间：${EXPIRY1:-N/A}"
echo "  ${DOMAIN2} 过期时间：${EXPIRY2:-N/A}"

# 检查证书是否相同（自定义域名应该和 root 域名证书一致）
echo ""
echo "=========================================="
echo "域名映射验证"
echo "=========================================="

if [ -n "${RAY1}" ] && [ -n "${RAY2}" ]; then
    if echo "${RAY1}" | grep -q "Cloudflare" && echo "${RAY2}" | grep -q "Cloudflare"; then
        echo "✅ 两个域名都使用 Cloudflare"
    else
        echo "⚠️  请确认两个域名是否都正确配置 Cloudflare"
    fi
else
    echo "⚠️  无法获取 CF-Ray，可能未通过 Cloudflare 访问"
fi

echo ""
echo "提示：CF-RAY 是每次请求的唯一标识，不会相同"
echo "      但两个域名都应该有 CF-Ray 头，且都来自 Cloudflare"
